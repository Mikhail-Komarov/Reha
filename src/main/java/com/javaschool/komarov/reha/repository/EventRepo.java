package com.javaschool.komarov.reha.repository;

import com.javaschool.komarov.reha.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface EventRepo extends JpaRepository<Event, Long> {
    Set<Event> findEventByPrescriptionItemId(Long id);

    @Query(value = "SELECT DISTINCT * from event where status = 'SCHEDULED'and prescription_item_id = :id", nativeQuery = true)
    Set<Event> findActiveEventByPrescriptionItemId(@Param(value = "id") Long id);

    @Query(value = "SELECT * from event where  DATE(date_time) = current_date", nativeQuery = true)
    Set<Event> findEventByToday();

}