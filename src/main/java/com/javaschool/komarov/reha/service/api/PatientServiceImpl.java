package com.javaschool.komarov.reha.service.api;

import com.javaschool.komarov.reha.model.dto.PatientDto;
import com.javaschool.komarov.reha.model.entity.Patient;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface PatientServiceImpl {
    /**
     * Method to check patient in DB
     *
     * @param healthInsurance health insurance number
     * @return boolean
     */
    boolean checkPatientInDb(String healthInsurance);

    /**
     * Method to check patient in DB
     *
     * @param id patient id
     * @return boolean
     */
    boolean checkPatientInDb(Long id);

    /**
     * Method to save patient in DB
     *
     * @param patientDto patientDTO
     */
    void savePatient(PatientDto patientDto);

    /**
     * Method to get all patients from DB
     *
     * @return patients
     */
    Iterable<PatientDto> getAllPatientsDTO();

    /**
     * Method to get patientDTO by id
     *
     * @param id patient id
     * @return patientDTO
     */
    PatientDto getPatientDTOById(Long id);

    /**
     * Method to update patient status
     *
     * @param patientDto patientDTO
     */
    void updatePatientStatus(PatientDto patientDto);

    /**
     * Method to update patient status
     *
     * @param patient patient
     */
    void setStatusIsTreated(Patient patient);

    /**
     * Method to get all patients with active event
     *
     * @return patients
     */
    Iterable<Long> getPatientIdWithActiveEvent();

    /**
     * Method to check is exist active events for patient
     *
     * @param id patient id
     * @return boolean
     */
    boolean hasActiveEvent(Long id);

    /**
     * Method to get patient by id
     *
     * @param id patient id
     * @return patient
     */
    Optional<Patient> getPatientById(Long id);

}
