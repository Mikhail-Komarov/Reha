package com.javaschool.komarov.reha.controller;

import com.javaschool.komarov.reha.dto.EventDto;
import com.javaschool.komarov.reha.service.EventService;
import com.javaschool.komarov.reha.service.ValidationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EventController {
    private final EventService eventService;
    private final ValidationService validationService;

    public EventController(EventService eventService, ValidationService validationService) {
        this.eventService = eventService;
        this.validationService = validationService;
    }

    @ModelAttribute("events")
    public Iterable<EventDto> prescriptions(@RequestParam(required = false, defaultValue = "") String filterDate,
                                            @RequestParam(required = false, defaultValue = "") String filterHealthInsurance) {
        return eventService.getEventsDto(filterDate, filterHealthInsurance);
    }

    @GetMapping("/event")
    public String getEvents(Model model) {
        model.addAttribute("newEvent", new EventDto());
        return "event";
    }

    @PostMapping("/event/update")
    @PreAuthorize("hasAnyAuthority('employee:read')")
    public String updatePatientStatus(@ModelAttribute("newEvent") EventDto eventDto, BindingResult bindingResult, Model model) {

        validationService.checkEventStatus(eventDto, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", "Error in event id:" + eventDto.getId());
            return "event";
        } else {
            eventService.updateEventStatus(eventDto);
            return "redirect:/event";
        }
    }
}
