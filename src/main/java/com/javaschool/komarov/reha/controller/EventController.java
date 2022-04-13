package com.javaschool.komarov.reha.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.javaschool.komarov.reha.model.dto.EventDto;
import com.javaschool.komarov.reha.service.impl.EventServiceImpl;
import com.javaschool.komarov.reha.service.impl.SenderServiceImpl;
import com.javaschool.komarov.reha.service.impl.ValidationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class EventController {
    private final EventServiceImpl eventServiceImpl;
    private final ValidationServiceImpl validationServiceImpl;
    private final SenderServiceImpl senderServiceImpl;

    /**
     * Method return model with events
     *
     * @param filterDate            date filter
     * @param filterHealthInsurance insurance number filter
     * @param filterStatus          event status filter
     * @return model
     */
    @ModelAttribute("events")
    public Iterable<EventDto> prescriptions(@RequestParam(required = false, defaultValue = "") String filterDate,
                                            @RequestParam(required = false, defaultValue = "") String filterHealthInsurance,
                                            @RequestParam(required = false, defaultValue = "") String filterStatus) {
        return eventServiceImpl.getEventsDto(filterDate, filterHealthInsurance, filterStatus);
    }

    /**
     * Method return page with events
     *
     * @param model model with events
     * @return html
     */
    @GetMapping("/event")
    public String getEvents(Model model) {
        model.addAttribute("newEvent", new EventDto());
        return "event";
    }

    /**
     * Method to update event
     *
     * @param eventDto      event
     * @param bindingResult validation result
     * @param userDetails   employee info
     * @param model         model with error message
     * @return html
     */
    @PostMapping("/event/update")
    @PreAuthorize("hasAnyAuthority('employee:read')")
    public String updatePatientStatus(@ModelAttribute("newEvent") EventDto eventDto,
                                      BindingResult bindingResult,
                                      @AuthenticationPrincipal UserDetails userDetails,
                                      Model model) throws JsonProcessingException {

        validationServiceImpl.checkEventStatus(eventDto, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", "Error in event id:" + eventDto.getId());
            return "event";
        } else {
            eventServiceImpl.updateEventStatus(eventDto, userDetails);
            senderServiceImpl.sendEvents();
            return "redirect:/event";
        }
    }
}
