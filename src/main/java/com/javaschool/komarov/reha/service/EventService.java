package com.javaschool.komarov.reha.service;

import com.javaschool.komarov.reha.dto.EventDto;
import com.javaschool.komarov.reha.dto.PrescriptionItemDto;
import com.javaschool.komarov.reha.mapper.EventMapper;
import com.javaschool.komarov.reha.model.Event;
import com.javaschool.komarov.reha.model.EventStatus;
import com.javaschool.komarov.reha.repository.EventRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class EventService {

    private final EventMapper eventMapper;
    private final EventRepo eventRepo;

    public EventService(EventMapper eventMapper, EventRepo eventRepo) {
        this.eventMapper = eventMapper;
        this.eventRepo = eventRepo;
    }

    public Iterable<EventDto> getEventsDto(String filterDate, String filterHealthInsurance) {
        Iterable<EventDto> eventsDto = eventMapper.toDTOList(eventRepo.findAll());
        if (filterDate.equals("all")) {
            eventsDto = eventMapper.toDTOList(eventRepo.findAll());
            if (filterHealthInsurance != null && !filterHealthInsurance.isEmpty()) {
                eventsDto = eventMapper.toDTOList(eventRepo.findEventByPatient(filterHealthInsurance));
            }
        }
        if (filterDate.equals("today")) {
            eventsDto = eventMapper.toDTOList(eventRepo.findEventByDateTimeBetween(LocalDate.now().atStartOfDay().minusSeconds(1),
                    LocalDate.now().atStartOfDay().plusDays(1)));
            if (filterHealthInsurance != null && !filterHealthInsurance.isEmpty()) {
                eventsDto = eventMapper.toDTOList(eventRepo.findEventByDateAndPatient(filterHealthInsurance, LocalDate.now().atStartOfDay().minusSeconds(1),
                        LocalDate.now().atStartOfDay().plusDays(1)));
            }
        }
        if (filterDate.equals("hour")) {
            eventsDto = eventMapper.toDTOList(eventRepo.findEventByDateTimeBetween(LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
            if (filterHealthInsurance != null && !filterHealthInsurance.isEmpty()) {
                eventsDto = eventMapper.toDTOList(eventRepo.findEventByDateAndPatient(filterHealthInsurance, LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
            }
        }
        return eventsDto;
    }

    public Iterable<EventDto> getEventDtoByPrescriptionItemId(Long id) {
        return eventMapper.toDTOList(eventRepo.findEventByPrescriptionItemId(id));
    }

    public void createEvent(PrescriptionItemDto prescriptionItemDto, List<LocalDateTime> list) {
        List<EventDto> eventDtos = new ArrayList<>();
        updateEventStatus(prescriptionItemDto.getId(), "Date and time pattern was changed");
        for (LocalDateTime dateTime : list) {
            EventDto eventDto = new EventDto();
            eventDto.setEventStatus(EventStatus.SCHEDULED);
            eventDto.setDateTime(dateTime);
            eventDto.setPrescriptionItem(prescriptionItemDto);
            eventDtos.add(eventDto);
        }
        eventRepo.saveAll(eventMapper.toModelList(eventDtos));
    }


    public EventStatus getEventStatusById(long id) {
        if (eventRepo.findById(id).isPresent()) {
            return eventRepo.findById(id).get().getEventStatus();
        }
        else return null;
    }

    @Transactional
    public void updateEventStatus(EventDto eventDto) {

        if (eventDto.getCancellationReason() != null
                && eventRepo.findById(eventDto.getId()).isPresent()
                && !eventDto.getCancellationReason().isEmpty()
                && eventDto.getEventStatus().equals(EventStatus.CANCELLED)
                && eventRepo.findById(eventDto.getId()).get().getEventStatus().equals(EventStatus.SCHEDULED)) {
            eventRepo.addCancellationReason(eventDto.getId(), eventDto.getCancellationReason());
            eventRepo.updateEventStatus(eventDto.getId(), eventDto.getEventStatus());
        }
        if (eventDto.getCancellationReason().isEmpty()
                && eventDto.getEventStatus().equals(EventStatus.COMPLETED)) {
            eventRepo.updateEventStatus(eventDto.getId(), eventDto.getEventStatus());
        }
    }

    public void updateEventStatus(Long id, String cancellationReason) {
        Set<Event> events = eventRepo.findActiveEventByPrescriptionItemId(id);
        System.out.println(events);
        if (!events.isEmpty()) {
            for (Event event : events) {
                if (event.getEventStatus().equals(EventStatus.SCHEDULED)) {
                    event.setEventStatus(EventStatus.CANCELLED);
                    event.setCancellationReason(cancellationReason);
                }
            }
            eventRepo.saveAll(events);
        }
    }

}
