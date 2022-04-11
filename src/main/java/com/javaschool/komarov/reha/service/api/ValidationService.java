package com.javaschool.komarov.reha.service.api;

import com.javaschool.komarov.reha.model.dto.EventDto;
import com.javaschool.komarov.reha.model.dto.PatientDto;
import com.javaschool.komarov.reha.model.dto.PrescriptionDto;
import com.javaschool.komarov.reha.model.dto.PrescriptionItemDto;
import com.javaschool.komarov.reha.model.dto.TherapyDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public interface ValidationService {
    void checkPatient(PatientDto patientDto, BindingResult bindingResult);

    void checkPrescription(PrescriptionDto prescriptionDto, BindingResult bindingResult, Long patientId, UserDetails userDetails);

    void checkPatientStatus(PatientDto patientDto, BindingResult bindingResult);

    void checkEventStatus(EventDto eventDto, BindingResult bindingResult);

    void checkPrescriptionItem(PrescriptionItemDto prescriptionItemDto, BindingResult bindingResult);

    void checkPatternUpdate(PrescriptionItemDto prescriptionItemDto, BindingResult bindingResult);

    void checkTherapy(TherapyDto therapyDto, BindingResult bindingResult);

}
