package com.javaschool.komarov.reha.mapper;

import com.javaschool.komarov.reha.dto.EventDto;
import com.javaschool.komarov.reha.model.Event;
import java.util.ArrayList;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-03-19T18:26:17+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_312 (BellSoft)"
)
@Component
public class EventMapperImpl implements EventMapper {

    @Autowired
    private PrescriptionItemMapper prescriptionItemMapper;

    @Override
    public EventDto toDTO(Event model) {
        if ( model == null ) {
            return null;
        }

        EventDto eventDto = new EventDto();

        eventDto.setId( model.getId() );
        eventDto.setEventStatus( model.getEventStatus() );
        eventDto.setCancellationReason( model.getCancellationReason() );
        eventDto.setDateTime( model.getDateTime() );
        eventDto.setPrescriptionItem( prescriptionItemMapper.toDTO( model.getPrescriptionItem() ) );

        return eventDto;
    }

    @Override
    public Event toModel(EventDto dto) {
        if ( dto == null ) {
            return null;
        }

        Event event = new Event();

        event.setId( dto.getId() );
        event.setEventStatus( dto.getEventStatus() );
        event.setCancellationReason( dto.getCancellationReason() );
        event.setDateTime( dto.getDateTime() );
        event.setPrescriptionItem( prescriptionItemMapper.toModel( dto.getPrescriptionItem() ) );

        return event;
    }

    @Override
    public Iterable<EventDto> toDTOList(Iterable<Event> models) {
        if ( models == null ) {
            return null;
        }

        ArrayList<EventDto> iterable = new ArrayList<EventDto>();
        for ( Event event : models ) {
            iterable.add( toDTO( event ) );
        }

        return iterable;
    }

    @Override
    public Iterable<Event> toModelList(Iterable<EventDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        ArrayList<Event> iterable = new ArrayList<Event>();
        for ( EventDto eventDto : dtos ) {
            iterable.add( toModel( eventDto ) );
        }

        return iterable;
    }
}
