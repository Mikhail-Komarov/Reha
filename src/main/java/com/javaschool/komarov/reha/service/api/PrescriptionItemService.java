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
    /**
     * Method to get all prescription items by prescription id
     *
     * @param id prescription id
     * @return items
     */
    Iterable<PrescriptionItemDto> getPrescriptionItemDTOByPrescriptionID(Long id);

    /**
     * Method to get prescription itemDTO by id
     *
     * @param id prescription item id
     * @return prescription itemDTO
     */
    PrescriptionItemDto getPrescriptionItemDTOById(Long id);

    /**
     * Method to get prescription item by id
     *
     * @param id prescription item id
     * @return prescription item
     */
    Optional<PrescriptionItem> getPrescriptionItemById(Long id);

    /**
     * Method to save prescription item in DB
     *
     * @param prescriptionItemDto prescription itemDTO
     * @param userDetails         employee info
     */
    void savePrescriptionItem(PrescriptionItemDto prescriptionItemDto, UserDetails userDetails);

    /**
     * Method to update prescription item in DB
     *
     * @param prescriptionItemDto prescription itemDTO
     * @param userDetails         employee info
     */
    void updatePrescriptionItem(PrescriptionItemDto prescriptionItemDto, UserDetails userDetails);

    /**
     * Method to create date and time list for events
     *
     * @param dates dates
     * @param times times
     * @return Iterable<LocalDateTime>
     */
    Iterable<LocalDateTime> createDateTimeListForEvents(List<LocalDate> dates, List<LocalTime> times);


}
