package com.javaschool.komarov.reha.model.dto;

import com.javaschool.komarov.reha.model.PrescriptionItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionItemDto {
    private Long itemId;
    private Integer dose;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTreatment;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTreatment;
    private String timePattern;
    @DateTimeFormat(pattern = "dd.MM.yy")
    private List<LocalDate> date;
    private String dates;
    private String times;
    @DateTimeFormat(pattern = "H:mm")
    private List<LocalTime> time;
    private PrescriptionItemStatus prescriptionItemStatus;
    private String cancellationReason;
    private String doctorName;
    private String therapyName;
    private String diagnosis;
    private long employeeId;
    private long therapyId;
    private long prescriptionId;

}
