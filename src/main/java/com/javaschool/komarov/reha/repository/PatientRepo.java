package com.javaschool.komarov.reha.repository;

import com.javaschool.komarov.reha.model.Patient;
import org.springframework.data.repository.CrudRepository;

public interface PatientRepo extends CrudRepository<Patient, Long> {
}
