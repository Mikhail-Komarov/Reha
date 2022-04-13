package com.javaschool.komarov.reha.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.javaschool.komarov.reha.model.dto.EmployeeDto;
import com.javaschool.komarov.reha.model.dto.PatientDto;
import com.javaschool.komarov.reha.model.dto.PrescriptionDto;
import com.javaschool.komarov.reha.model.dto.PrescriptionItemDto;
import com.javaschool.komarov.reha.model.dto.TherapyDto;
import com.javaschool.komarov.reha.service.impl.EmployeeServiceImpl;
import com.javaschool.komarov.reha.service.impl.PatientServiceImpl;
import com.javaschool.komarov.reha.service.impl.PrescriptionItemServiceImpl;
import com.javaschool.komarov.reha.service.impl.PrescriptionServiceImpl;
import com.javaschool.komarov.reha.service.impl.SenderServiceImpl;
import com.javaschool.komarov.reha.service.impl.TherapyServiceImpl;
import com.javaschool.komarov.reha.service.impl.ValidationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/patient/{id}/prescription/{num}")
public class PrescriptionItemController {
    private final PatientServiceImpl patientServiceImpl;
    private final PrescriptionServiceImpl prescriptionServiceImpl;
    private final PrescriptionItemServiceImpl prescriptionItemServiceImpl;
    private final EmployeeServiceImpl employeeServiceImpl;
    private final TherapyServiceImpl therapyServiceImpl;
    private final ValidationServiceImpl validationServiceImpl;
    private final SenderServiceImpl senderServiceImpl;

    /**
     * Method return model with patient info
     *
     * @param id patient id
     * @return model
     */
    @ModelAttribute("patientInfo")
    public PatientDto patientInfo(@PathVariable("id") long id) {
        return patientServiceImpl.getPatientDTOById(id);
    }

    /**
     * Method return model with prescription info
     *
     * @param num prescription number
     * @return model
     */
    @ModelAttribute("prescriptionInfo")
    public PrescriptionDto prescriptionInfo(@PathVariable("num") long num) {
        return prescriptionServiceImpl.getPrescriptionDTOById(num);
    }

    /**
     * Method return prescription items for patient's prescription
     *
     * @param num prescription number
     * @return model
     */
    @ModelAttribute("items")
    public Iterable<PrescriptionItemDto> items(@PathVariable("num") long num) {
        return prescriptionItemServiceImpl.getPrescriptionItemDTOByPrescriptionID(num);
    }

    /**
     * Method return model with employee info
     *
     * @param userDetails employee info
     * @return model
     */
    @ModelAttribute("doctor")
    public EmployeeDto doctor(@AuthenticationPrincipal UserDetails userDetails) {
        return employeeServiceImpl.getEmployeeDTOByLogin(userDetails.getUsername());
    }

    /**
     * Method return model for new item
     *
     * @return model
     */
    @ModelAttribute("newItem")
    public PrescriptionItemDto newItem() {
        return new PrescriptionItemDto();
    }

    /**
     * Method return model for updatable item
     *
     * @return model
     */
    @ModelAttribute("updateItem")
    public PrescriptionItemDto updateItem() {
        return new PrescriptionItemDto();
    }

    /**
     * Method return model with all available therapies
     *
     * @return model
     */
    @ModelAttribute("therapies")
    public Iterable<TherapyDto> therapies() {
        return therapyServiceImpl.getAllTherapiesDTO();
    }

    /**
     * Method return page with all prescription items
     *
     * @return html
     */
    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('employee:read')")
    public String prescription() {
        return "item";
    }

    /**
     * Method to save new prescription item
     *
     * @param id                  patient id
     * @param num                 prescription number
     * @param prescriptionItemDto new item
     * @param bindingResult       validation result
     * @param userDetails         employee info
     * @param model               model to adding error messages
     * @return html
     */
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('employee:read')")
    public String addPrescription(@PathVariable("id") long id, @PathVariable("num") long num,
                                  @ModelAttribute("newItem") PrescriptionItemDto prescriptionItemDto,
                                  BindingResult bindingResult, @AuthenticationPrincipal UserDetails userDetails,
                                  Model model) throws JsonProcessingException {
        validationServiceImpl.checkPrescriptionItem(prescriptionItemDto, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", "Adding error!");
            return "item";
        } else {
            prescriptionItemServiceImpl.savePrescriptionItem(prescriptionItemDto, userDetails);
            senderServiceImpl.sendEvents();
            return "redirect:/patient/" + id + "/prescription/" + num;
        }
    }

    /**
     * Method to update prescription item
     *
     * @param id                  patient id
     * @param num                 prescription number
     * @param prescriptionItemDto updatable item
     * @param bindingResult       validation result
     * @param userDetails         employee info
     * @param model               model to adding error messages
     * @return html
     */
    @PostMapping("/upd")
    @PreAuthorize("hasAnyAuthority('employee:read')")
    public String updPrescription(@PathVariable("id") long id, @PathVariable("num") long num,
                                  @ModelAttribute("updateItem") PrescriptionItemDto prescriptionItemDto,
                                  BindingResult bindingResult,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  Model model) throws JsonProcessingException {
        validationServiceImpl.checkPatternUpdate(prescriptionItemDto, bindingResult);
        if (!bindingResult.hasErrors()) {
            validationServiceImpl.checkPrescriptionItem(prescriptionItemDto, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", "Error in item id:" + prescriptionItemDto.getItemId());
            return "item";
        } else {
            prescriptionItemServiceImpl.updatePrescriptionItem(prescriptionItemDto, userDetails);
            senderServiceImpl.sendEvents();
            return "redirect:/patient/" + id + "/prescription/" + num;
        }
    }
}
