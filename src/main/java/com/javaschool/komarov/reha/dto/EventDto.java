package com.javaschool.komarov.reha.dto;

import com.javaschool.komarov.reha.model.EventStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class EventDto {
    private Long id;
    private EventStatus eventStatus;
    private String cancellationReason;
    private LocalDateTime dateTime;
    private PrescriptionItemDto prescriptionItem;

    public EventDto(Long id, EventStatus eventStatus, String cancellationReason, LocalDateTime dateTime, PrescriptionItemDto prescriptionItem) {
        this.id = id;
        this.eventStatus = eventStatus;
        this.cancellationReason = cancellationReason;
        this.dateTime = dateTime;
        this.prescriptionItem = prescriptionItem;
    }

    public EventDto(EventStatus eventStatus, String cancellationReason, LocalDateTime dateTime, PrescriptionItemDto prescriptionItem) {
        this.eventStatus = eventStatus;
        this.cancellationReason = cancellationReason;
        this.dateTime = dateTime;
        this.prescriptionItem = prescriptionItem;
    }

    public EventDto(EventStatus eventStatus, LocalDateTime dateTime, PrescriptionItemDto prescriptionItem) {
        this.eventStatus = eventStatus;
        this.dateTime = dateTime;
        this.prescriptionItem = prescriptionItem;
    }
}
