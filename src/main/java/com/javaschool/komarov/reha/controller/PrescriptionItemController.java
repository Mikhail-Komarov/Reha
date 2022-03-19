package com.javaschool.komarov.reha.controller;

import com.javaschool.komarov.reha.dto.EmployeeDto;
import com.javaschool.komarov.reha.dto.PatientDto;
import com.javaschool.komarov.reha.dto.PrescriptionDto;
import com.javaschool.komarov.reha.dto.PrescriptionItemDto;
import com.javaschool.komarov.reha.dto.TherapyDto;
import com.javaschool.komarov.reha.service.EmployeeService;
import com.javaschool.komarov.reha.service.PatientService;
import com.javaschool.komarov.reha.service.PrescriptionItemService;
import com.javaschool.komarov.reha.service.PrescriptionService;
import com.javaschool.komarov.reha.service.TherapyService;
import com.javaschool.komarov.reha.service.ValidationService;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/patient/{id}/prescription/{num}")
public class PrescriptionItemController {
    private final PatientService patientService;
    private final PrescriptionService prescriptionService;
    private final PrescriptionItemService prescriptionItemService;
    private final EmployeeService employeeService;
    private final TherapyService therapyService;
    private final ValidationService validationService;

    public PrescriptionItemController(PatientService patientService, PrescriptionService prescriptionService, PrescriptionItemService prescriptionItemService,
                                      EmployeeService employeeService, TherapyService therapyService, ValidationService validationService) {
        this.patientService = patientService;
        this.prescriptionService = prescriptionService;
        this.prescriptionItemService = prescriptionItemService;
        this.employeeService = employeeService;
        this.therapyService = therapyService;
        this.validationService = validationService;
    }

    @ModelAttribute("patientInfo")
    public PatientDto patientInfo(@PathVariable("id") long id) {
        return patientService.getPatientById(id);
    }

    @ModelAttribute("prescriptionInfo")
    public PrescriptionDto prescriptionInfo(@PathVariable("num") long num) {
        return prescriptionService.getPrescriptionById(num);
    }

    @ModelAttribute("items")
    public Iterable<PrescriptionItemDto> items(@PathVariable("num") long num) {
        return prescriptionItemService.getPrescriptionItemByPrescriptionID(num);
    }

    @ModelAttribute("doctor")
    public EmployeeDto doctor(@AuthenticationPrincipal UserDetails userDetails) {
        return employeeService.getEmployeeByLogin(userDetails.getUsername());
    }

    @ModelAttribute("newItem")
    public PrescriptionItemDto newItem() {
        return new PrescriptionItemDto();
    }

    @ModelAttribute("updateItem")
    public PrescriptionItemDto updateItem() {
        return new PrescriptionItemDto();
    }

    @ModelAttribute("therapies")
    public Iterable<TherapyDto> therapies() {
        return therapyService.getAllTherapies();
    }

    @ModelAttribute("dateList")
    public List<LocalDate> dateList() {
        return prescriptionItemService.getDateList();
    }

    @ModelAttribute("timeList")
    public List<LocalTime> timeList() {
        return prescriptionItemService.getTimeList();
    }

    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('employee:read')")
    public String prescription() {
        return "item";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('employee:read')")
    public String addPrescription(@PathVariable("id") long id, @PathVariable("num") long num,
                                  @ModelAttribute("newItem") PrescriptionItemDto prescriptionItemDto,
                                  BindingResult bindingResult, Model model) {
        validationService.checkPrescriptionItem(prescriptionItemDto, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", "Adding error!");
            return "item";
        } else {
            prescriptionItemService.savePrescriptionItem(prescriptionItemDto);
            return "redirect:/patient/" + id + "/prescription/" + num;
        }
    }

    @PostMapping("/upd")
    @PreAuthorize("hasAnyAuthority('employee:read')")
    public String updPrescription(@PathVariable("id") long id, @PathVariable("num") long num,
                                  @ModelAttribute("updateItem") PrescriptionItemDto prescriptionItemDto,
                                  BindingResult bindingResult, Model model) {
        validationService.checkPatternUpdate(prescriptionItemDto, bindingResult);
        if (!bindingResult.hasErrors()) {
            validationService.checkPrescriptionItem(prescriptionItemDto, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", "Error in item id:" + prescriptionItemDto.getItemId());
            return "item";
        } else {
            prescriptionItemService.updatePrescriptionItem(prescriptionItemDto);
            return "redirect:/patient/" + id + "/prescription/" + num;
        }
    }
}
