package com.javaschool.komarov.reha.service;

import com.javaschool.komarov.reha.dto.EventDto;
import com.javaschool.komarov.reha.dto.PatientDto;
import com.javaschool.komarov.reha.dto.PrescriptionDto;
import com.javaschool.komarov.reha.dto.PrescriptionItemDto;
import com.javaschool.komarov.reha.dto.TherapyDto;
import com.javaschool.komarov.reha.model.EventStatus;
import com.javaschool.komarov.reha.model.PatientStatus;
import com.javaschool.komarov.reha.model.PrescriptionItemStatus;
import com.javaschool.komarov.reha.model.TherapyType;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ValidationService {
    private final PatientService patientService;
    private final EventService eventService;
    private final PrescriptionItemService prescriptionItemService;
    private final TherapyService therapyService;

    public ValidationService(PatientService patientService, EventService eventService, PrescriptionItemService prescriptionItemService, TherapyService therapyService) {
        this.patientService = patientService;
        this.eventService = eventService;
        this.prescriptionItemService = prescriptionItemService;
        this.therapyService = therapyService;
    }

    public void checkPatient(PatientDto patientDto, BindingResult bindingResult) {
        String patientFirstName = patientDto.getFirstName();
        String patientLastName = patientDto.getLastName();
        String patientHealthInsurance = patientDto.getHealthInsurance();

        if (patientFirstName == null || patientFirstName.isEmpty()) {
            bindingResult.addError(new FieldError("newPatient", "firstName", "The first name should not be empty"));
        } else if (!patientFirstName.matches("^[a-zA-Z][a-zA-Z]{1,20}$")) {
            bindingResult.addError(new FieldError("newPatient", "firstName", "The use of numbers and special characters is not allowed"));
        }

        if (patientLastName == null || patientLastName.isEmpty()) {
            bindingResult.addError(new FieldError("newPatient", "lastName", "The last name should not be empty"));
        } else if (!patientLastName.matches("^[a-zA-Z][a-zA-Z]{1,20}$")) {
            bindingResult.addError(new FieldError("newPatient", "lastName", "The use of numbers and special characters is not allowed"));
        }

        if (patientHealthInsurance == null || patientHealthInsurance.isEmpty()) {
            bindingResult.addError(new FieldError("newPatient", "healthInsurance", "The health insurance should not be empty"));
        } else if (!patientHealthInsurance.matches("[0-9]{8}")) {
            bindingResult.addError(new FieldError("newPatient", "healthInsurance", "8 characters, numbers only"));
        }
        if (patientService.checkPatientInDb(patientHealthInsurance)) {
            bindingResult.addError(new FieldError("newPatient", "healthInsurance", "This patient has already been registered"));
        }
    }

    public void checkPrescription(PrescriptionDto prescriptionDto, BindingResult bindingResult) {
        String diagnosis = prescriptionDto.getDiagnosis();
        if (diagnosis == null || diagnosis.isEmpty()) {
            bindingResult.addError(new FieldError("newPrescription", "diagnosis", "Diagnosis should not be empty"));
        } else if (!diagnosis.matches("^[a-zA-Z][a-zA-Z0-9-_.]{1,60}$")) {
            bindingResult.addError(new FieldError("newPrescription", "diagnosis", "maximum of 60 characters"));
        }

    }

    public void checkPatientStatus(PatientDto patientDto, BindingResult bindingResult) {
        PatientStatus status = patientDto.getStatus();
        if (status == null) {
            bindingResult.addError(new FieldError("updatedPatient", "status", "Status should not be empty"));
        } else if (!(status.equals(PatientStatus.ISTREATED) || status.equals(PatientStatus.DISCHARGED))) {
            bindingResult.addError(new FieldError("updatedPatient", "status", "Invalid status"));
        }
        if (patientService.hasActiveEvent(patientDto.getId())
                && patientDto.getStatus().equals(PatientStatus.DISCHARGED)) {
            bindingResult.addError(new FieldError("updatedPatient", "status", "End active events"));
        }
    }

    public void checkEventStatus(EventDto eventDto, BindingResult bindingResult) {
        EventStatus status = eventDto.getEventStatus();
        String cancellationReason = eventDto.getCancellationReason();
        if (status == null) {
            bindingResult.addError(new FieldError("newEvent", "eventStatus", "Status should not be empty"));
        } else if (!(status.equals(EventStatus.COMPLETED)
                || status.equals(EventStatus.SCHEDULED)
                || status.equals(EventStatus.CANCELLED))) {
            bindingResult.addError(new FieldError("newEvent", "eventStatus", "Invalid status"));
        }

        assert status != null;
        if (status.equals(EventStatus.CANCELLED) && cancellationReason.isEmpty()) {
            bindingResult.addError(new FieldError("newEvent", "eventStatus", "Cancellation reason should not be empty"));
        } else if (!eventService.getEventStatusById(eventDto.getId()).equals(EventStatus.SCHEDULED)) {
            bindingResult.addError(new FieldError("newEvent", "eventStatus", "The current status cannot be changed"));
        }

        if (cancellationReason != null) {
            if (!cancellationReason.isEmpty()) {
                if (!status.equals(EventStatus.CANCELLED)) {
                    bindingResult.addError(new FieldError("newEvent", "eventStatus", "Cancellation reason allowed only for the CANCELLED status"));
                }
            }

        }
    }

    public void checkPrescriptionItem(PrescriptionItemDto prescriptionItemDto, BindingResult bindingResult) {
        Integer dose = prescriptionItemDto.getDose();
        LocalDate startTreatment = prescriptionItemDto.getStartTreatment();
        LocalDate endTreatment = prescriptionItemDto.getEndTreatment();
        List<LocalDate> date = prescriptionItemDto.getDate();
        List<LocalTime> time = prescriptionItemDto.getTime();
        PrescriptionItemStatus prescriptionItemStatus = prescriptionItemDto.getPrescriptionItemStatus();
        String cancellationReason = prescriptionItemDto.getCancellationReason();
        long therapyId = prescriptionItemDto.getTherapyId();
        PrescriptionItemDto oldItem = null;
        PrescriptionItemStatus oldStatus = null;

        TherapyDto therapy = null;
        if (therapyId != 0) {
            therapy = therapyService.getTherapyDtoById(therapyId);
        }

        if (prescriptionItemDto.getItemId() != null) {
            oldItem = prescriptionItemService.getPrescriptionItemById(prescriptionItemDto.getItemId());
            if (oldItem != null) {
                oldStatus = oldItem.getPrescriptionItemStatus();
                therapy = oldItem.getTherapy();
            }
        }

        LocalDateTime maxEventDate;
        LocalDateTime minEventDate;
        List<LocalDateTime> dateTimeList = prescriptionItemService.createDateTimeListForEvents(prescriptionItemDto.getDate(), prescriptionItemDto.getTime());

        if (date != null && oldItem == null) {
            if (dateTimeList.stream().max(Comparator.naturalOrder()).isPresent()) {
                maxEventDate = dateTimeList.stream().max(Comparator.naturalOrder()).get();
                minEventDate = dateTimeList.stream().min(Comparator.naturalOrder()).get();
            } else {
                maxEventDate = LocalDateTime.now();
                minEventDate = LocalDateTime.now();
            }
            if (prescriptionItemDto.getStartTreatment().isBefore(LocalDate.now())
                    || prescriptionItemDto.getStartTreatment().isAfter(prescriptionItemDto.getEndTreatment())
                    || prescriptionItemDto.getStartTreatment().isAfter(minEventDate.toLocalDate())
                    || prescriptionItemDto.getEndTreatment().isBefore(prescriptionItemDto.getStartTreatment())
                    || prescriptionItemDto.getEndTreatment().isBefore(maxEventDate.toLocalDate())) {
                bindingResult.addError(new FieldError("newPrescription", "startTreatment", "Illegal date statement!"));
                bindingResult.addError(new FieldError("newPrescription", "endTreatment", "Illegal date statement!"));
            }

            if (minEventDate.isBefore(LocalDateTime.now())) {
                bindingResult.addError(new FieldError("newPrescription", "time", "It is not possible to assign an event earlier than the current moment"));
            }
        }

        if (oldItem == null && dose == null && therapy != null && !therapy.getTherapyType().equals(TherapyType.PROCEDURE)) {
            bindingResult.addError(new FieldError("newItem", "dose", "Set the dose"));
        }

        if (therapy != null && oldItem == null) {
            if ((dose == null || dose <= 0) && therapy.getTherapyType().equals(TherapyType.MEDICINE)) {
                bindingResult.addError(new FieldError("newItem", "dose", "Invalid dose"));
            }
            if (dose != null && therapy.getTherapyType().equals(TherapyType.PROCEDURE)) {
                bindingResult.addError(new FieldError("newItem", "dose", "Invalid dose"));
            }
        }

        if (oldItem != null) {
            if ((dose != null) && !oldItem.getTherapy().getTherapyType().equals(TherapyType.MEDICINE)) {
                bindingResult.addError(new FieldError("updateItem", "dose", "Dose available only for medicines"));
            }
        }

        if (oldStatus != null) {
            if (oldStatus.equals(PrescriptionItemStatus.CANCELLED) && (dose != null || startTreatment != null || endTreatment != null || date != null || time != null
                    || prescriptionItemStatus != null || cancellationReason != null || therapy != null)) {
                bindingResult.addError(new FieldError("updateItem", "prescriptionItemStatus", "Changes to the cancelled prescription are not available"));
            }
        }

        if (prescriptionItemStatus != null && oldItem == null) {
            if (prescriptionItemStatus.equals(PrescriptionItemStatus.CANCELLED)
                    && (dose != null || startTreatment != null || endTreatment != null
                    || date != null || time != null || therapy != null)) {
                bindingResult.addError(new FieldError("newItem", "prescriptionItemStatus", "Only the reason for cancellation field is available for this status"));
            }
        }

        if (prescriptionItemStatus != null && oldItem != null) {
            if (prescriptionItemStatus.equals(PrescriptionItemStatus.CANCELLED) &&
                    (cancellationReason == null || cancellationReason.isEmpty())) {
                bindingResult.addError(new FieldError("updateItem", "cancellationReason", "Cancellation reason should not be empty"));
            }
        }
    }

    public void checkPatternUpdate(PrescriptionItemDto prescriptionItemDto, BindingResult bindingResult) {
        PrescriptionItemDto prescriptionItem = prescriptionItemService.getPrescriptionItemById(prescriptionItemDto.getItemId());

        if (prescriptionItem != null) {
            Set<EventDto> eventsById = StreamSupport
                    .stream(eventService.getEventDtoByPrescriptionItemId(prescriptionItemDto.getPrescriptionId())
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

            List<LocalDateTime> newEventDateList = prescriptionItemService.createDateTimeListForEvents(prescriptionItemDto.getDate(), prescriptionItemDto.getTime());

            if (numberOfDays * perDay > 0
                    && prescriptionItem.getPrescriptionItemStatus().equals(PrescriptionItemStatus.PRESCRIBED)) {

                for (int i = 0; i < numberOfDays; i++) {
                    for (int j = 0; j < perDay; j++) {
                        newEventDateList.add(prescriptionItemDto
                                .getDate().get(i)
                                .atTime(prescriptionItemDto.getTime().get(j)));
                    }
                }
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
                maxEventDate = oldDatesOfActEvent.stream().max(Comparator.naturalOrder()).get();
                minEventDate = oldDatesOfActEvent.stream().min(Comparator.naturalOrder()).get();
            }

            if (!oldDatesOfCompletedEvent.isEmpty()) {
                minCompletedEventDate = oldDatesOfCompletedEvent.stream().min(Comparator.naturalOrder()).get();
                maxCompletedEventDate = oldDatesOfCompletedEvent.stream().max(Comparator.naturalOrder()).get();
            }

            if (!newEventDateList.isEmpty()) {
                maxEventDate = newEventDateList.stream().max(Comparator.naturalOrder()).get();
                minEventDate = newEventDateList.stream().min(Comparator.naturalOrder()).get();
            }

            if (!newEventDateList.isEmpty() && minEventDate.isBefore(LocalDateTime.now())) {

                bindingResult.addError(new FieldError("updateItem", "time", "It is not possible to assign an event earlier than the current moment"));
            }

            if ((prescriptionItemDto.getStartTreatment() != null
                    || prescriptionItemDto.getEndTreatment() != null)
                    && !prescriptionItem.getPrescriptionItemStatus().equals(PrescriptionItemStatus.CANCELLED)) {

                if (newEventDateList.isEmpty() && (startTreatment.isAfter(endTreatment)
                        || startTreatment.isBefore(LocalDate.now())
                        || startTreatment.isAfter(minEventDate.toLocalDate()))) {
                    bindingResult.addError(new FieldError("updateItem", "startTreatment", "Illegal date statement!"));
                }


                if (newEventDateList.isEmpty() && (endTreatment.isBefore(startTreatment)
                        || endTreatment.isBefore(LocalDate.now())
                        || endTreatment.isBefore(maxEventDate.toLocalDate()))) {
                    bindingResult.addError(new FieldError("updateItem", "endTreatment", "Illegal date statement!"));
                }

                if (!newEventDateList.isEmpty() && (startTreatment.isAfter(endTreatment)
                        || startTreatment.isBefore(LocalDate.now())
                        || startTreatment.isAfter(minEventDate.toLocalDate())
                        || startTreatment.isAfter(minCompletedEventDate.toLocalDate()))
                ) {
                    bindingResult.addError(new FieldError("updateItem", "startTreatment", "Illegal date statement, check date and time pattern"));
                }

                if (!newEventDateList.isEmpty() && (endTreatment.isBefore(startTreatment)
                        || endTreatment.isBefore(LocalDate.now())
                        || endTreatment.isBefore(maxEventDate.toLocalDate())
                        || endTreatment.isBefore(maxCompletedEventDate.toLocalDate()))
                ) {
                    bindingResult.addError(new FieldError("updateItem", "endTreatment", "Illegal date statement, check date and time pattern"));
                }

            }
        } else {
            bindingResult.addError(new FieldError("updateItem", "itemId", "Prescription is non-existent"));
        }
    }
}
