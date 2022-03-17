package com.javaschool.komarov.reha.mapper;

import com.javaschool.komarov.reha.dto.PrescriptionItemDto;
import com.javaschool.komarov.reha.model.PrescriptionItem;
import org.mapstruct.Mapper;

@Mapper
public interface PrescriptionItemMapper {
    PrescriptionItemDto toDTO(PrescriptionItem model);
    PrescriptionItem toModel(PrescriptionItemDto dto);
    Iterable<PrescriptionItemDto> toDTOList(Iterable<PrescriptionItem> models);
}