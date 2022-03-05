package com.javaschool.komarov.reha.service;

import com.javaschool.komarov.reha.dto.PatientDto;
import com.javaschool.komarov.reha.mapper.PatientMapper;
import com.javaschool.komarov.reha.repository.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        patientRepo.save(patientMapper.toModel(patientDto));
    }

    public Iterable<PatientDto> getAllPatients() {
        return patientMapper.toDTOList(patientRepo.findAll());
    }

}
