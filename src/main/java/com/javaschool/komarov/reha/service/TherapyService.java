package com.javaschool.komarov.reha.service;

import com.javaschool.komarov.reha.dto.TherapyDto;
import com.javaschool.komarov.reha.mapper.TherapyMapper;
import com.javaschool.komarov.reha.repository.TherapyRepo;
import org.springframework.stereotype.Service;

@Service
public class TherapyService {
    private final TherapyRepo therapyRepo;
    private final TherapyMapper therapyMapper;

    public TherapyService(TherapyRepo therapyRepo, TherapyMapper therapyMapper) {
        this.therapyRepo = therapyRepo;
        this.therapyMapper = therapyMapper;
    }

    public Iterable<TherapyDto> getAllTherapies() {
        return therapyMapper.toDTOList(therapyRepo.findAll());
    }

    public TherapyDto getTherapyDtoById(Long id) {
        return therapyMapper.toDTO(therapyRepo.findById(id).get());
    }
}
