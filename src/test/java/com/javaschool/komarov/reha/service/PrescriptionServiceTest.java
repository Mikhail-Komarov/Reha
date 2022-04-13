package com.javaschool.komarov.reha.service;

import com.javaschool.komarov.reha.mapper.PrescriptionMapperImpl;
import com.javaschool.komarov.reha.model.dto.PrescriptionDto;
import com.javaschool.komarov.reha.model.entity.Prescription;
import com.javaschool.komarov.reha.repository.PrescriptionRepo;
import com.javaschool.komarov.reha.service.impl.PrescriptionServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PrescriptionServiceTest {
    @Autowired
    private PrescriptionServiceImpl prescriptionService;
    @MockBean
    private PrescriptionRepo prescriptionRepo;
    @MockBean
    private PrescriptionMapperImpl prescriptionMapper;

    @Test
    public void getPrescriptionsDTOByPatientId() {
        prescriptionService.getPrescriptionsDTOByPatientId(1L);
        Mockito.verify(prescriptionRepo, Mockito.times(1)).findByPatientId(1L);
    }

    @Test
    public void getPrescriptionDTOById() {
        Prescription prescription = new Prescription();
        PrescriptionDto prescriptionDto = new PrescriptionDto();
        Mockito.when(prescriptionRepo.findById(1L)).thenReturn(Optional.of(prescription));
        Mockito.when(prescriptionMapper.toDTO(prescription)).thenReturn(prescriptionDto);
        prescriptionService.getPrescriptionDTOById(1L);
        Mockito.verify(prescriptionRepo, Mockito.times(1)).findById(1L);
    }

    @Test
    public void getPrescriptionById() {
        Prescription prescription = new Prescription();
        Mockito.when(prescriptionRepo.findById(1L)).thenReturn(Optional.of(prescription));
        prescriptionService.getPrescriptionById(1L);
        Mockito.verify(prescriptionRepo, Mockito.times(1)).findById(1L);
    }
}