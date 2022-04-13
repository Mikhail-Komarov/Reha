package com.javaschool.komarov.reha.controller;

import com.javaschool.komarov.reha.model.dto.TherapyDto;
import com.javaschool.komarov.reha.service.impl.TherapyServiceImpl;
import com.javaschool.komarov.reha.service.impl.ValidationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/therapy")
@Controller
public class TherapyController {
    private final TherapyServiceImpl therapyService;
    private final ValidationServiceImpl validationService;

    /**
     * Method return model with all available therapies
     *
     * @return model
     */
    @ModelAttribute("therapies")
    public Iterable<TherapyDto> therapyDtos() {
        return therapyService.getAllTherapiesDTO();
    }

    /**
     * Method return page with all available therapies
     *
     * @param model model with new therapy
     * @return html
     */
    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('employee:read')")
    public String getTherapies(Model model) {
        model.addAttribute("newTherapy", new TherapyDto());
        return "therapy";
    }

    /**
     * Method to save new therapy
     *
     * @param therapyDto    new therapy
     * @param bindingResult validation result
     * @return html
     */
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
