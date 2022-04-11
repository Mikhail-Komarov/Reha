package com.javaschool.komarov.reha.service.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

@Service
public interface SenderService {
    void sendEvents() throws JsonProcessingException;
}
