package com.javaschool.komarov.reha.mapper;

import com.javaschool.komarov.reha.dto.TherapyDto;
import com.javaschool.komarov.reha.model.Therapy;
import org.mapstruct.Mapper;

@Mapper
public interface TherapyMapper {
    TherapyDto toDTO(Therapy model);
    Therapy toModel(TherapyDto dto);
}