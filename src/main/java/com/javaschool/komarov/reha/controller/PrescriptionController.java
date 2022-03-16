package com.javaschool.komarov.reha.controller;

import com.javaschool.komarov.reha.model.PatientStatus;
import com.javaschool.komarov.reha.service.PatientService;
import com.javaschool.komarov.reha.service.PrescriptionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PrescriptionController {
    private final PatientService patientService;
    private final PrescriptionService prescriptionService;

    public PrescriptionController(PatientService patientService, PrescriptionService prescriptionService) {
        this.patientService = patientService;
        this.prescriptionService = prescriptionService;
    }

    @GetMapping("/patient/{id}/prescription")
    @PreAuthorize("hasAnyAuthority('employee:read')")
    public String prescription(@PathVariable("id") long id, Model model) {
        model.addAttribute("patientInfo", patientService.getPatientById(id));
        model.addAttribute("prescriptions", prescriptionService.getPrescriptionByPatientId(id));
        return "prescription";
    }

    @PostMapping("/patient/{id}/prescription/add")
    @PreAuthorize("hasAnyAuthority('employee:write')")
    public String addPrescription(@PathVariable("id") long id, @AuthenticationPrincipal UserDetails userDetails,
                                  @RequestParam String diagnosis) {
        prescriptionService.savePrescription(id, userDetails, diagnosis);
        return "redirect:/patient/" + id + "/prescription";
    }

    @PostMapping("/patient/{id}/prescription/update")
    @PreAuthorize("hasAnyAuthority('employee:write')")
    public String updatePatientStatus(@PathVariable("id") long id,
                                      @RequestParam String status) {
        patientService.updatePatientStatus(id, PatientStatus.valueOf(status));
        return "redirect:/patient/" + id + "/prescription";
    }

}
