package com.javaschool.komarov.reha.repository;

import com.javaschool.komarov.reha.model.Prescription;
import org.springframework.data.repository.CrudRepository;

public interface PrescriptionRepo extends CrudRepository<Prescription, Long> {
    Iterable<Prescription> findByPatientId(Long id);
}
