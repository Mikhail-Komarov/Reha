package com.javaschool.komarov.reha.repository;

import com.javaschool.komarov.reha.model.Patient;
import com.javaschool.komarov.reha.model.PatientStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PatientRepo extends CrudRepository<Patient, Long> {
    @Transactional
    @Modifying
    @Query("update Patient p set p.Status = :status where p.id = :id")
    void updatePatientStatus(@Param(value = "id") long id, @Param(value = "status") PatientStatus status);
}

