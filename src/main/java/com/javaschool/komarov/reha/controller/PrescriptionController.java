package com.javaschool.komarov.reha.controller;

import com.javaschool.komarov.reha.model.dto.PatientDto;
import com.javaschool.komarov.reha.model.dto.PrescriptionDto;
import com.javaschool.komarov.reha.service.impl.PatientServiceImpl;
import com.javaschool.komarov.reha.service.impl.PrescriptionServiceImpl;
import com.javaschool.komarov.reha.service.impl.ValidationServiceImpl;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Controller
@RequestMapping("/patient/{id}")
public class PrescriptionController {
    private final PatientServiceImpl patientServiceImpl;
    private final PrescriptionServiceImpl prescriptionServiceImpl;
    private final ValidationServiceImpl validationServiceImpl;

    /**
     * Method return model with patient information
     *
     * @param id patient id
     * @return model
     */
    @ModelAttribute("patientInfo")
    public PatientDto patientInfo(@PathVariable("id") long id) {
        return patientServiceImpl.getPatientDTOById(id);
    }

    /**
     * Method return model for new patient
     *
     * @return model
     */
    @ModelAttribute("updatedPatient")
    public PatientDto updatedPatient() {
        return new PatientDto();
    }

    /**
     * Method return prescriptions for patient
     *
     * @param id patient id
     * @return model
     */
    @ModelAttribute("prescriptions")
    public Iterable<PrescriptionDto> prescriptions(@PathVariable("id") long id) {
        return prescriptionServiceImpl.getPrescriptionsDTOByPatientId(id);
    }

    /**
     * Method return model for new prescription
     *
     * @return model
     */
    @ModelAttribute("newPrescription")
    public PrescriptionDto newPrescription() {
        return new PrescriptionDto();
    }

    /**
     * Method return page with patient's prescriptions
     *
     * @return html
     */
    @GetMapping("/prescription")
    @PreAuthorize("hasAnyAuthority('employee:read')")
    public String prescription() {
        return "prescription";
    }

    /**
     * Method to save new prescription for patient
     *
     * @param id              patient id
     * @param userDetails     employee info
     * @param prescriptionDto new prescription
     * @param bindingResult   validation result
     * @return html
     */
    @PostMapping("/prescription/add")
    @PreAuthorize("hasAnyAuthority('employee:write')")
    public String addPrescription(@PathVariable("id") long id, @AuthenticationPrincipal UserDetails userDetails,
                                  @ModelAttribute("newPrescription") PrescriptionDto prescriptionDto,
                                  BindingResult bindingResult) {
        validationServiceImpl.checkPrescription(prescriptionDto, bindingResult, id, userDetails);
        if (bindingResult.hasErrors()) {
            return "prescription";
        } else {
            prescriptionServiceImpl.savePrescription(prescriptionDto, userDetails, id);
            return "redirect:/patient/" + id + "/prescription";
        }
    }

    /**
     * Method to update patient status
     *
     * @param id            patient id
     * @param patientDto    updatable patient
     * @param bindingResult validation result
     * @return html
     */
    @PostMapping("/prescription/update")
    @PreAuthorize("hasAnyAuthority('employee:write')")
    public String updatePatientStatus(@PathVariable("id") long id,
                                      @ModelAttribute("updatedPatient") PatientDto patientDto,
                                      BindingResult bindingResult) {
        validationServiceImpl.checkPatientStatus(patientDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "prescription";
        } else {
            patientServiceImpl.updatePatientStatus(patientDto);
            return "redirect:/patient/" + id + "/prescription";
        }
    }

}
