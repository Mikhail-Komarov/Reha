package com.javaschool.komarov.reha.service;

import com.javaschool.komarov.reha.dto.PatientDto;
import com.javaschool.komarov.reha.mapper.PatientMapper;
import com.javaschool.komarov.reha.model.PatientStatus;
import com.javaschool.komarov.reha.repository.PatientRepo;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PatientService {
    private final PatientMapper patientMapper;
    private final PatientRepo patientRepo;

    public PatientService(PatientMapper patientMapper, PatientRepo patientRepo) {
        this.patientMapper = patientMapper;
        this.patientRepo = patientRepo;
    }

    public boolean checkPatientInDb(String healthInsurance) {
        return patientRepo.getPatientByHealthInsurance(healthInsurance).isPresent();
    }

    public void savePatient(PatientDto patientDto) {
        patientDto.setStatus(PatientStatus.UNDEFINED);
        patientRepo.save(patientMapper.toModel(patientDto));
    }

    public Iterable<PatientDto> getAllPatients() {
        return patientMapper.toDTOList(patientRepo.findAll());
    }

    public PatientDto getPatientById(Long id) {
        if (patientRepo.findById(id).isPresent()) {
            return patientMapper.toDTO(patientRepo.findById(id).get());
        } else {
            return null;
        }
    }

    public void updatePatientStatus(PatientDto patientDto) {
        if (!hasActiveEvent(patientDto.getId())) {
            patientRepo.updatePatientStatus(patientDto.getId(), patientDto.getStatus());
        }
    }

    public void setStatusIsTreated(PatientDto patientDto) {
        if (!patientDto.getStatus().equals(PatientStatus.IS_TREATED)) {
            patientDto.setStatus(PatientStatus.IS_TREATED);
            updatePatientStatus(patientDto);
        }
    }

    public Set<Long> patientIdWithActiveEvent() {
        return patientRepo.findAllPatientIdWithActiveEvent();
    }

    public boolean hasActiveEvent(Long id) {
        return patientIdWithActiveEvent().contains(id);
    }
}
