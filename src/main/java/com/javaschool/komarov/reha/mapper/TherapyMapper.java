package com.javaschool.komarov.reha.mapper;

import com.javaschool.komarov.reha.model.dto.TherapyDto;
import com.javaschool.komarov.reha.model.entity.Therapy;
import org.mapstruct.Mapper;

@Mapper
public interface TherapyMapper {
    TherapyDto toDTO(Therapy model);

    Therapy toModel(TherapyDto dto);

    Iterable<TherapyDto> toDTOList(Iterable<Therapy> models);
}