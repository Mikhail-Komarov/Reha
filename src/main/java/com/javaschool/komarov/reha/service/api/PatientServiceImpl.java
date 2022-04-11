package com.javaschool.komarov.reha.service.api;

import com.javaschool.komarov.reha.model.dto.PatientDto;
import com.javaschool.komarov.reha.model.entity.Patient;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface PatientServiceImpl {
    boolean checkPatientInDb(String healthInsurance);

    boolean checkPatientInDb(Long id);

    void savePatient(PatientDto patientDto);

    Iterable<PatientDto> getAllPatientsDTO();

    PatientDto getPatientDTOById(Long id);

    void updatePatientStatus(PatientDto patientDto);

    void setStatusIsTreated(Patient patient);

    Iterable<Long> getPatientIdWithActiveEvent();

    boolean hasActiveEvent(Long id);

    Optional<Patient> getPatientById(Long id);

}
