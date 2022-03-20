package com.javaschool.komarov.reha.mapper;

import com.javaschool.komarov.reha.dto.PrescriptionDto;
import com.javaschool.komarov.reha.model.entity.Prescription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PrescriptionMapper {
    @Mapping(target = "prescriptionId", source = "model.id")
    PrescriptionDto toDTO(Prescription model);

    @Mapping(target = "id", source = "dto.prescriptionId")
    Prescription toModel(PrescriptionDto dto);

    Iterable<PrescriptionDto> toDTOList(Iterable<Prescription> models);

}
