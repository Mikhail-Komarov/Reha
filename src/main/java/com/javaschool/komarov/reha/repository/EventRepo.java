package com.javaschool.komarov.reha.repository;

import com.javaschool.komarov.reha.model.entity.Event;
import com.javaschool.komarov.reha.model.EventStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Set;

public interface EventRepo extends CrudRepository<Event, Long> {
    Set<Event> findEventByPrescriptionItemId(Long id);

    Iterable<Event> findEventByDateTimeBetween(LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd);

    @Query(value = "SELECT * from event\n" +
            "join prescription_item pi\n" +
            "on pi.id = event.prescription_item_id\n" +
            "join prescription p on pi.prescription_id = p.id\n" +
            "join patient p2 on p.patient_id = p2.id\n" +
            "where p2.health_insurance =?1\n" +
            "and event.date_time between ?2 and ?3", nativeQuery = true)
    Iterable<Event> findEventByDateAndPatient(String healthInsurance, LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd);

    @Query(value = "SELECT * from event\n" +
            "join prescription_item pi\n" +
            "on pi.id = event.prescription_item_id\n" +
            "join prescription p on pi.prescription_id = p.id\n" +
            "join patient p2 on p.patient_id = p2.id\n" +
            "where p2.health_insurance =?", nativeQuery = true)
    Iterable<Event> findEventByPatient(String healthInsurance);

    @Query(value = "SELECT DISTINCT * from event where status = 'SCHEDULED'and prescription_item_id = :id", nativeQuery = true)
    Set<Event> findActiveEventByPrescriptionItemId(@Param(value = "id") Long id);

    @Modifying
    @Query("UPDATE Event e set e.eventStatus = :status where e.id = :id")
    void updateEventStatus(@Param(value = "id") Long id, @Param(value = "status") EventStatus status);

    @Modifying
    @Query("UPDATE Event e set e.cancellationReason = :reason where e.id = :id")
    void addCancellationReason(@Param(value = "id") Long id, @Param(value = "reason") String reason);
}