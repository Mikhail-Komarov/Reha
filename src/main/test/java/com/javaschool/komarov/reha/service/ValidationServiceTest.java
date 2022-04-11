package com.javaschool.komarov.reha.service;

import com.javaschool.komarov.reha.model.EventStatus;
import com.javaschool.komarov.reha.model.PatientStatus;
import com.javaschool.komarov.reha.model.TherapyType;
import com.javaschool.komarov.reha.model.dto.EventDto;
import com.javaschool.komarov.reha.model.dto.PatientDto;
import com.javaschool.komarov.reha.model.dto.PrescriptionDto;
import com.javaschool.komarov.reha.model.dto.TherapyDto;
import com.javaschool.komarov.reha.service.impl.EmployeeServiceImpl;
import com.javaschool.komarov.reha.service.impl.EventServiceImpl;
import com.javaschool.komarov.reha.service.impl.PatientServiceImpl;
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
        validationService.checkTherapy(new TherapyDto(1L,"testName1", TherapyType.MEDICINE), bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("newTherapy", "name", "The use of numbers and special characters is not allowed"));
    }

    @Test
    public void checkAddTherapyNullName() {
        validationService.checkTherapy(new TherapyDto(1L,null, TherapyType.MEDICINE), bindingResult);
        Mockito.verify(bindingResult)
                .addError(new FieldError("newTherapy", "name", "Therapy name should not be null"));
    }

    @Test
    public void checkAddTherapyNullType() {
        validationService.checkTherapy(new TherapyDto(1L,"test", null), bindingResult);
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
    public void checkPrescriptionItem() {
    }

    @Test
    public void checkPatternUpdate() {
    }
}

