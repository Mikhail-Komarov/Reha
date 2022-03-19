package com.javaschool.komarov.reha.service;

import com.javaschool.komarov.reha.dto.PrescriptionItemDto;
import com.javaschool.komarov.reha.mapper.PrescriptionItemMapper;
import com.javaschool.komarov.reha.model.PrescriptionItem;
import com.javaschool.komarov.reha.model.PrescriptionItemStatus;
import com.javaschool.komarov.reha.repository.PrescriptionItemRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrescriptionItemService {
    private final PrescriptionItemMapper prescriptionItemMapper;
    private final PrescriptionItemRepo prescriptionItemRepo;

    private final EmployeeService employeeService;
    private final TherapyService therapyService;
    private final PrescriptionService prescriptionService;
    private final EventService eventService;
    private final PatientService patientService;

    public PrescriptionItemService(PrescriptionItemMapper prescriptionItemMapper, PrescriptionItemRepo prescriptionItemRepo, EmployeeService employeeService,
                                   TherapyService therapyService, PrescriptionService prescriptionService, EventService eventService, PatientService patientService) {
        this.prescriptionItemMapper = prescriptionItemMapper;
        this.prescriptionItemRepo = prescriptionItemRepo;
        this.employeeService = employeeService;
        this.therapyService = therapyService;
        this.prescriptionService = prescriptionService;
        this.eventService = eventService;
        this.patientService = patientService;
    }

    public Iterable<PrescriptionItemDto> getPrescriptionItemByPrescriptionID(Long id) {
        return prescriptionItemMapper.
                toDTOList(prescriptionItemRepo.findPrescriptionItemByPrescriptionId(id));
    }

    public List<LocalDate> getDateList() {
        List<LocalDate> dateList = new ArrayList<>();
        for (int i = 0; i < 31; i++) {
            dateList.add(LocalDate.now().plusDays(i));
        }
        return dateList;
    }

    public List<LocalTime> getTimeList() {
        List<LocalTime> timeList = new ArrayList<>();
        for (int j = 0; j < 24; j++) {
            timeList.add(LocalTime.parse("00:00").plusHours(j));
        }
        return timeList;
    }

    public PrescriptionItemDto getPrescriptionItemById(Long id) {
        PrescriptionItemDto prescriptionItemDto = null;
        if (id != null && prescriptionItemRepo.findById(id).isPresent()) {
            prescriptionItemDto = prescriptionItemMapper.toDTO(prescriptionItemRepo.getById(id));
        }
        return prescriptionItemDto;
    }

    public void savePrescriptionItem(PrescriptionItemDto prescriptionItemDto) {

        prescriptionItemDto.setEmployee(employeeService.getEmployeeDtoById(prescriptionItemDto.getEmployeeId()));
        prescriptionItemDto.setTherapy(therapyService.getTherapyDtoById(prescriptionItemDto.getTherapyId()));
        prescriptionItemDto.setPrescription(prescriptionService.getPrescriptionById(prescriptionItemDto.getPrescriptionId()));
        prescriptionItemDto.setPrescriptionItemStatus(PrescriptionItemStatus.PRESCRIBED);
        prescriptionItemDto.setTimePattern(createDateAndTimePattern(prescriptionItemDto.getDate(), prescriptionItemDto.getTime()));
        PrescriptionItemDto newItemDto = prescriptionItemMapper.toDTO(prescriptionItemRepo.saveAndFlush(prescriptionItemMapper.toModel(prescriptionItemDto)));
        patientService.setStatusIsTreated(prescriptionItemDto.getPrescription().getPatient());
        eventService.createEvent(newItemDto, createDateTimeListForEvents(prescriptionItemDto.getDate(), prescriptionItemDto.getTime()));
    }

    public void updatePrescriptionItem(PrescriptionItemDto prescriptionItemDto) {
        PrescriptionItem prescriptionItem = null;
        if (prescriptionItemRepo.findById(prescriptionItemDto.getItemId()).isPresent()) {
            prescriptionItem = prescriptionItemRepo.findById(prescriptionItemDto.getItemId()).get();
        }

        if (prescriptionItem != null) {

            if (prescriptionItemDto.getPrescriptionItemStatus() != null) {
                prescriptionItem.setPrescriptionItemStatus(prescriptionItemDto.getPrescriptionItemStatus());
            }
            if (prescriptionItemDto.getCancellationReason() != null && !prescriptionItemDto.getCancellationReason().isEmpty()) {
                prescriptionItem.setCancellationReason(prescriptionItemDto.getCancellationReason());
                prescriptionItemRepo.save(prescriptionItem);
                eventService.updateEventStatus(prescriptionItemDto.getItemId(), prescriptionItemDto.getCancellationReason());
            }

            if (prescriptionItemDto.getDose() != null) {
                prescriptionItem.setDose(prescriptionItemDto.getDose());
            }

            if (prescriptionItemDto.getDate() != null && !prescriptionItemDto.getDate().isEmpty()
                    && prescriptionItemDto.getTime() != null && !prescriptionItemDto.getTime().isEmpty()) {

                prescriptionItem.setTimePattern(createDateAndTimePattern
                        (prescriptionItemDto.getDate(), prescriptionItemDto.getTime()));
                prescriptionItemRepo.save(prescriptionItem);
                eventService.createEvent(prescriptionItemMapper.toDTO(prescriptionItem),
                        createDateTimeListForEvents(prescriptionItemDto.getDate(), prescriptionItemDto.getTime()));
            }

            if (prescriptionItemDto.getStartTreatment() != null) {
                prescriptionItem.setStartTreatment(prescriptionItemDto.getStartTreatment());
            }

            if (prescriptionItemDto.getEndTreatment() != null) {
                prescriptionItem.setEndTreatment(prescriptionItemDto.getEndTreatment());
            }
            prescriptionItemRepo.save(prescriptionItem);
        }
    }

    public String createDateAndTimePattern(List<LocalDate> dates, List<LocalTime> times) {
        return (times.size() + " time(s) per day, " + dates.size() + " days, "
                + dates.stream().map(p -> p.getDayOfWeek().name()).collect(Collectors.toSet()));

    }

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
