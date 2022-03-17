package com.javaschool.komarov.reha.repository;

import com.javaschool.komarov.reha.model.Patient;
import com.javaschool.komarov.reha.model.PatientStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

public interface PatientRepo extends CrudRepository<Patient, Long> {
    @Transactional
    @Modifying
    @Query("update Patient p set p.Status = :status where p.id = :id")
    void updatePatientStatus(@Param(value = "id") long id, @Param(value = "status") PatientStatus status);

    @Query(value = "SELECT DISTINCT patient.id from patient\n" +
            "    join prescription p on patient.id = p.patient_id\n" +
            "    join prescription_item pi on p.id = pi.prescription_id\n" +
            "    join event e on pi.id = e.prescription_item_id\n" +
            "where e.status = 'SCHEDULED'",
            nativeQuery = true)
    Set<Long> findAllPatientIdWithActiveEvent();

    Optional<Patient> getPatientByHealthInsurance(String HealthInsurance);
}

