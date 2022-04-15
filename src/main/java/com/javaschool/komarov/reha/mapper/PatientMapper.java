package com.javaschool.komarov.reha.mapper;

import com.javaschool.komarov.reha.model.dto.PatientDto;
import com.javaschool.komarov.reha.model.entity.Patient;
import org.mapstruct.Mapper;

@Mapper
public interface PatientMapper {

    PatientDto toDTO(Patient model);

    Patient toModel(PatientDto dto);

    Iterable<PatientDto> toDTOList(Iterable<Patient> models);
}
