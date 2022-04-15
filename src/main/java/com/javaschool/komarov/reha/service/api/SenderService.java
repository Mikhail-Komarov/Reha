package com.javaschool.komarov.reha.service.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

@Service
public interface SenderService {
    /**
     * Method to send events
     *
     * @throws JsonProcessingException if converting error is exists
     */
    void sendEvents() throws JsonProcessingException;
}
