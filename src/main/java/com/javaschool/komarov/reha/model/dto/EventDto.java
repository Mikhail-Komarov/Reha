package com.javaschool.komarov.reha.model.dto;

import com.javaschool.komarov.reha.model.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    private Long id;
    private EventStatus eventStatus;
    private String cancellationReason;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTime;
    private String patientName;
    private String patientInsurance;
    private String therapy;
    private Integer therapyDose;
    private String doctorName;
    private Long prescriptionItemId;
    private String executorName;

}
