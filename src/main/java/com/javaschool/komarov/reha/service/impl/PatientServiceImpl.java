package com.javaschool.komarov.reha.service.impl;

import com.javaschool.komarov.reha.exception.PatientNotFoundException;
import com.javaschool.komarov.reha.mapper.PatientMapper;
import com.javaschool.komarov.reha.model.PatientStatus;
import com.javaschool.komarov.reha.model.dto.PatientDto;
import com.javaschool.komarov.reha.model.entity.Patient;
import com.javaschool.komarov.reha.repository.PatientRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class PatientServiceImpl implements com.javaschool.komarov.reha.service.api.PatientServiceImpl {
    private final PatientMapper patientMapper;
    private final PatientRepo patientRepo;

    public PatientServiceImpl(PatientMapper patientMapper, PatientRepo patientRepo) {
        this.patientMapper = patientMapper;
        this.patientRepo = patientRepo;
    }

    @Override
    public boolean checkPatientInDb(String healthInsurance) {
        return patientRepo.getPatientByHealthInsurance(healthInsurance).isPresent();
    }

    @Override
    public boolean checkPatientInDb(Long id) {
        return patientRepo.findById(id).isPresent();
    }

    @Override
    public void savePatient(PatientDto patientDto) {
        patientDto.setStatus(PatientStatus.UNDEFINED);
        patientRepo.save(patientMapper.toModel(patientDto));
        log.info("Patient was added: " + patientDto.getHealthInsurance() + " insurance number");
    }

    @Override
    public Iterable<PatientDto> getAllPatientsDTO() {
        return patientMapper.toDTOList(patientRepo.findAll());
    }

    @Override
    public PatientDto getPatientDTOById(Long id) {
        Optional<Patient> patient = patientRepo.findById(id);
        return patient.map(patientMapper::toDTO).orElseThrow(PatientNotFoundException::new);
    }

    @Override
    public void updatePatientStatus(PatientDto patientDto) {
        if (!hasActiveEvent(patientDto.getId())) {
            patientRepo.updatePatientStatus(patientDto.getId(), patientDto.getStatus());
            log.info("Patient's status was changed to " + patientDto.getStatus());
        }
    }

    @Override
    public void setStatusIsTreated(Patient patient) {
        if (!patient.getStatus().equals(PatientStatus.IS_TREATED)) {
            patient.setStatus(PatientStatus.IS_TREATED);
            updatePatientStatus(patientMapper.toDTO(patient));
            log.info("Patient's status was changed to " + PatientStatus.IS_TREATED);
        }
    }

    @Override
    public Set<Long> getPatientIdWithActiveEvent() {
        return patientRepo.findAllPatientIdWithActiveEvent();
    }

    @Override
    public boolean hasActiveEvent(Long id) {
        return getPatientIdWithActiveEvent().contains(id);
    }

    @Override
    public Optional<Patient> getPatientById(Long id) {
        return patientRepo.findById(id);
    }
}
