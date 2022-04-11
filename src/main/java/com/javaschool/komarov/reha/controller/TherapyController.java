package com.javaschool.komarov.reha.controller;

import com.javaschool.komarov.reha.model.dto.TherapyDto;
import com.javaschool.komarov.reha.service.impl.TherapyServiceImpl;
import com.javaschool.komarov.reha.service.impl.ValidationServiceImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/therapy")
@Controller
public class TherapyController {
    private final TherapyServiceImpl therapyService;
    private final ValidationServiceImpl validationService;

    public TherapyController(TherapyServiceImpl therapyService, ValidationServiceImpl validationService) {
        this.therapyService = therapyService;
        this.validationService = validationService;
    }

    @ModelAttribute("therapies")
    public Iterable<TherapyDto> therapyDtos() {
        return therapyService.getAllTherapiesDTO();
    }

    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('employee:read')")
    public String getTherapies(Model model) {
        model.addAttribute("newTherapy", new TherapyDto());
        return "therapy";
    }

    @PostMapping("/new")
    @PreAuthorize("hasAnyAuthority('employee:add')")
    public String addTherapy(@ModelAttribute("newTherapy") TherapyDto therapyDto, BindingResult bindingResult) {
        validationService.checkTherapy(therapyDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "therapy";
        } else {
            therapyService.saveTherapy(therapyDto);
            return "redirect:/therapy";
        }
    }
}
