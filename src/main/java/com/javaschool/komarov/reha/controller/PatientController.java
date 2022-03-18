package com.javaschool.komarov.reha.controller;

import com.javaschool.komarov.reha.dto.PatientDto;
import com.javaschool.komarov.reha.service.PatientService;
import com.javaschool.komarov.reha.service.ValidationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/patient")
@Controller
public class PatientController {
    private final PatientService patientService;
    private final ValidationService validationService;

    public PatientController(PatientService patientService, ValidationService validationService) {
        this.patientService = patientService;
        this.validationService = validationService;
    }

    @ModelAttribute("patients")
    public Iterable<PatientDto> patientDtos() {
        return patientService.getAllPatients();
    }

    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('employee:read')")
    public String main(Model model) {
        model.addAttribute("newPatient", new PatientDto());
        return "patient";
    }

    @PostMapping("/new")
    @PreAuthorize("hasAnyAuthority('employee:write')")
    public String addPatient(@ModelAttribute("newPatient") PatientDto patientDto, BindingResult bindingResult) {
        validationService.checkPatient(patientDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "patient";
        } else {
            patientService.savePatient(patientDto);
            return "redirect:/patient";
        }
    }
}
