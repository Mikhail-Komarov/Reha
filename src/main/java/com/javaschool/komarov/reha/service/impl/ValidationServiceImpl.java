package com.javaschool.komarov.reha.service.impl;

import com.javaschool.komarov.reha.model.EventStatus;
import com.javaschool.komarov.reha.model.PatientStatus;
import com.javaschool.komarov.reha.model.PrescriptionItemStatus;
import com.javaschool.komarov.reha.model.TherapyType;
import com.javaschool.komarov.reha.model.dto.EventDto;
import com.javaschool.komarov.reha.model.dto.PatientDto;
import com.javaschool.komarov.reha.model.dto.PrescriptionDto;
import com.javaschool.komarov.reha.model.dto.PrescriptionItemDto;
import com.javaschool.komarov.reha.model.dto.TherapyDto;
import com.javaschool.komarov.reha.model.entity.PrescriptionItem;
import com.javaschool.komarov.reha.model.entity.Therapy;
import com.javaschool.komarov.reha.service.api.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Slf4j
@Service
public class ValidationServiceImpl implements ValidationService {
    private final PatientServiceImpl patientServiceImpl;
    private final EventServiceImpl eventServiceImpl;
    private final PrescriptionItemServiceImpl prescriptionItemServiceImpl;
    private final TherapyServiceImpl therapyServiceImpl;
    private final EmployeeServiceImpl employeeServiceImpl;

    @Override
    public void checkPatient(PatientDto patientDto, BindingResult bindingResult) {
        if (patientDto == null) {
            bindingResult.addError(new FieldError("newPatient", "id", "PatientDto should not be null"));
        } else {
            String patientFirstName = patientDto.getFirstName();
            String patientLastName = patientDto.getLastName();
            String patientHealthInsurance = patientDto.getHealthInsurance();

            checkPatientFirstName(patientFirstName, bindingResult);
            checkPatientLastName(patientLastName, bindingResult);
            checkPatientHealthInsurance(patientHealthInsurance, bindingResult);
            checkPatientDuplicate(patientHealthInsurance, bindingResult);
        }
        if (!bindingResult.hasErrors()) {
            log.info("The patient's check was successful " + patientDto);
        }
    }

    @Override
    public void checkTherapy(TherapyDto therapyDto, BindingResult bindingResult) {
        if (therapyDto == null) {
            bindingResult.addError(new FieldError("newTherapy", "id", "Therapy should not be null"));
        } else {
            if (therapyDto.getName() == null || therapyDto.getName().isEmpty()) {
                bindingResult.addError(new FieldError("newTherapy", "name", "Therapy name should not be null"));
            } else if (!therapyDto.getName().matches("^[a-zA-Z][a-zA-Z]{1,20}$")) {
                bindingResult.addError(new FieldError("newTherapy", "name", "The use of numbers and special characters is not allowed"));
            } else if (therapyServiceImpl.therapyIsExist(therapyDto.getName())) {
                bindingResult.addError(new FieldError("newTherapy", "name", "Therapy with this name is exist"));
            }

            if (therapyDto.getTherapyType() == null || !(therapyDto.getTherapyType().equals(TherapyType.PROCEDURE) || therapyDto.getTherapyType().equals(TherapyType.MEDICINE))) {
                bindingResult.addError(new FieldError("newTherapy", "therapyType", "Invalid therapy status"));
            }

        }
        if (!bindingResult.hasErrors()) {
            log.info("The therapy's check was successful " + therapyDto);
        }
    }

    private void checkPatientFirstName(String firstName, BindingResult bindingResult) {
        if (firstName == null || firstName.isEmpty()) {
            bindingResult.addError(new FieldError("newPatient", "firstName", "The first name should not be empty"));
        } else if (!firstName.matches("^[a-zA-Z][a-zA-Z]{1,20}$")) {
            bindingResult.addError(new FieldError("newPatient", "firstName", "The use of numbers and special characters is not allowed"));
        }
    }

    /**
     *
     * @param lastName
     * @param bindingResult
     */
    private void checkPatientLastName(String lastName, BindingResult bindingResult) {
        if (lastName == null || lastName.isEmpty()) {
            bindingResult.addError(new FieldError("newPatient", "lastName", "The last name should not be empty"));
        } else if (!lastName.matches("^[a-zA-Z][a-zA-Z]{1,20}$")) {
            bindingResult.addError(new FieldError("newPatient", "lastName", "The use of numbers and special characters is not allowed"));
        }
    }

