package com.javaschool.komarov.reha.service.api;

import com.javaschool.komarov.reha.model.dto.TherapyDto;
import com.javaschool.komarov.reha.model.entity.Therapy;

import java.util.Optional;

public interface TherapyService {
    Iterable<TherapyDto> getAllTherapiesDTO();

    Optional<Therapy> getTherapyById(Long id);

    void saveTherapy(TherapyDto therapyDto);
}
