package com.javaschool.komarov.reha.service.impl;

import com.javaschool.komarov.reha.mapper.EmployeeMapper;
import com.javaschool.komarov.reha.mapper.EventMapper;
import com.javaschool.komarov.reha.model.EventStatus;
import com.javaschool.komarov.reha.model.dto.EmployeeDto;
import com.javaschool.komarov.reha.model.dto.EventDto;
import com.javaschool.komarov.reha.model.entity.Event;
import com.javaschool.komarov.reha.model.entity.PrescriptionItem;
import com.javaschool.komarov.reha.repository.EventRepo;
import com.javaschool.komarov.reha.service.api.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventMapper eventMapper;
    private final EventRepo eventRepo;
    private final EmployeeServiceImpl employeeServiceImpl;
    private final EmployeeMapper employeeMapper;

    public EventServiceImpl(EventMapper eventMapper, EventRepo eventRepo, EmployeeServiceImpl employeeServiceImpl, EmployeeMapper employeeMapper) {
        this.eventMapper = eventMapper;
        this.eventRepo = eventRepo;
        this.employeeServiceImpl = employeeServiceImpl;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public Iterable<EventDto> getEventsDto(String filterDate, String filterHealthInsurance, String filterStatus) {
        List<Event> events = new ArrayList<>(eventRepo.findAll());
        if (filterDate.equals("today")) {
            events = events.stream().filter(event -> event.getDateTime().toLocalDate().equals(LocalDate.now())).collect(Collectors.toList());
        }
        if (filterDate.equals("hour")) {
            events =
                    events.stream().filter(event -> event.getDateTime().isAfter(LocalDateTime.now().minusSeconds(1)) && event.getDateTime().isBefore(LocalDateTime.now().plusHours(1).plusSeconds(1))).collect(Collectors.toList());
        }
        if (filterHealthInsurance != null && !filterHealthInsurance.isEmpty()) {
            events =
                    events.stream().filter(event -> event.getPrescriptionItem().getPrescription().getPatient().getHealthInsurance().equals(filterHealthInsurance)).collect(Collectors.toList());
        }
        if (filterStatus != null && !filterStatus.isEmpty() && !filterStatus.equals("ALL")) {
            events = events.stream().filter(event -> event.getEventStatus().toString().equals(filterStatus)).collect(Collectors.toList());
        }
        return eventMapper.toDTOList(events);
    }

    @Override
    public Iterable<EventDto> getEventsDTOByToday() {
        return eventMapper.toDTOList(eventRepo.findEventByToday());
    }

    @Override
    public Iterable<EventDto> getEventDtoByPrescriptionItemId(Long id) {
        return eventMapper.toDTOList(eventRepo.findEventByPrescriptionItemId(id));
    }

    @Override
    public void createEvent(PrescriptionItem item, List<LocalDateTime> list, EmployeeDto employee) {
        List<Event> events = new ArrayList<>();
        updateEventStatus(item.getId(), "Date and time pattern was changed", employee);
        for (LocalDateTime dateTime : list) {
            Event event = new Event();
            event.setEventStatus(EventStatus.SCHEDULED);
            event.setDateTime(dateTime);
            event.setPrescriptionItem(item);
            events.add(event);
        }
        eventRepo.saveAllAndFlush(events);
        log.info("Events were created: " + events.size() + " event(s)");
    }

    @Override
    public EventStatus getEventStatusById(long id) {
        if (eventRepo.findById(id).isPresent()) {
            return eventRepo.findById(id).get().getEventStatus();
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public void updateEventStatus(EventDto eventDto, UserDetails userDetails) {
        EmployeeDto employee = employeeServiceImpl.getEmployeeDTOByLogin(userDetails.getUsername());
        Event event = eventRepo.findById(eventDto.getId()).get();
        if (eventDto.getCancellationReason() != null
                && eventRepo.findById(eventDto.getId()).isPresent()
                && !eventDto.getCancellationReason().isEmpty()
                && eventDto.getEventStatus().equals(EventStatus.CANCELLED)
                && event.getEventStatus().equals(EventStatus.SCHEDULED)) {
            event.setCancellationReason(eventDto.getCancellationReason());
            event.setEmployee(employeeMapper.toModel(employee));
            event.setEventStatus(eventDto.getEventStatus());
        }
        if (eventDto.getCancellationReason().isEmpty()
                && eventDto.getEventStatus().equals(EventStatus.COMPLETED)) {
            event.setEmployee(employeeMapper.toModel(employee));
            event.setEventStatus(eventDto.getEventStatus());
        }
        eventRepo.saveAndFlush(event);
        log.info("Event status was updated: " + event.getId() + " id");
    }

    @Override
    public void updateEventItem(PrescriptionItem itemOld, PrescriptionItem itemNew) {
        Set<Event> events = eventRepo.findActiveEventByPrescriptionItemId(itemOld.getId());
        if (!events.isEmpty()) {
            for (Event event : events) {
                event.setPrescriptionItem(itemNew);
            }
            eventRepo.saveAllAndFlush(events);
            log.info("Events were updated: " + events.size() + " event(s)");
        }
    }

    @Override
    public void updateEventStatus(Long id, String cancellationReason, EmployeeDto employee) {
        Set<Event> events = eventRepo.findActiveEventByPrescriptionItemId(id);
        if(!events.isEmpty()) {
            for (Event event : events) {
                if (event.getEventStatus().equals(EventStatus.SCHEDULED)) {
                    event.setEventStatus(EventStatus.CANCELLED);
                    event.setCancellationReason(cancellationReason);
                    event.setEmployee(employeeMapper.toModel(employee));
                }
            }
            eventRepo.saveAllAndFlush(events);
            log.info("Events status were updated: " + events.size() + " events");
        }
    }

    @Override
    public LocalDateTime getEventDateTime(Long id) {
        if (id != null && eventRepo.findById(id).isPresent()) {
            return eventRepo.findById(id).get().getDateTime();
        } else {
            return null;
        }
    }

    @Override
    public boolean eventIsExist(Long id) {
        return eventRepo.findById(id).isPresent();
    }
}
