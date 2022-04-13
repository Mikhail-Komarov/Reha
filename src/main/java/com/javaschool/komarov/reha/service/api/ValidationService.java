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
    /**
     * Method to check new patient when adding
     *
     * @param patientDto    patientDTO
     * @param bindingResult validation result
     */
    void checkPatient(PatientDto patientDto, BindingResult bindingResult);

    /**
     * Method to check new prescription when adding
     *
     * @param prescriptionDto prescriptionDTO
     * @param bindingResult   validation result
     * @param patientId       patient id
     * @param userDetails     employee info
     */
    void checkPrescription(PrescriptionDto prescriptionDto, BindingResult bindingResult, Long patientId, UserDetails userDetails);

    /**
     * Method to check patient status when update
     *
     * @param patientDto    patientDTO
     * @param bindingResult validation result
     */
    void checkPatientStatus(PatientDto patientDto, BindingResult bindingResult);

    /**
     * Method to check event status when update
     *
     * @param eventDto      eventDTO
     * @param bindingResult validation result
     */
    void checkEventStatus(EventDto eventDto, BindingResult bindingResult);

    /**
     * Method to check prescription item when adding
     *
     * @param prescriptionItemDto prescription itemDTO
     * @param bindingResult       validation result
     */
    void checkPrescriptionItem(PrescriptionItemDto prescriptionItemDto, BindingResult bindingResult);

    /**
     * Method to check new date and time pattern
     *
     * @param prescriptionItemDto prescription itemDTO
     * @param bindingResult       validation result
     */
    void checkPatternUpdate(PrescriptionItemDto prescriptionItemDto, BindingResult bindingResult);

    /**
     * Method to check therapy when adding
     *
     * @param therapyDto    therapyDTO
     * @param bindingResult validation result
     */
    void checkTherapy(TherapyDto therapyDto, BindingResult bindingResult);

}
