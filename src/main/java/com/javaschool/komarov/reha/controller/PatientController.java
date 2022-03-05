package com.javaschool.komarov.reha.controller;

import com.javaschool.komarov.reha.dto.PatientDto;
import com.javaschool.komarov.reha.service.PatientService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/patient")
    @PreAuthorize("hasAnyAuthority('employee:read')")
    public String main(Model model) {
        model.addAttribute("patient", patientService.getAllPatients());
        model.addAttribute("newPatient", new PatientDto());
        return "patient";
    }

    @PostMapping("/patient/new")
    @PreAuthorize("hasAnyAuthority('employee:write')")
    public String add(@ModelAttribute("patient") PatientDto patientDto) {
        patientService.savePatient(patientDto);
        return "redirect:/patient";
    }
}
