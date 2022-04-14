package com.javaschool.komarov.reha.service.impl;

import com.javaschool.komarov.reha.mapper.TherapyMapper;
import com.javaschool.komarov.reha.model.dto.TherapyDto;
import com.javaschool.komarov.reha.model.entity.Therapy;
import com.javaschool.komarov.reha.repository.TherapyRepo;
import com.javaschool.komarov.reha.service.api.TherapyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class TherapyServiceImpl implements TherapyService {
    private final TherapyRepo therapyRepo;
    private final TherapyMapper therapyMapper;

    @Override
    public Iterable<TherapyDto> getAllTherapiesDTO() {
        return therapyMapper.toDTOList(therapyRepo.findAll());
    }

    @Override
    public Optional<Therapy> getTherapyById(Long id) {
        return therapyRepo.findById(id);
    }

    @Override
    @Transactional
    public void saveTherapy(TherapyDto therapyDto) {
        therapyRepo.save(therapyMapper.toModel(therapyDto));
        log.info("Therapy was added: " + therapyDto.getName() + " insurance number");
    }
}