    /**
     *
     * @param healthInsurance
     * @param bindingResult
     */
    private void checkPatientHealthInsurance(String healthInsurance, BindingResult bindingResult) {
        if (healthInsurance == null || healthInsurance.isEmpty()) {
            bindingResult.addError(new FieldError("newPatient", "healthInsurance", "The health insurance should not be empty"));
        } else if (!healthInsurance.matches("[0-9]{8}")) {
            bindingResult.addError(new FieldError("newPatient", "healthInsurance", "8 characters, numbers only"));
        }
    }

    /**
     *
     * @param healthInsurance
     * @param bindingResult
     */
    private void checkPatientDuplicate(String healthInsurance, BindingResult bindingResult) {
        if (patientServiceImpl.checkPatientInDb(healthInsurance)) {
            bindingResult.addError(new FieldError("newPatient", "healthInsurance", "This patient has already been registered"));
        }
    }

    @Override
    public void checkPrescription(PrescriptionDto prescriptionDto, BindingResult bindingResult, Long patientId, UserDetails userDetails) {
        if (prescriptionDto == null) {
            bindingResult.addError(new FieldError("newPrescription", "id", "PrescriptionDto should not be null"));
        } else {
            String diagnosis = prescriptionDto.getDiagnosis();
            checkDiagnosis(diagnosis, bindingResult);
            checkPrescriptionPatient(patientId, bindingResult);
            if (userDetails == null) {
                bindingResult.addError(new FieldError("newPrescription", "employee", "Employee should not be empty"));
            } else {
                checkPrescriptionEmployee(employeeServiceImpl.getEmployeeDTOByLogin(userDetails.getUsername()).getId(), bindingResult);
            }
        }
        if (!bindingResult.hasErrors()) {
            log.info("The prescription's check was successful " + prescriptionDto);
        }
    }

    /**
     *
     * @param id
     * @param bindingResult
     */
    private void checkPrescriptionPatient(Long id, BindingResult bindingResult) {
        if (id == null || !patientServiceImpl.checkPatientInDb(id)) {
            bindingResult.addError(new FieldError("newPrescription", "patient", "Patient should not be empty"));
        }
    }

    /**
     *
     * @param id
     * @param bindingResult
     */
    private void checkPrescriptionEmployee(Long id, BindingResult bindingResult) {
        if (id == null || !employeeServiceImpl.EmployeeIsExist(id)) {
            bindingResult.addError(new FieldError("newPrescription", "employee", "Employee should not be empty"));
        }
    }

    /**
     *
     * @param diagnosis
     * @param bindingResult
     */
    private void checkDiagnosis(String diagnosis, BindingResult bindingResult) {
        if (diagnosis == null || diagnosis.isEmpty()) {
            bindingResult.addError(new FieldError("newPrescription", "diagnosis", "Diagnosis should not be empty"));
        } else if (!diagnosis.matches("^[a-zA-Z][a-zA-Z0-9-_.]{1,60}$")) {
            bindingResult.addError(new FieldError("newPrescription", "diagnosis", "Invalid characters or exceeded the limit of 60 characters"));
        }
    }

    @Override
    public void checkPatientStatus(PatientDto patientDto, BindingResult bindingResult) {
        PatientStatus status = null;
        if (patientDto == null || patientDto.getId() == null || !patientServiceImpl.checkPatientInDb(patientDto.getId())) {
            bindingResult.addError(new FieldError("updatedPatient", "id", "Patient does not exist"));
        } else {
            try {
                status = patientDto.getStatus();
            } catch (IllegalArgumentException e) {
                bindingResult.addError(new FieldError("updatedPatient", "status", "Invalid status"));
            }
            if (status == null) {
                bindingResult.addError(new FieldError("updatedPatient", "status", "Status should not be empty"));
            }
            if (patientServiceImpl.hasActiveEvent(patientDto.getId())
                    && patientDto.getStatus().equals(PatientStatus.DISCHARGED)) {
                bindingResult.addError(new FieldError("updatedPatient", "status", "End active events"));
            }
        }
    }

