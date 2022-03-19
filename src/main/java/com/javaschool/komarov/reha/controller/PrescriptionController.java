package com.javaschool.komarov.reha.controller;

import com.javaschool.komarov.reha.dto.PatientDto;
import com.javaschool.komarov.reha.dto.PrescriptionDto;
import com.javaschool.komarov.reha.service.PatientService;
import com.javaschool.komarov.reha.service.PrescriptionService;
import com.javaschool.komarov.reha.service.ValidationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/patient/{id}")
public class PrescriptionController {
    private final PatientService patientService;
    private final PrescriptionService prescriptionService;
    private final ValidationService validationService;

    public PrescriptionController(PatientService patientService, PrescriptionService prescriptionService, ValidationService validationService) {
        this.patientService = patientService;
        this.prescriptionService = prescriptionService;
        this.validationService = validationService;
    }

    @ModelAttribute("patientInfo")
    public PatientDto patientInfo(@PathVariable("id") long id) {
        return patientService.getPatientById(id);
    }

    @ModelAttribute("updatedPatient")
    public PatientDto updatedPatient() {
        return new PatientDto();
    }

    @ModelAttribute("prescriptions")
    public Iterable<PrescriptionDto> prescriptions(@PathVariable("id") long id) {
        return prescriptionService.getPrescriptionByPatientId(id);
    }

    @ModelAttribute("newPrescription")
    public PrescriptionDto newPrescription() {
        return new PrescriptionDto();
    }

    @GetMapping("/prescription")
    @PreAuthorize("hasAnyAuthority('employee:read')")
    public String prescription() {
        return "prescription";
    }

    @PostMapping("/prescription/add")
    @PreAuthorize("hasAnyAuthority('employee:write')")
    public String addPrescription(@PathVariable("id") long id, @AuthenticationPrincipal UserDetails userDetails,
                                  @ModelAttribute("newPrescription") PrescriptionDto prescriptionDto,
                                  BindingResult bindingResult) {
        validationService.checkPrescription(prescriptionDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "prescription";
        } else {
            prescriptionService.savePrescription(prescriptionDto, userDetails, id);
            return "redirect:/patient/" + id + "/prescription";
        }
    }

    @PostMapping("/prescription/update")
    @PreAuthorize("hasAnyAuthority('employee:write')")
    public String updatePatientStatus(@PathVariable("id") long id,
                                      @ModelAttribute("updatedPatient") PatientDto patientDto,
                                      BindingResult bindingResult) {
        validationService.checkPatientStatus(patientDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "prescription";
        } else {
            patientService.updatePatientStatus(patientDto);
            return "redirect:/patient/" + id + "/prescription";
        }
    }

}
