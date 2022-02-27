package com.javaschool.komarov.reha.controller;

import com.javaschool.komarov.reha.model.Patient;
import com.javaschool.komarov.reha.model.PatientStatus;
import com.javaschool.komarov.reha.repository.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PatientController {
    @Autowired
    private PatientRepo patientRepo;

    @GetMapping("/patient")
    @PreAuthorize("hasAnyAuthority('employee:read')")
    public String main(Model model) {
        model.addAttribute("name", "Test message");
        Iterable<Patient> patients = patientRepo.findAll();
        model.addAttribute("patient", patients);
        return "patient";
    }

    @PostMapping("/patient")
    @PreAuthorize("hasAnyAuthority('employee:write')")
    public String add(@RequestParam String firstName, @RequestParam String lastName,
                      @RequestParam String healthInsurance, Model model) {
        Patient patient = new Patient(firstName, lastName, healthInsurance, PatientStatus.ISTREATED);
        patientRepo.save(patient);
        Iterable<Patient> patients = patientRepo.findAll();
        model.addAttribute("patient", patients);

        return "patient";
    }
}