    @Override
    public void checkEventStatus(EventDto eventDto, BindingResult bindingResult) {
        EventStatus status = null;
        if (eventDto == null || eventDto.getId() == null || !eventServiceImpl.eventIsExist(eventDto.getId())) {
            bindingResult.addError(new FieldError("newEvent", "id", "Event does not exist"));
        } else {
            try {
                status = eventDto.getEventStatus();
            } catch (IllegalArgumentException e) {
                bindingResult.addError(new FieldError("newEvent", "eventStatus", "Invalid status"));
            }
            String cancellationReason = eventDto.getCancellationReason();
            LocalDate eventDate = eventServiceImpl.getEventDateTime(eventDto.getId()).toLocalDate();
            if (status == null) {
                bindingResult.addError(new FieldError("newEvent", "eventStatus", "Status should not be empty"));
            } else if (status.equals(EventStatus.CANCELLED) && (cancellationReason == null || cancellationReason.isEmpty())) {
                bindingResult.addError(new FieldError("newEvent", "eventStatus", "Cancellation reason should not be empty"));
            } else if (!eventServiceImpl.getEventStatusById(eventDto.getId()).equals(EventStatus.SCHEDULED)) {
                bindingResult.addError(new FieldError("newEvent", "eventStatus", "The current status cannot be changed"));
            } else if (cancellationReason != null && !cancellationReason.isEmpty() && !status.equals(EventStatus.CANCELLED)) {
                bindingResult.addError(new FieldError("newEvent", "eventStatus", "Cancellation reason allowed only for the CANCELLED status"));
            } else if (status.equals(EventStatus.COMPLETED) && eventDate.isAfter(LocalDate.now())) {
                bindingResult.addError(new FieldError("newEvent", "eventStatus", "Invalid status for an upcoming event"));
            }
        }
        if (!bindingResult.hasErrors()) {
            log.info("The event's check was successful " + eventDto);
        }
    }

