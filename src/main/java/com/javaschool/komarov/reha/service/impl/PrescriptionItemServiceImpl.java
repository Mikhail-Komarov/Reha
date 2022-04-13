package com.javaschool.komarov.reha.service.impl;

import com.javaschool.komarov.reha.exception.PrescriptionItemNotFoundException;
import com.javaschool.komarov.reha.mapper.EmployeeMapper;
import com.javaschool.komarov.reha.mapper.PrescriptionItemMapper;
import com.javaschool.komarov.reha.model.PrescriptionItemStatus;
import com.javaschool.komarov.reha.model.dto.EmployeeDto;
import com.javaschool.komarov.reha.model.dto.PrescriptionItemDto;
import com.javaschool.komarov.reha.model.entity.PrescriptionItem;
import com.javaschool.komarov.reha.repository.PrescriptionItemRepo;
import com.javaschool.komarov.reha.service.api.PrescriptionItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class PrescriptionItemServiceImpl implements PrescriptionItemService {
    private final PrescriptionItemMapper prescriptionItemMapper;
    private final PrescriptionItemRepo prescriptionItemRepo;
    private final EmployeeMapper employeeMapper;

    private final EmployeeServiceImpl employeeServiceImpl;
    private final TherapyServiceImpl therapyServiceImpl;
    private final PrescriptionServiceImpl prescriptionServiceImpl;
    private final EventServiceImpl eventServiceImpl;
    private final PatientServiceImpl patientServiceImpl;

    @Override
    public Iterable<PrescriptionItemDto> getPrescriptionItemDTOByPrescriptionID(Long id) {
        return prescriptionItemMapper.
                toDTOList(prescriptionItemRepo.findPrescriptionItemByPrescriptionId(id));
    }

    @Override
    public PrescriptionItemDto getPrescriptionItemDTOById(Long id) {
        Optional<PrescriptionItem> item = prescriptionItemRepo.findById(id);
        return item.map(prescriptionItemMapper::toDTO).orElseThrow(PrescriptionItemNotFoundException::new);
    }

    public Optional<PrescriptionItem> getPrescriptionItemById(Long id) {
        return prescriptionItemRepo.findById(id);
    }

    @Override
    @Transactional
    public void savePrescriptionItem(PrescriptionItemDto prescriptionItemDto, UserDetails userDetails) {
        EmployeeDto employeeDTO = employeeServiceImpl.getEmployeeDTOByLogin(userDetails.getUsername());
        if (prescriptionItemDto.getDates() != null && prescriptionItemDto.getTimes() != null) {
            prescriptionItemDto.setDate(getDates(prescriptionItemDto));
            prescriptionItemDto.setTime(getTimes(prescriptionItemDto));
        }
        PrescriptionItem prescriptionItem = prescriptionItemMapper.toModel(prescriptionItemDto);
        employeeServiceImpl.getEmployeeById(prescriptionItemDto.getEmployeeId()).ifPresent(prescriptionItem::setEmployee);
        therapyServiceImpl.getTherapyById(prescriptionItemDto.getTherapyId()).ifPresent(prescriptionItem::setTherapy);
        prescriptionServiceImpl.getPrescriptionById(prescriptionItemDto.getPrescriptionId()).ifPresent(prescriptionItem::setPrescription);
        prescriptionItem.setPrescriptionItemStatus(PrescriptionItemStatus.PRESCRIBED);
        prescriptionItem.setTimePattern(createDateAndTimePattern(prescriptionItemDto.getDate(), prescriptionItemDto.getTime()));
        PrescriptionItem newItemDto = prescriptionItemRepo.saveAndFlush(prescriptionItem);
        patientServiceImpl.setStatusIsTreated(prescriptionItem.getPrescription().getPatient());
        eventServiceImpl.createEvent(newItemDto, createDateTimeListForEvents(prescriptionItemDto.getDate(), prescriptionItemDto.getTime()), employeeDTO);
        log.info("Dr. " + employeeDTO.getLastName() + " added item to patient id " + prescriptionItem.getPrescription().getPatient().getId());
    }

    @Override
    @Transactional
    public void updatePrescriptionItem(PrescriptionItemDto prescriptionItemDto, UserDetails userDetails) {
        EmployeeDto employee = employeeServiceImpl.getEmployeeDTOByLogin(userDetails.getUsername());
        Optional<PrescriptionItem> item = prescriptionItemRepo.findById(prescriptionItemDto.getItemId());
        item.ifPresent(prescriptionItem -> {
            if (prescriptionItemDto.getPrescriptionItemStatus() != null) {
                prescriptionItem.setPrescriptionItemStatus(prescriptionItemDto.getPrescriptionItemStatus());
            }
            if (prescriptionItemDto.getCancellationReason() != null && !prescriptionItemDto.getCancellationReason().isEmpty()) {
                prescriptionItem.setCancellationReason(prescriptionItemDto.getCancellationReason());
                eventServiceImpl.updateEventStatus(prescriptionItemDto.getItemId(), prescriptionItemDto.getCancellationReason(), employee);
            }
            PrescriptionItem duplicateItem = null;
            if (prescriptionItemDto.getDose() != null) {
                duplicateItem = createDuplicateItem(prescriptionItem);
                duplicateItem.setDose(prescriptionItemDto.getDose());
                duplicateItem.setEmployee(employeeMapper.toModel(employee));
                eventServiceImpl.updateEventItem(prescriptionItem, duplicateItem);
            }
            if (prescriptionItemDto.getDates() != null && prescriptionItemDto.getTimes() != null
                    && !prescriptionItemDto.getDates().isEmpty() && !prescriptionItemDto.getTimes().isEmpty()) {
                prescriptionItemDto.setDate(getDates(prescriptionItemDto));
                prescriptionItemDto.setTime(getTimes(prescriptionItemDto));
            }
            if (prescriptionItemDto.getDate() != null && !prescriptionItemDto.getDate().isEmpty()
                    && prescriptionItemDto.getTime() != null && !prescriptionItemDto.getTime().isEmpty()) {
                if (duplicateItem == null) {
                    duplicateItem = createDuplicateItem(prescriptionItem);
                    duplicateItem.setEmployee(employeeMapper.toModel(employee));
                }
                duplicateItem.setTimePattern(createDateAndTimePattern
                        (prescriptionItemDto.getDate(), prescriptionItemDto.getTime()));
                eventServiceImpl.createEvent(duplicateItem,
                        createDateTimeListForEvents(prescriptionItemDto.getDate(), prescriptionItemDto.getTime()), employee);
                eventServiceImpl.updateEventStatus(prescriptionItem.getId(), "Date and time pattern was changed", employee);
            }
            if (prescriptionItemDto.getStartTreatment() != null) {
                prescriptionItem.setStartTreatment(prescriptionItemDto.getStartTreatment());
            }
            if (prescriptionItemDto.getEndTreatment() != null) {
                prescriptionItem.setEndTreatment(prescriptionItemDto.getEndTreatment());
            }
            prescriptionItemRepo.save(prescriptionItem);
            log.info("Dr. " + employee.getLastName() + " updated item id " + prescriptionItem.getId());
            if (duplicateItem != null) {
                prescriptionItemRepo.save(duplicateItem);
                log.info("Dr. " + employee.getLastName() + " updated item id " + prescriptionItem.getId());
            }
        });
    }

    /**
     * Method to create duplicate prescription item
     *
     * @param item prescription item
     * @return duplicate
     */
    private PrescriptionItem createDuplicateItem(PrescriptionItem item) {
        PrescriptionItem duplicateItem = new PrescriptionItem();
        duplicateItem.setId(null);
        duplicateItem.setDose(item.getDose());
        duplicateItem.setStartTreatment(item.getStartTreatment());
        duplicateItem.setEndTreatment(item.getEndTreatment());
        duplicateItem.setTimePattern(item.getTimePattern());
        duplicateItem.setPrescription(item.getPrescription());
        duplicateItem.setTherapy(item.getTherapy());
        duplicateItem.setEmployee(item.getEmployee());
        duplicateItem.setPrescriptionItemStatus(item.getPrescriptionItemStatus());
        duplicateItem.setCancellationReason(item.getCancellationReason());
        prescriptionItemRepo.save(duplicateItem);
        return duplicateItem;
    }

    public List<LocalDate> getDates(PrescriptionItemDto itemDto) {
        List<String> strings = Arrays.asList(itemDto.getDates().split(","));
        return strings.stream().map(LocalDate::parse).collect(Collectors.toList());
    }

    public List<LocalTime> getTimes(PrescriptionItemDto itemDto) {
        List<String> strings = Arrays.asList(itemDto.getTimes().split(","));
        return strings.stream().map(LocalTime::parse).collect(Collectors.toList());
    }

    /**
     * Method to create date and time pattern
     *
     * @param dates dates
     * @param times times
     * @return date and time pattern
     */
    private String createDateAndTimePattern(List<LocalDate> dates, List<LocalTime> times) {
        return (times.size() + " time(s) per day, " + dates.size() + " days, "
                + dates.stream().map(p -> p.getDayOfWeek().name()).collect(Collectors.toSet()));

    }

    @Override
    public List<LocalDateTime> createDateTimeListForEvents(List<LocalDate> dates, List<LocalTime> times) {
        List<LocalDateTime> dateTimeList = new ArrayList<>();
        for (LocalDate date : dates) {
            for (LocalTime time : times) {
                dateTimeList.add(date
                        .atTime(time));
            }
        }
        return dateTimeList;
    }
}