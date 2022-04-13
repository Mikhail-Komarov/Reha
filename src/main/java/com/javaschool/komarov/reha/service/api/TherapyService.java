package com.javaschool.komarov.reha.service.api;

import com.javaschool.komarov.reha.model.dto.TherapyDto;
import com.javaschool.komarov.reha.model.entity.Therapy;

import java.util.Optional;

public interface TherapyService {
    /**
     * Method to get all therapiesDTO
     *
     * @return therapiesDTO
     */
    Iterable<TherapyDto> getAllTherapiesDTO();

    /**
     * Method to get therapy from DB
     *
     * @param id therapy id
     * @return therapy
     */
    Optional<Therapy> getTherapyById(Long id);

    /**
     * Method to save therapy in DB
     *
     * @param therapyDto therapyDTO
     */
    void saveTherapy(TherapyDto therapyDto);
}