    @Override
    public void checkPrescriptionItem(PrescriptionItemDto prescriptionItemDto, BindingResult bindingResult) {
        if (prescriptionItemDto == null) {
            bindingResult.addError(new FieldError("newItem", "itemId", "Item should not be empty!"));
        } else {
            Integer dose;
            LocalDate startTreatment = prescriptionItemDto.getStartTreatment();
            LocalDate endTreatment = prescriptionItemDto.getEndTreatment();
            if (prescriptionItemDto.getDates() != null) {
                checkDateParser(prescriptionItemDto, bindingResult);
            }
            if (prescriptionItemDto.getTimes() != null) {
                checkTimeParser(prescriptionItemDto, bindingResult);
            }
            List<LocalDate> date = prescriptionItemDto.getDate();
            List<LocalTime> time = prescriptionItemDto.getTime();
            PrescriptionItemStatus prescriptionItemStatus = prescriptionItemDto.getPrescriptionItemStatus();
            String cancellationReason = prescriptionItemDto.getCancellationReason();

            Optional<PrescriptionItem> oldItem;
            if (prescriptionItemDto.getItemId() != null) {
                oldItem = prescriptionItemServiceImpl.getPrescriptionItemById(prescriptionItemDto.getItemId());
            } else {
                oldItem = Optional.empty();
            }
            PrescriptionItemStatus oldStatus = null;

            long therapyId = prescriptionItemDto.getTherapyId();
            Optional<Therapy> therapy = Optional.empty();

            if (therapyId > 0) {
                therapy = therapyServiceImpl.getTherapyById(therapyId);
            }

            if (prescriptionItemDto.getItemId() != null && oldItem.isPresent()) {
                oldStatus = oldItem.get().getPrescriptionItemStatus();
                therapy = Optional.of(oldItem.get().getTherapy());
            }
            dose = prescriptionItemDto.getDose();
            LocalDateTime maxEventDate;
            LocalDateTime minEventDate;
            List<LocalDateTime> dateTimeList = null;
            if (prescriptionItemDto.getDate() != null && prescriptionItemDto.getTime() != null) {
                dateTimeList = prescriptionItemServiceImpl.createDateTimeListForEvents(prescriptionItemDto.getDate(), prescriptionItemDto.getTime());
            }
            if (date != null && !oldItem.isPresent() && dateTimeList != null) {
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
                    bindingResult.addError(new FieldError("newItem", "startTreatment", "Illegal date statement!"));
                    bindingResult.addError(new FieldError("newItem", "endTreatment", "Illegal date statement!"));
                }

                if (minEventDate.isBefore(LocalDateTime.now())) {
                    bindingResult.addError(new FieldError("newItem", "time", "It is not possible to assign an event earlier than the current moment"));
                    prescriptionItemDto.setTimes(null);
                }
            }

            if (!oldItem.isPresent() && dose == null && therapy.isPresent() && !therapy.get().getTherapyType().equals(TherapyType.PROCEDURE)) {
                bindingResult.addError(new FieldError("newItem", "dose", "Set the dose"));
            }

            if (therapy.isPresent() && !oldItem.isPresent()) {
                if ((dose == null || dose <= 0) && therapy.get().getTherapyType().equals(TherapyType.MEDICINE)) {
                    bindingResult.addError(new FieldError("newItem", "dose", "Invalid dose"));
                }
                if (dose != null && therapy.get().getTherapyType().equals(TherapyType.PROCEDURE)) {
                    bindingResult.addError(new FieldError("newItem", "dose", "Invalid dose"));
                }
            }

            if (oldItem.isPresent()) {
                if ((dose != null) && !oldItem.get().getTherapy().getTherapyType().equals(TherapyType.MEDICINE)) {
                    bindingResult.addError(new FieldError("updateItem", "dose", "Dose available only for medicines"));
                }
            }

            if (oldItem.isPresent() && oldStatus != null && oldStatus.equals(PrescriptionItemStatus.CANCELLED)) {
                bindingResult.addError(new FieldError("updateItem", "prescriptionItemStatus", "Changes to the cancelled prescription are not available"));
            }

            if (prescriptionItemStatus != null && prescriptionItemStatus.equals(PrescriptionItemStatus.CANCELLED)) {
                if (dose != null || startTreatment != null || endTreatment != null || (date != null && !date.isEmpty()) || (time != null && !time.isEmpty())) {
                    bindingResult.addError(new FieldError("updateItem", "prescriptionItemStatus", "Changes to the cancelled prescription are not available"));
                }
            }

            if (prescriptionItemStatus != null && !oldItem.isPresent()) {
                if (prescriptionItemStatus.equals(PrescriptionItemStatus.CANCELLED)
                        && (dose != null || startTreatment != null || endTreatment != null
                        || date != null || time != null || therapy.isPresent())) {
                    bindingResult.addError(new FieldError("newItem", "prescriptionItemStatus", "Only the reason for cancellation field is available for this status"));
                }
            }

            if (prescriptionItemStatus != null && oldItem.isPresent()) {
                if (prescriptionItemStatus.equals(PrescriptionItemStatus.CANCELLED) &&
                        (cancellationReason == null || cancellationReason.isEmpty())) {
                    bindingResult.addError(new FieldError("updateItem", "cancellationReason", "Cancellation reason should not be empty"));
                }
            }

            if (!oldItem.isPresent() && startTreatment == null) {
                bindingResult.addError(new FieldError("newItem", "startTreatment", "Start treatment should not be empty!"));
            }

            if (!oldItem.isPresent() && endTreatment == null) {
                bindingResult.addError(new FieldError("newItem", "endTreatment", "End treatment should not be empty!"));
            }

            if (!oldItem.isPresent() && (prescriptionItemDto.getDates() == null || prescriptionItemDto.getDates().isEmpty())) {
                bindingResult.addError(new FieldError("newItem", "date", "Date pattern should not be empty!"));
            }

            if (!oldItem.isPresent() && (prescriptionItemDto.getTimes() == null || prescriptionItemDto.getTimes().isEmpty())) {
                bindingResult.addError(new FieldError("newItem", "time", "Time pattern should not be empty!"));
            }

            if (!oldItem.isPresent() && !therapy.isPresent()) {
                bindingResult.addError(new FieldError("newItem", "therapyId", "Therapy should not be empty!"));
            }
            if (!bindingResult.hasErrors()) {
                log.info("The item's check was successful " + prescriptionItemDto);
            }

        }
    }

    /**
     *
     * @param prescriptionItemDto
     * @param bindingResult
     */
    private void checkDateParser(PrescriptionItemDto prescriptionItemDto, BindingResult bindingResult) {
        if (prescriptionItemDto.getDates() == null || prescriptionItemDto.getDates().isEmpty()) {
            prescriptionItemDto.setDate(null);
        } else {
            try {
                List<String> strings = Arrays.asList(prescriptionItemDto.getDates().split(","));
                List<LocalDate> date = strings.stream().map(LocalDate::parse).collect(Collectors.toList());
                prescriptionItemDto.setDate(date);
            } catch (RuntimeException e) {
                prescriptionItemDto.setDate(null);
                bindingResult.addError(new FieldError(bindingResult.getObjectName(), "date", "Invalid input data format for pattern date"));
            }
        }
    }

    /**
     *
     * @param prescriptionItemDto
     * @param bindingResult
     */
    private void checkTimeParser(PrescriptionItemDto prescriptionItemDto, BindingResult bindingResult) {
        if (prescriptionItemDto.getTimes() == null || prescriptionItemDto.getTimes().isEmpty()) {
            prescriptionItemDto.setTime(null);
        } else {
            try {
                List<String> strings = Arrays.asList(prescriptionItemDto.getTimes().split(","));
                List<LocalTime> time = strings.stream().map(LocalTime::parse).collect(Collectors.toList());
                prescriptionItemDto.setTime(time);
            } catch (RuntimeException e) {
                prescriptionItemDto.setTime(null);
                bindingResult.addError(new FieldError(bindingResult.getObjectName(), "time", "Invalid input data format for pattern time"));
            }
        }
    }

    @Override
    public void checkPatternUpdate(PrescriptionItemDto prescriptionItemDto, BindingResult bindingResult) {
        Optional<PrescriptionItem> prescriptionItem = Optional.empty();
        if (prescriptionItemDto == null) {
            bindingResult.addError(new FieldError("updateItem", "itemID", "Item should not be empty"));
        } else {
            prescriptionItem = prescriptionItemServiceImpl.getPrescriptionItemById(prescriptionItemDto.getItemId());
        }
        if (!prescriptionItem.isPresent()) {
            bindingResult.addError(new FieldError("updateItem", "itemID", "Item does not exist"));
        } else {
            Set<EventDto> eventsById = StreamSupport
                    .stream(eventServiceImpl.getEventDtoByPrescriptionItemId(prescriptionItemDto.getPrescriptionId())
                            .spliterator(), false)
                    .collect(Collectors.toSet());

            List<LocalDateTime> oldDatesOfActEvent = eventsById.stream()
                    .filter(p -> !p.getEventStatus().equals(EventStatus.CANCELLED))
                    .map(EventDto::getDateTime).collect(Collectors.toList());

            List<LocalDateTime> oldDatesOfCompletedEvent = eventsById.stream()
                    .filter(p -> p.getEventStatus().equals(EventStatus.COMPLETED))
                    .map(EventDto::getDateTime).collect(Collectors.toList());

            checkDateParser(prescriptionItemDto, bindingResult);
            checkTimeParser(prescriptionItemDto, bindingResult);

            int numberOfDays;
            int perDay;
            List<LocalDateTime> newEventDateList = new ArrayList<>();

            if (prescriptionItemDto.getDate() == null || prescriptionItemDto.getTime() == null) {
                numberOfDays = 0;
                perDay = 0;
            } else {
                numberOfDays = prescriptionItemDto.getDate().size();
                perDay = prescriptionItemDto.getTime().size();
                newEventDateList = prescriptionItemServiceImpl.createDateTimeListForEvents(prescriptionItemDto.getDate(), prescriptionItemDto.getTime());
            }

            if (numberOfDays * perDay > 0
                    && prescriptionItem.get().getPrescriptionItemStatus().equals(PrescriptionItemStatus.PRESCRIBED)) {

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

            LocalDate endTreatment = prescriptionItem.get().getEndTreatment();
            LocalDate startTreatment = prescriptionItem.get().getStartTreatment();

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
                    && !prescriptionItem.get().getPrescriptionItemStatus().equals(PrescriptionItemStatus.CANCELLED)) {

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
        }
    }
}
