package com.javaschool.komarov.reha.mapper;

import com.javaschool.komarov.reha.dto.PatientDto;
import com.javaschool.komarov.reha.model.Patient;
import org.mapstruct.Mapper;

@Mapper
public interface PatientMapper {

    PatientDto toDTO(Patient model);
    Patient toModel(PatientDto dto);
    Iterable<Patient> toModelList(Iterable<PatientDto> dtos);
    Iterable<PatientDto> toDTOList(Iterable<Patient> models);
}
