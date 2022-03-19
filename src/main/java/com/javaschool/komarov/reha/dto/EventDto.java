package com.javaschool.komarov.reha.dto;

import com.javaschool.komarov.reha.model.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    private Long id;
    private EventStatus eventStatus;
    private String cancellationReason;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTime;
    private PrescriptionItemDto prescriptionItem;
    private Long prescriptionItemId;

}
