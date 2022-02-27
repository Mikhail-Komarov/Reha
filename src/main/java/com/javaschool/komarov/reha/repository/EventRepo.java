package com.javaschool.komarov.reha.repository;

import com.javaschool.komarov.reha.model.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepo extends CrudRepository<Event, Long> {
}