package com.javaschool.komarov.reha.service;

import com.javaschool.komarov.reha.mapper.PatientMapperImpl;
import com.javaschool.komarov.reha.model.PatientStatus;
import com.javaschool.komarov.reha.model.dto.PatientDto;
import com.javaschool.komarov.reha.model.entity.Patient;
import com.javaschool.komarov.reha.repository.PatientRepo;
import com.javaschool.komarov.reha.service.impl.PatientServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PatientServiceTest {
    @Autowired
    private PatientServiceImpl patientService;
    @MockBean
    private PatientRepo patientRepo;
    @MockBean
    private PatientMapperImpl patientMapper;

    @Test
    public void checkPatientInDb() {
        Optional<Patient> patient = Optional.empty();
        Mockito.when(patientRepo.getPatientByHealthInsurance("test")).thenReturn(patient);
        Assert.assertFalse(patientService.checkPatientInDb("test"));
        Mockito.verify(patientRepo, Mockito.times(1)).getPatientByHealthInsurance("test");
    }

    @Test
    public void testCheckPatientInDb() {
        Optional<Patient> patient = Optional.empty();
        Mockito.when(patientRepo.findById(1L)).thenReturn(patient);
        Assert.assertFalse(patientService.checkPatientInDb(1L));
        Mockito.verify(patientRepo, Mockito.times(1)).findById(1L);
    }

    @Test
    public void savePatient() {
        PatientDto patientDto = new PatientDto();
        Patient patient = new Patient();
        Mockito.when(patientMapper.toModel(patientDto)).thenReturn(patient);
        patientService.savePatient(patientDto);
        Mockito.verify(patientRepo, Mockito.times(1)).save(patient);
    }

    @Test
    public void getAllPatientsDTO() {
        patientService.getAllPatientsDTO();
        Iterable<Patient> patients = new ArrayList<>();
        Mockito.when(patientRepo.findAll()).thenReturn(patients);
        Mockito.verify(patientRepo, Mockito.times(1)).findAll();
        Mockito.verify(patientMapper, Mockito.times(1)).toDTOList(patients);
    }

    @Test
    public void getPatientDTOById() {
        Patient patient = new Patient();
        PatientDto patientDto = new PatientDto();
        Mockito.when(patientRepo.findById(1L)).thenReturn(Optional.of(patient));
        Mockito.when(patientMapper.toDTO(patient)).thenReturn(patientDto);
        patientService.getPatientDTOById(1L);
        Mockito.verify(patientRepo, Mockito.times(1)).findById(1L);
        Mockito.verify(patientMapper, Mockito.times(1)).toDTO(patient);
    }

    @Test
    public void updatePatientStatusWithActiveEvent() {
        PatientDto patientDto = new PatientDto();
        patientDto.setId(1L);
        Set<Long> patientsWithActiveEvent = new HashSet<>();
        patientsWithActiveEvent.add(1L);
        Mockito.when(patientRepo.findAllPatientIdWithActiveEvent()).thenReturn(patientsWithActiveEvent);
        patientService.updatePatientStatus(patientDto);
        Mockito.verify(patientRepo, Mockito.times(0)).updatePatientStatus(1L, PatientStatus.IS_TREATED);
    }

    @Test
    public void setStatusIsTreated() {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setStatus(PatientStatus.UNDEFINED);
        Set<Long> patientWithActiveEvent = new HashSet<>();
        PatientDto patientDto = new PatientDto();
        patientDto.setId(patient.getId());
        patientDto.setStatus(PatientStatus.IS_TREATED);
        Mockito.when(patientMapper.toDTO(patient)).thenReturn(patientDto);
        Mockito.when(patientRepo.findAllPatientIdWithActiveEvent()).thenReturn(patientWithActiveEvent);
        patientService.setStatusIsTreated(patient);
        Mockito.verify(patientRepo, Mockito.times(1)).updatePatientStatus(1L, PatientStatus.IS_TREATED);
    }

    @Test
    public void setStatusIsTreatedInvalid() {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setStatus(PatientStatus.IS_TREATED);
        patientService.setStatusIsTreated(patient);
        Mockito.verify(patientRepo, Mockito.times(0)).updatePatientStatus(1L, PatientStatus.IS_TREATED);
    }

    @Test
    public void getPatientIdWithActiveEvent() {
        Set<Long> patientWithActiveEvent = new HashSet<>();
        Mockito.when(patientRepo.findAllPatientIdWithActiveEvent()).thenReturn(patientWithActiveEvent);
        patientService.getPatientIdWithActiveEvent();
        Mockito.verify(patientRepo, Mockito.times(1)).findAllPatientIdWithActiveEvent();
    }

    @Test
    public void hasActiveEvent() {
        Set<Long> patientWithActiveEvent = new HashSet<>();
        patientWithActiveEvent.add(1L);
        Mockito.when(patientRepo.findAllPatientIdWithActiveEvent()).thenReturn(patientWithActiveEvent);
        Assert.assertTrue(patientService.hasActiveEvent(1L));
        Mockito.verify(patientRepo, Mockito.times(1)).findAllPatientIdWithActiveEvent();
    }

    @Test
    public void getPatientById() {
        patientService.getPatientById(1L);
        Mockito.verify(patientRepo, Mockito.times(1)).findById(1L);
    }
}