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
    /**
     * Method to get events according to filters
     *
     * @param filterDate            date filter
     * @param filterHealthInsurance insurance number filter
     * @param filterStatus          event status filter
     * @return events
     */
    Iterable<EventDto> getEventsDto(String filterDate, String filterHealthInsurance, String filterStatus);

    /**
     * Method to get events by today
     *
     * @return events
     */
    Iterable<EventDto> getEventsDTOByToday();

    /**
     * Method to get events by prescription item id
     *
     * @param id prescription item id
     * @return events
     */
    Iterable<EventDto> getEventDtoByPrescriptionItemId(Long id);

    /**
     * Method to create event and save it in DB
     *
     * @param item     prescription item
     * @param list     list with dates
     * @param employee employee
     */
    void createEvent(PrescriptionItem item, List<LocalDateTime> list, EmployeeDto employee);

    /**
     * Method to get event status
     *
     * @param id event id
     * @return event status
     */
    EventStatus getEventStatusById(long id);

    /**
     * Method to update event status
     *
     * @param eventDto    eventDTO
     * @param userDetails employee info
     */
    void updateEventStatus(EventDto eventDto, UserDetails userDetails);

    /**
     * Method to update event
     *
     * @param itemOld old prescription item
     * @param itemNew new prescription item
     */
    void updateEventItem(PrescriptionItem itemOld, PrescriptionItem itemNew);

    /**
     * Method to update event status
     *
     * @param id                 prescription item id
     * @param cancellationReason cancellation reason
     * @param employee           employee
     */
    void updateEventStatus(Long id, String cancellationReason, EmployeeDto employee);

    /**
     * Method to get event date and time
     *
     * @param id event id
     * @return LocalDateTime
     */
    LocalDateTime getEventDateTime(Long id);

    /**
     * Method to check event in DB
     *
     * @param id event id
     * @return boolean
     */
    boolean eventIsExist(Long id);
}
