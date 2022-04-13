package com.javaschool.komarov.reha.service;

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
import com.javaschool.komarov.reha.service.impl.EmployeeServiceImpl;
import com.javaschool.komarov.reha.service.impl.EventServiceImpl;
import com.javaschool.komarov.reha.service.impl.PatientServiceImpl;
import com.javaschool.komarov.reha.service.impl.PrescriptionItemServiceImpl;
import com.javaschool.komarov.reha.service.impl.TherapyServiceImpl;
import com.javaschool.komarov.reha.service.impl.ValidationServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ValidationServiceTest {
    @Autowired
    private ValidationServiceImpl validationService;
    @MockBean
    private PatientServiceImpl patientService;
    @MockBean
    private BindingResult bindingResult;
    @MockBean
    private EmployeeServiceImpl employeeService;
    @MockBean
    private EventServiceImpl eventService;
    @MockBean
    private TherapyServiceImpl therapyService;
    @MockBean
    PrescriptionItemServiceImpl prescriptionItemService;

    @Test
    public void checkPatientNull() {
        validationService.checkPatient(null, bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("newPatient", "id", "PatientDto should not be null"));
    }

    @Test
    public void checkPatientNullFields() {
        validationService.checkPatient(new PatientDto(), bindingResult);
        Mockito.verify(bindingResult, Mockito.times(1))
                .addError(new FieldError("newPatient", "firstName", "The first name should not be empty"));
        Mockito.verify(bindingResult, Mockito.times(1))
                .addError(new FieldError("newPatient", "lastName", "The last name should not be empty"));
        Mockito.verify(bindingResult, Mockito.times(1))
                .addError(new FieldError("newPatient", "healthInsurance", "The health insurance should not be empty"));
    }

    @Test
    public void checkPatientEmptyFields() {
        validationService.checkPatient(new PatientDto(1L, "", "", "", null), bindingResult);
        Mockito.verify(bindingResult, Mockito.times(1))
                .addError(new FieldError("newPatient", "firstName", "The first name should not be empty"));
        Mockito.verify(bindingResult, Mockito.times(1))
                .addError(new FieldError("newPatient", "lastName", "The last name should not be empty"));
        Mockito.verify(bindingResult, Mockito.times(1))
                .addError(new FieldError("newPatient", "healthInsurance", "The health insurance should not be empty"));
    }

    @Test
    public void checkPatientWrongVariables() {
        validationService.checkPatient(new PatientDto(1L, "11", "11", "aa", null), bindingResult);
        Mockito.verify(bindingResult, Mockito.times(1))
                .addError(new FieldError("newPatient", "firstName", "The use of numbers and special characters is not allowed"));
        Mockito.verify(bindingResult, Mockito.times(1))
                .addError(new FieldError("newPatient", "lastName", "The use of numbers and special characters is not allowed"));
        Mockito.verify(bindingResult, Mockito.times(1))
                .addError(new FieldError("newPatient", "healthInsurance", "8 characters, numbers only"));
    }

    @Test
    public void checkPatientDuplicate() {
        Mockito.when(patientService.checkPatientInDb("12345678")).thenReturn(true);
        validationService.checkPatient(new PatientDto(1L, "Test", "Test", "12345678", null), bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("newPatient", "healthInsurance", "This patient has already been registered"));
    }

    @Test
    public void checkPatientStatusNullDto() {
        validationService.checkPatientStatus(null, bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("updatedPatient", "id", "Patient does not exist"));
    }

    @Test
    public void checkPatientStatusWrongPatientId() {
        PatientDto patientDto = new PatientDto();
        patientDto.setId(1L);
        Mockito.when(patientService.checkPatientInDb(1L)).thenReturn(false);
        validationService.checkPatientStatus(patientDto, bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("updatedPatient", "id", "Patient does not exist"));
    }

    @Test
    public void checkPatientStatusWrongPatientNullId() {
        PatientDto patientDto = new PatientDto();
        validationService.checkPatientStatus(patientDto, bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("updatedPatient", "id", "Patient does not exist"));
    }

    @Test
    public void checkPatientStatusNull() {
        Mockito.when(patientService.checkPatientInDb(1L)).thenReturn(true);
        validationService.checkPatientStatus(new PatientDto(1L, "Test", "Test", "12345678", null), bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("updatedPatient", "status", "Status should not be empty"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkPatientStatusInvalid() {
        validationService.checkPatientStatus(new PatientDto(1L, "Test", "Test", "12345678", PatientStatus.valueOf("test")), bindingResult);
    }

    @Test
    public void checkPatientStatusActiveEvent() {
        Mockito.when(patientService.hasActiveEvent(1L)).thenReturn(true);
        Mockito.when(patientService.checkPatientInDb(1L)).thenReturn(true);
        validationService.checkPatientStatus(new PatientDto(1L, "Test", "Test", "12345678", PatientStatus.DISCHARGED), bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("updatedPatient", "status", "End active events"));
    }

    @Test
    public void checkAddNullTherapy() {
        validationService.checkTherapy(null, bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("newTherapy", "id", "Therapy should not be null"));
    }

    @Test
    public void checkAddTherapyInvalidName() {
        validationService.checkTherapy(new TherapyDto(1L, "testName1", TherapyType.MEDICINE), bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("newTherapy", "name", "The use of numbers and special characters is not allowed"));
    }

    @Test
    public void checkAddTherapyNullName() {
        validationService.checkTherapy(new TherapyDto(1L, null, TherapyType.MEDICINE), bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("newTherapy", "name", "Therapy name should not be null"));
    }

    @Test
    public void checkAddTherapyNullType() {
        validationService.checkTherapy(new TherapyDto(1L, "test", null), bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("newTherapy", "therapyType", "Invalid therapy status"));
    }

    @Test
    public void checkPrescriptionNullDto() {
        validationService.checkPrescription(null, bindingResult, null, null);
        Mockito.verify(bindingResult)
                .addError(new FieldError("newPrescription", "id", "PrescriptionDto should not be null"));
    }

    @Test
    public void checkPrescriptionNullFields() {
        validationService.checkPrescription(new PrescriptionDto(), bindingResult, null, null);
        Mockito.verify(bindingResult)
                .addError(new FieldError("newPrescription", "diagnosis", "Diagnosis should not be empty"));
        Mockito.verify(bindingResult)
                .addError(new FieldError("newPrescription", "patient", "Patient should not be empty"));
        Mockito.verify(bindingResult)
                .addError(new FieldError("newPrescription", "employee", "Employee should not be empty"));
    }

    @Test
    public void checkPrescriptionDiagnosisEmpty() {
        validationService.checkPrescription(new PrescriptionDto(1L, "", null, null), bindingResult, null, null);
        Mockito.verify(bindingResult)
                .addError(new FieldError("newPrescription", "diagnosis", "Diagnosis should not be empty"));
    }

    @Test
    public void checkPrescriptionEmployeeWrong() {
        Mockito.when(employeeService.EmployeeIsExist(1L)).thenReturn(false);
        validationService.checkPrescription(new PrescriptionDto(1L, "test", null, null), bindingResult, null, null);
        Mockito.verify(bindingResult)
                .addError(new FieldError("newPrescription", "employee", "Employee should not be empty"));
    }

    @Test
    public void checkPrescriptionPatientWrong() {
        Mockito.when(patientService.checkPatientInDb(1L)).thenReturn(false);
        validationService.checkPrescription(new PrescriptionDto(1L, "test", null, null), bindingResult, 1L, null);
        Mockito.verify(bindingResult)
                .addError(new FieldError("newPrescription", "patient", "Patient should not be empty"));
    }

    @Test
    public void checkEventNullDto() {
        validationService.checkEventStatus(null, bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("newEvent", "id", "Event does not exist"));
    }

    @Test
    public void checkEventWrongId() {
        Mockito.when(eventService.eventIsExist(1L)).thenReturn(false);
        validationService.checkEventStatus(new EventDto(1L, null, null, null, null, null, null, null, null, null, null), bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("newEvent", "id", "Event does not exist"));
    }

    @Test
    public void checkEventNullId() {
        validationService.checkEventStatus(new EventDto(), bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("newEvent", "id", "Event does not exist"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkEventStatusInvalid() {
        validationService.checkEventStatus(new EventDto(1L, EventStatus.valueOf("test"), "Test", null, null, null, null, null, null, null, null), bindingResult);
    }

    @Test
    public void checkPrescriptionItemAddNull() {
        validationService.checkPrescriptionItem(null, bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("newItem", "itemId", "Item should not be empty!"));
    }

    @Test
    public void checkPrescriptionItemDoseNullForMedicine() {
        PrescriptionItemDto testDTO = new PrescriptionItemDto();
        testDTO.setTherapyId(1);
        Therapy testTherapy = new Therapy();
        testTherapy.setTherapyType(TherapyType.MEDICINE);
        Mockito.when(therapyService.getTherapyById(1L)).thenReturn(Optional.of(testTherapy));
        validationService.checkPrescriptionItem(testDTO, bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("newItem", "dose", "Invalid dose"));
    }

    @Test
    public void checkPrescriptionItemDoseNonNullForProcedure() {
        PrescriptionItemDto testDTO = new PrescriptionItemDto();
        testDTO.setTherapyId(1);
        testDTO.setDose(1);
        Therapy testTherapy = new Therapy();
        testTherapy.setTherapyType(TherapyType.PROCEDURE);
        Mockito.when(therapyService.getTherapyById(1L)).thenReturn(Optional.of(testTherapy));
        validationService.checkPrescriptionItem(testDTO, bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("newItem", "dose", "Invalid dose"));
    }

    @Test
    public void checkPrescriptionItemNullFields() {
        validationService.checkPrescriptionItem(new PrescriptionItemDto(), bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("newItem", "startTreatment", "Start treatment should not be empty!"));
        Mockito.verify(bindingResult)
                .addError(new FieldError("newItem", "endTreatment", "End treatment should not be empty!"));
        Mockito.verify(bindingResult)
                .addError(new FieldError("newItem", "therapyId", "Therapy should not be empty!"));
        Mockito.verify(bindingResult)
                .addError(new FieldError("newItem", "date", "Date pattern should not be empty!"));
        Mockito.verify(bindingResult)
                .addError(new FieldError("newItem", "time", "Time pattern should not be empty!"));
    }

    @Test
    public void checkPrescriptionItemStartAfterEndTreatment() {
        PrescriptionItemDto testDTO = new PrescriptionItemDto();
        testDTO.setStartTreatment(LocalDate.now());
        testDTO.setEndTreatment(LocalDate.now().minusDays(1));
        testDTO.setTime(new ArrayList<>());
        testDTO.setDate(new ArrayList<>());
        validationService.checkPrescriptionItem(testDTO, bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("newItem", "startTreatment", "Illegal date statement!"));
        Mockito.verify(bindingResult)
                .addError(new FieldError("newItem", "endTreatment", "Illegal date statement!"));
    }

    @Test
    public void checkPrescriptionItemStartBeforeEndTreatment() {
        PrescriptionItemDto testDTO = new PrescriptionItemDto();
        testDTO.setStartTreatment(LocalDate.now().plusDays(1));
        testDTO.setEndTreatment(LocalDate.now());
        testDTO.setTime(new ArrayList<>());
        testDTO.setDate(new ArrayList<>());
        validationService.checkPrescriptionItem(testDTO, bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("newItem", "startTreatment", "Illegal date statement!"));
        Mockito.verify(bindingResult)
                .addError(new FieldError("newItem", "endTreatment", "Illegal date statement!"));
    }

    @Test
    public void checkPrescriptionItemStartBeforeToday() {
        PrescriptionItemDto testDTO = new PrescriptionItemDto();
        testDTO.setStartTreatment(LocalDate.now().minusDays(1));
        testDTO.setEndTreatment(LocalDate.now());
        testDTO.setTime(new ArrayList<>());
        testDTO.setDate(new ArrayList<>());
        validationService.checkPrescriptionItem(testDTO, bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("newItem", "startTreatment", "Illegal date statement!"));
    }

    @Test
    public void checkPrescriptionItemStartTreatmentAfterEvent() {
        PrescriptionItemDto testDTO = new PrescriptionItemDto();
        testDTO.setStartTreatment(LocalDate.now().plusDays(1));
        testDTO.setEndTreatment(LocalDate.now().plusDays(2));
        LocalDate eventDate = LocalDate.now();
        LocalTime eventTime = LocalTime.now();
        List<LocalDate> dateList = new ArrayList<>();
        List<LocalTime> timeList = new ArrayList<>();
        dateList.add(eventDate);
        timeList.add(eventTime);
        testDTO.setDate(dateList);
        testDTO.setTime(timeList);
        validationService.checkPrescriptionItem(testDTO, bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("newItem", "startTreatment", "Illegal date statement!"));
    }

    @Test
    public void checkPrescriptionItemEndTreatmentAfterEvent() {
        PrescriptionItemDto testItem = new PrescriptionItemDto();
        testItem.setStartTreatment(LocalDate.now().plusDays(1));
        testItem.setEndTreatment(LocalDate.now().plusDays(2));
        LocalDate eventDate = LocalDate.now().plusDays(3);
        LocalTime eventTime = LocalTime.now();
        List<LocalDate> dateList = new ArrayList<>();
        List<LocalTime> timeList = new ArrayList<>();
        dateList.add(eventDate);
        timeList.add(eventTime);
        testItem.setDate(dateList);
        testItem.setTime(timeList);
        validationService.checkPrescriptionItem(testItem, bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("newItem", "endTreatment", "Illegal date statement!"));
    }

    @Test
    public void checkPatternUpdateNullItem() {
        validationService.checkPatternUpdate(null, bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("updateItem", "itemID", "Item should not be empty"));
    }

    @Test
    public void checkPatternUpdateInvalidItemId() {
        PrescriptionItemDto testItem = new PrescriptionItemDto();
        testItem.setItemId(1L);
        Mockito.when(prescriptionItemService.getPrescriptionItemById(1L)).thenReturn(Optional.empty());
        validationService.checkPatternUpdate(testItem, bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("updateItem", "itemID", "Item does not exist"));
    }

    @Test
    public void checkPatternUpdateEventBeforeToday() {
        PrescriptionItemDto testItem = new PrescriptionItemDto();
        testItem.setItemId(1L);
        PrescriptionItem item = new PrescriptionItem();
        item.setPrescriptionItemStatus(PrescriptionItemStatus.PRESCRIBED);
        Iterable<EventDto> events = new ArrayList<>();
        String eventDate = LocalDate.now().minusDays(1).toString();
        String eventTime = LocalTime.now().toString();
        testItem.setDates(eventDate);
        testItem.setTimes(eventTime);
        Mockito.when(prescriptionItemService.getPrescriptionItemById(1L)).thenReturn(Optional.of(item));
        Mockito.when(eventService.getEventDtoByPrescriptionItemId(testItem.getPrescriptionId())).thenReturn(events);
        validationService.checkPatternUpdate(testItem, bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("updateItem", "time", "It is not possible to assign an event earlier than the current moment"));
    }

    @Test
    public void checkPatternUpdateStartAfterEndTreatment() {
        PrescriptionItemDto testItem = new PrescriptionItemDto();
        testItem.setItemId(1L);
        PrescriptionItem item = new PrescriptionItem();
        item.setPrescriptionItemStatus(PrescriptionItemStatus.PRESCRIBED);
        Iterable<EventDto> events = new ArrayList<>();
        testItem.setStartTreatment(LocalDate.now().plusDays(1));
        testItem.setEndTreatment(LocalDate.now());
        Mockito.when(prescriptionItemService.getPrescriptionItemById(1L)).thenReturn(Optional.of(item));
        Mockito.when(eventService.getEventDtoByPrescriptionItemId(testItem.getPrescriptionId())).thenReturn(events);
        validationService.checkPatternUpdate(testItem, bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("updateItem", "startTreatment", "Illegal date statement!"));
        Mockito.verify(bindingResult)
                .addError(new FieldError("updateItem", "endTreatment", "Illegal date statement!"));
    }

    @Test
    public void checkPatternUpdateStartAfterMinEventDate() {
        PrescriptionItemDto testItem = new PrescriptionItemDto();
        testItem.setItemId(1L);
        PrescriptionItem item = new PrescriptionItem();
        item.setPrescriptionItemStatus(PrescriptionItemStatus.PRESCRIBED);
        Iterable<EventDto> events = new ArrayList<>();
        testItem.setStartTreatment(LocalDate.now().plusDays(1));
        testItem.setEndTreatment(LocalDate.now().plusDays(2));
        String eventDate = LocalDate.now().toString();
        String eventTime = LocalTime.now().toString();
        testItem.setDates(eventDate);
        testItem.setTimes(eventTime);
        Mockito.when(prescriptionItemService.getPrescriptionItemById(1L)).thenReturn(Optional.of(item));
        Mockito.when(eventService.getEventDtoByPrescriptionItemId(testItem.getPrescriptionId())).thenReturn(events);
        validationService.checkPatternUpdate(testItem, bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("updateItem", "startTreatment", "Illegal date statement, check date and time pattern"));
    }

    @Test
    public void checkPatternUpdateEndTreatmentBeforeMaxEventDate() {
        PrescriptionItemDto testItem = new PrescriptionItemDto();
        testItem.setItemId(1L);
        PrescriptionItem item = new PrescriptionItem();
        item.setPrescriptionItemStatus(PrescriptionItemStatus.PRESCRIBED);
        Iterable<EventDto> events = new ArrayList<>();
        testItem.setStartTreatment(LocalDate.now().plusDays(1));
        testItem.setEndTreatment(LocalDate.now().plusDays(2));
        String eventDate = LocalDate.now().plusDays(3).toString();
        String eventTime = LocalTime.now().toString();
        testItem.setDates(eventDate);
        testItem.setTimes(eventTime);
        Mockito.when(prescriptionItemService.getPrescriptionItemById(1L)).thenReturn(Optional.of(item));
        Mockito.when(eventService.getEventDtoByPrescriptionItemId(testItem.getPrescriptionId())).thenReturn(events);
        validationService.checkPatternUpdate(testItem, bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("updateItem", "endTreatment", "Illegal date statement, check date and time pattern"));
    }

    @Test
    public void checkUpdateCancelledItem() {
        PrescriptionItemDto testItem = new PrescriptionItemDto();
        testItem.setItemId(1L);
        PrescriptionItem item = new PrescriptionItem();
        Therapy therapy = new Therapy();
        item.setTherapy(therapy);
        item.setPrescriptionItemStatus(PrescriptionItemStatus.CANCELLED);
        Iterable<EventDto> events = new ArrayList<>();
        testItem.setStartTreatment(LocalDate.now().plusDays(1));
        testItem.setEndTreatment(LocalDate.now().plusDays(2));
        Mockito.when(prescriptionItemService.getPrescriptionItemById(1L)).thenReturn(Optional.of(item));
        Mockito.when(eventService.getEventDtoByPrescriptionItemId(testItem.getPrescriptionId())).thenReturn(events);
        validationService.checkPrescriptionItem(testItem, bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("updateItem", "prescriptionItemStatus", "Changes to the cancelled prescription are not available"));

    }
}
