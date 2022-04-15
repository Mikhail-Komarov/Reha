package com.javaschool.komarov.reha.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.javaschool.komarov.reha.model.dto.EventDto;
import com.javaschool.komarov.reha.mapper.EventJSONMapper;
import com.javaschool.komarov.reha.service.impl.EventServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/event/today")
public class EventRestController {
    private final EventServiceImpl eventServiceImpl;
    private final EventJSONMapper eventJSONMapper;

    /**
     * Method return events by today
     *
     * @return events
     * @throws JsonProcessingException if converting error is exists
     */
    @GetMapping(value = "", produces = {"application/json;**charset=UTF-8**"})
    public String sendSchedule() throws JsonProcessingException {

        List<EventDto> events = new ArrayList<>();
        eventServiceImpl.getEventsDTOByToday().forEach(events::add);
        return eventJSONMapper.convertToJson(events);
    }
}
