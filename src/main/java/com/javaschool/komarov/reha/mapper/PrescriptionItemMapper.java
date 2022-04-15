package com.javaschool.komarov.reha.mapper;

import com.javaschool.komarov.reha.model.dto.PrescriptionItemDto;
import com.javaschool.komarov.reha.model.entity.PrescriptionItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = PrescriptionMapper.class)
public interface PrescriptionItemMapper {
    @Mapping(target = "itemId", source = "model.id")
    @Mapping(target = "doctorName", expression = "java(getDoctorFullName(model))")
    @Mapping(target = "therapyName", source = "model.therapy.name")
    @Mapping(target = "diagnosis", source = "model.prescription.diagnosis")
    PrescriptionItemDto toDTO(PrescriptionItem model);

    @Mapping(target = "id", source = "dto.itemId")
    PrescriptionItem toModel(PrescriptionItemDto dto);

    Iterable<PrescriptionItemDto> toDTOList(Iterable<PrescriptionItem> models);

    default String getDoctorFullName(PrescriptionItem model) {
        return model.getEmployee().getFirstName()
                + " " + model.getEmployee().getLastName();
    }
}