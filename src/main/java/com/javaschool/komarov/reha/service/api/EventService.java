package com.javaschool.komarov.reha.service.api;

import com.javaschool.komarov.reha.model.dto.EmployeeDto;
import com.javaschool.komarov.reha.model.dto.EventDto;
import com.javaschool.komarov.reha.model.EventStatus;
import com.javaschool.komarov.reha.model.entity.PrescriptionItem;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface EventService {
    Iterable<EventDto> getEventsDto(String filterDate, String filterHealthInsurance, String filterStatus);

    Iterable<EventDto> getEventsDTOByToday();

    Iterable<EventDto> getEventDtoByPrescriptionItemId(Long id);

    void createEvent(PrescriptionItem item, List<LocalDateTime> list, EmployeeDto employee);

    EventStatus getEventStatusById(long id);

    void updateEventStatus(EventDto eventDto, UserDetails userDetails);

    void updateEventItem(PrescriptionItem itemOld, PrescriptionItem itemNew);

    void updateEventStatus(Long id, String cancellationReason, EmployeeDto employee);

    LocalDateTime getEventDateTime(Long id);

    boolean eventIsExist(Long id);
}
