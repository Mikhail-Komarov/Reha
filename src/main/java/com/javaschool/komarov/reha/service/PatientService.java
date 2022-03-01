package com.javaschool.komarov.reha.service;

import com.javaschool.komarov.reha.model.Patient;
import com.javaschool.komarov.reha.repository.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
    private final PatientRepo patientRepo;

    @Autowired
    public PatientService(PatientRepo patientRepo) {
        this.patientRepo = patientRepo;
    }

    public void savePatient(Patient patient) {
        patientRepo.save(patient);
    }

    public Iterable<Patient> getAllPatients() {
        return patientRepo.findAll();
    }

}
