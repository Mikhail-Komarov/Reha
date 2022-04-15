package com.javaschool.komarov.reha.service;

import com.javaschool.komarov.reha.model.EventStatus;
import com.javaschool.komarov.reha.model.dto.EmployeeDto;
import com.javaschool.komarov.reha.model.dto.EventDto;
import com.javaschool.komarov.reha.model.entity.Employee;
import com.javaschool.komarov.reha.model.entity.Event;
import com.javaschool.komarov.reha.model.entity.PrescriptionItem;
import com.javaschool.komarov.reha.repository.EventRepo;
import com.javaschool.komarov.reha.service.impl.EmployeeServiceImpl;
import com.javaschool.komarov.reha.service.impl.EventServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class EventServiceTest {
    @Autowired
    private EventServiceImpl eventService;
    @MockBean
    private EventRepo eventRepo;
    @MockBean
    private UserDetails userDetails;
    @MockBean
    EmployeeServiceImpl employeeService;

    @Test
    public void getEventsDto() {
        eventService.getEventsDto("", "", "");
        Mockito.verify(eventRepo, Mockito.times(1)).findAll();
    }

    @Test
    public void getEventsDTOByToday() {
        eventService.getEventsDTOByToday();
        Mockito.verify(eventRepo, Mockito.times(1)).findEventByToday();
    }

    @Test
    public void getEventDtoByPrescriptionItemId() {
        eventService.getEventDtoByPrescriptionItemId(1L);
        Mockito.verify(eventRepo, Mockito.times(1)).findEventByPrescriptionItemId(1L);
    }

    @Test
    public void createEvent() {
        PrescriptionItem prescriptionItem = new PrescriptionItem();
        prescriptionItem.setId(1L);
        List<LocalDateTime> dateTimeList = new ArrayList<>();
        EmployeeDto employee = new EmployeeDto();
        dateTimeList.add(LocalDateTime.now());
        Set<Event> oldEvents = new HashSet<>();
        Mockito.when(eventRepo.findActiveEventByPrescriptionItemId(1L)).thenReturn(oldEvents);
        eventService.createEvent(prescriptionItem, dateTimeList, employee);
        Mockito.verify(eventRepo, Mockito.times(1)).saveAllAndFlush(Mockito.any());
    }

    @Test
    public void getEventStatusById() {
        eventService.getEventStatusById(1L);
        Mockito.verify(eventRepo, Mockito.times(1)).findById(1L);
    }

    @Test
    public void updateEventStatusComplete() {
        EventDto eventDto = new EventDto();
        eventDto.setId(1L);
        eventDto.setEventStatus(EventStatus.COMPLETED);
        eventDto.setCancellationReason("");
        Event event = new Event();
        Employee employee = new Employee();
        Mockito.when(eventRepo.findById(eventDto.getId())).thenReturn(Optional.of(event));
        Mockito.when(userDetails.getUsername()).thenReturn("test");
        Mockito.when(employeeService.getEmployeeByLogin("test")).thenReturn(Optional.of(employee));
        eventService.updateEventStatus(eventDto, userDetails);
        Assert.assertEquals(event.getEventStatus(), eventDto.getEventStatus());
        Mockito.verify(eventRepo, Mockito.times(1)).saveAndFlush(event);
    }

    @Test
    public void updateEventStatusCancelled() {
        EventDto eventDto = new EventDto();
        eventDto.setId(1L);
        eventDto.setEventStatus(EventStatus.CANCELLED);
        eventDto.setCancellationReason("test");
        Event event = new Event();
        event.setEventStatus(EventStatus.SCHEDULED);
        Employee employee = new Employee();
        Mockito.when(eventRepo.findById(eventDto.getId())).thenReturn(Optional.of(event));
        Mockito.when(userDetails.getUsername()).thenReturn("test");
        Mockito.when(employeeService.getEmployeeByLogin("test")).thenReturn(Optional.of(employee));
        eventService.updateEventStatus(eventDto, userDetails);
        Assert.assertEquals(event.getEventStatus(), eventDto.getEventStatus());
        Mockito.verify(eventRepo, Mockito.times(1)).saveAndFlush(event);
    }

    @Test
    public void updateEventItemChangeItemEmptyActiveEvents() {
        PrescriptionItem oldItem = new PrescriptionItem();
        oldItem.setId(1L);
        PrescriptionItem newItem = new PrescriptionItem();
        Set<Event> events = new HashSet<>();
        Mockito.when(eventRepo.findActiveEventByPrescriptionItemId(oldItem.getId())).thenReturn(events);
        eventService.updateEventItem(oldItem, newItem);
        Mockito.verify(eventRepo, Mockito.times(0)).saveAllAndFlush(events);
    }

    @Test
    public void updateEventItemChangeItemActiveEvents() {
        PrescriptionItem oldItem = new PrescriptionItem();
        oldItem.setId(1L);
        PrescriptionItem newItem = new PrescriptionItem();
        Set<Event> events = new HashSet<>();
        events.add(new Event());
        Mockito.when(eventRepo.findActiveEventByPrescriptionItemId(oldItem.getId())).thenReturn(events);
        eventService.updateEventItem(oldItem, newItem);
        Mockito.verify(eventRepo, Mockito.times(1)).saveAllAndFlush(events);
    }

    @Test
    public void updateEventStatusForCancelledItemEmptyEvents() {
        EmployeeDto employee = new EmployeeDto();
        Set<Event> events = new HashSet<>();
        Mockito.when(eventRepo.findActiveEventByPrescriptionItemId(1L)).thenReturn(events);
        eventService.updateEventStatus(1L, "test", employee);
        Mockito.verify(eventRepo, Mockito.times(0)).saveAllAndFlush(events);
    }

    @Test
    public void updateEventStatusForCancelledItem() {
        EmployeeDto employee = new EmployeeDto();
        Set<Event> events = new HashSet<>();
        Event event = new Event();
        event.setEventStatus(EventStatus.SCHEDULED);
        events.add(event);
        Mockito.when(eventRepo.findActiveEventByPrescriptionItemId(1L)).thenReturn(events);
        eventService.updateEventStatus(1L, "test", employee);
        Mockito.verify(eventRepo, Mockito.times(1)).saveAllAndFlush(events);
    }

    @Test
    public void getEventDateTime() {
        Mockito.when(eventRepo.findById(1L)).thenReturn(Optional.empty());
        Assert.assertNull(eventService.getEventDateTime(1L));
        Mockito.verify(eventRepo, Mockito.times(1)).findById(1L);
    }

    @Test
    public void eventIsExistEmptyOptional() {
        Mockito.when(eventRepo.findById(1L)).thenReturn(Optional.empty());
        Assert.assertFalse(eventService.eventIsExist(1L));
        Mockito.verify(eventRepo, Mockito.times(1)).findById(1L);
    }
}