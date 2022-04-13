package com.javaschool.komarov.reha.controller;

import com.javaschool.komarov.reha.model.dto.PatientDto;
import com.javaschool.komarov.reha.service.impl.PatientServiceImpl;
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
@RequestMapping("/patient")
@Controller
public class PatientController {
    private final PatientServiceImpl patientServiceImpl;
    private final ValidationServiceImpl validationServiceImpl;

    /**
     * Method return model with all patients
     *
     * @return model
     */
    @ModelAttribute("patients")
    public Iterable<PatientDto> patientDtos() {
        return patientServiceImpl.getAllPatientsDTO();
    }

    /**
     * Method return page with all patients
     *
     * @param model model with new patient
     * @return html
     */
    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('employee:read')")
    public String getPatients(Model model) {
        model.addAttribute("newPatient", new PatientDto());
        return "patient";
    }

    /**
     * Method to save new patient
     *
     * @param patientDto    new patient
     * @param bindingResult validation result
     * @return html
     */
    @PostMapping("/new")
    @PreAuthorize("hasAnyAuthority('employee:write')")
    public String addPatient(@ModelAttribute("newPatient") PatientDto patientDto, BindingResult bindingResult) {
        validationServiceImpl.checkPatient(patientDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "patient";
        } else {
            patientServiceImpl.savePatient(patientDto);
            return "redirect:/patient";
        }
    }
}
