package com.javaschool.komarov.reha.repository;

import com.javaschool.komarov.reha.model.entity.Therapy;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TherapyRepo extends CrudRepository<Therapy, Long> {
    Optional<Therapy> findByName(String login);
}
