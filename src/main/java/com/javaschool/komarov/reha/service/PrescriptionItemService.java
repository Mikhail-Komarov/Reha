package com.javaschool.komarov.reha.service;

import com.javaschool.komarov.reha.dto.EventDto;
import com.javaschool.komarov.reha.dto.PrescriptionItemDto;
import com.javaschool.komarov.reha.mapper.PrescriptionItemMapper;
import com.javaschool.komarov.reha.model.EventStatus;
import com.javaschool.komarov.reha.model.PrescriptionItem;
import com.javaschool.komarov.reha.model.PrescriptionItemStatus;
import com.javaschool.komarov.reha.model.TherapyType;
import com.javaschool.komarov.reha.repository.PrescriptionItemRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
        if (prescriptionItemRepo.findById(id).isPresent()) {
            prescriptionItemDto = prescriptionItemMapper.toDTO(prescriptionItemRepo.getById(id));
        }
        return prescriptionItemDto;
    }

    public void savePrescriptionItem(PrescriptionItemDto prescriptionItemDto) {

        LocalDate maxEventDate = prescriptionItemDto.getDate().stream().max(Comparator.naturalOrder()).orElseThrow(NullPointerException::new);
        LocalDate minEventDate = prescriptionItemDto.getDate().stream().min(Comparator.naturalOrder()).orElseThrow(NullPointerException::new);

        if (prescriptionItemDto.getStartTreatment().isBefore(LocalDate.now())
                || prescriptionItemDto.getStartTreatment().isAfter(prescriptionItemDto.getEndTreatment())
                || prescriptionItemDto.getStartTreatment().isAfter(minEventDate)
                || prescriptionItemDto.getEndTreatment().isBefore(prescriptionItemDto.getStartTreatment())
                || prescriptionItemDto.getEndTreatment().isBefore(maxEventDate)) {
            throw new RuntimeException("Illegal date statement!");
        }

        prescriptionItemDto.setEmployee(employeeService.getEmployeeDtoById(prescriptionItemDto.getEmployeeId()));
        prescriptionItemDto.setTherapy(therapyService.getTherapyDtoById(prescriptionItemDto.getTherapyId()));
        prescriptionItemDto.setPrescription(prescriptionService.getPrescriptionById(prescriptionItemDto.getPrescriptionId()));
        prescriptionItemDto.setPrescriptionItemStatus(PrescriptionItemStatus.PRESCRIBED);

        int numberOfDays = prescriptionItemDto.getDate().size();
        int perDay = prescriptionItemDto.getTime().size();

        List<LocalDateTime> dateTimeListForEvents = new ArrayList<>();
        Set<String> daysOfWeek = new HashSet<>();

        for (int i = 0; i < numberOfDays; i++) {
            for (int j = 0; j < perDay; j++) {
                dateTimeListForEvents.add(prescriptionItemDto.getDate().get(i).atTime(prescriptionItemDto.getTime().get(j)));
                daysOfWeek.add(prescriptionItemDto.getDate().get(i).getDayOfWeek().name());
            }
        }
        prescriptionItemDto.setTimePattern(createDateAndTimePattern(perDay, numberOfDays, daysOfWeek));
        PrescriptionItemDto newItemDto = prescriptionItemMapper.toDTO(prescriptionItemRepo.saveAndFlush(prescriptionItemMapper.toModel(prescriptionItemDto)));
        patientService.setStatusIsTreated(prescriptionItemDto.getPrescription().getPatient());
        eventService.createEvent(newItemDto, dateTimeListForEvents);
    }

    public void updatePrescriptionItem(PrescriptionItemDto prescriptionItemDto) {
        PrescriptionItem prescriptionItem = null;
        if (prescriptionItemRepo.findById(prescriptionItemDto.getId()).isPresent()) {
            prescriptionItem = prescriptionItemRepo.findById(prescriptionItemDto.getId()).get();
        }
        assert prescriptionItem != null;
        PrescriptionItemStatus prescriptionItemStatus = prescriptionItem.getPrescriptionItemStatus();
        if (prescriptionItemDto.getPrescriptionItemStatus() != null) {
            prescriptionItemStatus = prescriptionItemDto.getPrescriptionItemStatus();
        }
        if (prescriptionItemDto.getPrescriptionItemStatus().equals(PrescriptionItemStatus.CANCELLED)
                && prescriptionItem.getPrescriptionItemStatus().equals(PrescriptionItemStatus.PRESCRIBED)
                && prescriptionItemDto.getCancellationReason() != null
                && !prescriptionItemDto.getCancellationReason().isEmpty()) {

            prescriptionItem.setPrescriptionItemStatus(prescriptionItemDto.getPrescriptionItemStatus());
            prescriptionItem.setCancellationReason(prescriptionItemDto.getCancellationReason());
            prescriptionItemRepo.save(prescriptionItem);
            eventService.updateEventStatus(prescriptionItemDto.getId(), prescriptionItemDto.getCancellationReason());
        }

        if (prescriptionItemDto.getDose() != null
                && prescriptionItemDto.getDose() != 0
                && prescriptionItem.getTherapy().getTherapyType().equals(TherapyType.MEDICINE)
                && prescriptionItemDto.getDose() > 0
                && !prescriptionItemStatus.equals(PrescriptionItemStatus.CANCELLED)) {
            prescriptionItem.setDose(prescriptionItemDto.getDose());
            prescriptionItemRepo.save(prescriptionItem);
        }

        Set<EventDto> eventsById = StreamSupport
                .stream(eventService.getEventDtoByPrescriptionItemId(prescriptionItemDto.getId())
                        .spliterator(), false)
                .collect(Collectors.toSet());

        List<LocalDateTime> oldDatesOfActEvent = eventsById.stream()
                .filter(p -> !p.getEventStatus().equals(EventStatus.CANCELLED))
                .map(EventDto::getDateTime).collect(Collectors.toList());

        List<LocalDateTime> oldDatesOfCompletedEvent = eventsById.stream()
                .filter(p -> p.getEventStatus().equals(EventStatus.COMPLETED))
                .map(EventDto::getDateTime).collect(Collectors.toList());

        int numberOfDays = prescriptionItemDto.getDate().size();
        int perDay = prescriptionItemDto.getTime().size();

        List<LocalDateTime> newEventDateList = new ArrayList<>();
        Set<String> daysOfWeek = new HashSet<>();

        if (numberOfDays * perDay > 0
                && prescriptionItemStatus.equals(PrescriptionItemStatus.PRESCRIBED)) {

            for (int i = 0; i < numberOfDays; i++) {
                for (int j = 0; j < perDay; j++) {
                    newEventDateList.add(prescriptionItemDto
                            .getDate().get(i)
                            .atTime(prescriptionItemDto.getTime().get(j)));
                    daysOfWeek.add(prescriptionItemDto.getDate().get(i).getDayOfWeek().name());
                }
            }

            prescriptionItem.setTimePattern(createDateAndTimePattern(perDay, numberOfDays, daysOfWeek));
            prescriptionItemRepo.save(prescriptionItem);
            eventService.createEvent(prescriptionItemMapper.toDTO(prescriptionItem), newEventDateList);
        }

        LocalDateTime maxEventDate = LocalDateTime.now();
        LocalDateTime minEventDate = LocalDateTime.now();
        LocalDateTime maxCompletedEventDate = LocalDateTime.now();
        LocalDateTime minCompletedEventDate = LocalDateTime.now();

        LocalDate endTreatment = prescriptionItem.getEndTreatment();
        LocalDate startTreatment = prescriptionItem.getStartTreatment();

        if (prescriptionItemDto.getStartTreatment() != null) {
            startTreatment = prescriptionItemDto.getStartTreatment();
        }
        if (prescriptionItemDto.getEndTreatment() != null) {
            endTreatment = prescriptionItemDto.getEndTreatment();
        }

        if (!oldDatesOfActEvent.isEmpty()) {
            maxEventDate = oldDatesOfActEvent.stream().max(Comparator.naturalOrder()).orElseThrow(NullPointerException::new);
            minEventDate = oldDatesOfActEvent.stream().min(Comparator.naturalOrder()).orElseThrow(NullPointerException::new);

        }

        if (!oldDatesOfCompletedEvent.isEmpty()) {
            minCompletedEventDate = oldDatesOfCompletedEvent.stream().min(Comparator.naturalOrder()).orElseThrow(NullPointerException::new);
            maxCompletedEventDate = oldDatesOfCompletedEvent.stream().max(Comparator.naturalOrder()).orElseThrow(NullPointerException::new);
        }

        if (!newEventDateList.isEmpty()) {
            maxEventDate = newEventDateList.stream().max(Comparator.naturalOrder()).orElseThrow(NullPointerException::new);
            minEventDate = newEventDateList.stream().min(Comparator.naturalOrder()).orElseThrow(NullPointerException::new);

        }

        if ((prescriptionItemDto.getStartTreatment() != null
                || prescriptionItemDto.getEndTreatment() != null)
                && !prescriptionItemStatus.equals(PrescriptionItemStatus.CANCELLED)) {

            if (newEventDateList.isEmpty() && !startTreatment.isAfter(endTreatment)
                    && !startTreatment.isBefore(LocalDate.now())
                    && !startTreatment.isAfter(minEventDate.toLocalDate())) {
                prescriptionItem.setStartTreatment(startTreatment);
            }


            if (newEventDateList.isEmpty() && !endTreatment.isBefore(startTreatment)
                    && !endTreatment.isBefore(LocalDate.now())
                    && !endTreatment.isBefore(maxEventDate.toLocalDate())) {
                prescriptionItem.setEndTreatment(endTreatment);
            }

            if (!newEventDateList.isEmpty() && !startTreatment.isAfter(endTreatment)
                    && !startTreatment.isBefore(LocalDate.now())
                    && !startTreatment.isAfter(minEventDate.toLocalDate())
                    && !startTreatment.isAfter(minCompletedEventDate.toLocalDate())
            ) {
                prescriptionItem.setStartTreatment(startTreatment);
            }

            if (!newEventDateList.isEmpty() && !endTreatment.isBefore(startTreatment)
                    && !endTreatment.isBefore(LocalDate.now())
                    && !endTreatment.isBefore(maxEventDate.toLocalDate())
                    && !endTreatment.isBefore(maxCompletedEventDate.toLocalDate())
            ) {
                prescriptionItem.setEndTreatment(endTreatment);
            }
            prescriptionItemRepo.save(prescriptionItem);
        }

    }

    public String createDateAndTimePattern(int perDay, int numberOfDays, Set<String> daysOfWeek) {
        return (perDay + "time(s) per day, " + numberOfDays + " days, "
                + daysOfWeek);

    }

}
