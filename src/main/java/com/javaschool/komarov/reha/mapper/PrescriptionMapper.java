package com.javaschool.komarov.reha.mapper;

import com.javaschool.komarov.reha.dto.PrescriptionDto;
import com.javaschool.komarov.reha.model.Prescription;
import org.mapstruct.Mapper;

@Mapper
public interface PrescriptionMapper {
    PrescriptionDto toDTO(Prescription model);
    Prescription toModel(PrescriptionDto dto);
    Iterable<PrescriptionDto> toDTOList(Iterable<Prescription> models);
}
