package com.javaschool.komarov.reha.service;

import com.javaschool.komarov.reha.dto.PatientDto;
import com.javaschool.komarov.reha.mapper.PatientMapper;
import com.javaschool.komarov.reha.model.PatientStatus;
import com.javaschool.komarov.reha.repository.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientService {
    private final PatientMapper patientMapper;
    private final PatientRepo patientRepo;

    @Autowired
    public PatientService(PatientMapper patientMapper, PatientRepo patientRepo) {
        this.patientMapper = patientMapper;
        this.patientRepo = patientRepo;
    }

    public void savePatient(PatientDto patientDto) {
        patientDto.setStatus(PatientStatus.NOTDEFINED);
        patientRepo.save(patientMapper.toModel(patientDto));
    }

    public Iterable<PatientDto> getAllPatients() {
        return patientMapper.toDTOList(patientRepo.findAll());
    }

    public Optional<PatientDto> getPatientById(Long id) {
        return Optional.of(patientMapper.toDTO(patientRepo.findById(id).get()));
    }

    public void updatePatientStatus(long id, PatientStatus status) {
        patientRepo.updatePatientStatus(id, status);
    }
}
