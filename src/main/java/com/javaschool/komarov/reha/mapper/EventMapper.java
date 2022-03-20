package com.javaschool.komarov.reha.mapper;

import com.javaschool.komarov.reha.dto.EventDto;
import com.javaschool.komarov.reha.model.entity.Event;
import org.mapstruct.Mapper;

@Mapper(uses = PrescriptionItemMapper.class)
public interface EventMapper {
    EventDto toDTO(Event model);

    Event toModel(EventDto dto);

    Iterable<EventDto> toDTOList(Iterable<Event> models);

    Iterable<Event> toModelList(Iterable<EventDto> dtos);
}
