package com.javaschool.komarov.reha.service.impl;

import com.javaschool.komarov.reha.mapper.EventJSONMapper;
import com.javaschool.komarov.reha.model.dto.EventDto;
import com.javaschool.komarov.reha.service.api.SenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
@PropertySource(value = "classpath:application.properties")
public class SenderServiceImpl implements SenderService {
    private final JmsTemplate jmsTemplate;
    private final EventJSONMapper eventJSONMapper;
    private final EventServiceImpl eventServiceImpl;
    @Value("${activemq.destination}")
    private String destination;

    @Override
    public void sendEvents() {
        List<EventDto> eventDtoToSend = new ArrayList<>();
        eventServiceImpl.getEventsDTOByToday().forEach(eventDtoToSend::add);
        if (!eventDtoToSend.isEmpty()) {
            try {
                jmsTemplate.convertAndSend(destination, eventJSONMapper.convertToJson(eventDtoToSend));
            } catch (Exception e) {
                log.error("Error sending message to Event client" + e);
            }
        }
    }

    @PostConstruct
    private void initMessage() {
        sendEvents();
    }

}