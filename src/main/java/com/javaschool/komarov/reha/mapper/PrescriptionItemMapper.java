package com.javaschool.komarov.reha.mapper;

import com.javaschool.komarov.reha.dto.PrescriptionItemDto;
import com.javaschool.komarov.reha.model.entity.PrescriptionItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = PrescriptionMapper.class)
public interface PrescriptionItemMapper {
    @Mapping(target = "itemId", source = "model.id")
    PrescriptionItemDto toDTO(PrescriptionItem model);

    @Mapping(target = "id", source = "dto.itemId")
    PrescriptionItem toModel(PrescriptionItemDto dto);

    Iterable<PrescriptionItemDto> toDTOList(Iterable<PrescriptionItem> models);
}