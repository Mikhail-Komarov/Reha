package com.javaschool.komarov.reha.service.api;

import com.javaschool.komarov.reha.model.dto.PrescriptionItemDto;
import com.javaschool.komarov.reha.model.entity.PrescriptionItem;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public interface PrescriptionItemService {
    Iterable<PrescriptionItemDto> getPrescriptionItemDTOByPrescriptionID(Long id);

    /*Iterable<LocalDate> getDateList();

    Iterable<LocalTime> getTimeList();*/

    PrescriptionItemDto getPrescriptionItemDTOById(Long id);

    Optional<PrescriptionItem> getPrescriptionItemById(Long id);

    void savePrescriptionItem(PrescriptionItemDto prescriptionItemDto, UserDetails userDetails);

    void updatePrescriptionItem(PrescriptionItemDto prescriptionItemDto, UserDetails userDetails);

    Iterable<LocalDateTime> createDateTimeListForEvents(List<LocalDate> dates, List<LocalTime> times);


}
