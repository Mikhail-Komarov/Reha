package com.javaschool.komarov.reha.mapper;

import com.javaschool.komarov.reha.dto.EventDto;
import com.javaschool.komarov.reha.model.Event;
import org.mapstruct.Mapper;

@Mapper
public interface EventMapper {
    EventDto toDTO(Event model);
    Event toModel(EventDto dto);
}
