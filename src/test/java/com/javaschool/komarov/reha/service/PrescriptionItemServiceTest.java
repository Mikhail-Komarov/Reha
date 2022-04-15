package com.javaschool.komarov.reha.service;

import com.javaschool.komarov.reha.mapper.PrescriptionItemMapperImpl;
import com.javaschool.komarov.reha.model.dto.PrescriptionItemDto;
import com.javaschool.komarov.reha.model.entity.PrescriptionItem;
import com.javaschool.komarov.reha.repository.PrescriptionItemRepo;
import com.javaschool.komarov.reha.service.impl.PrescriptionItemServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PrescriptionItemServiceTest {
    @Autowired
    private PrescriptionItemServiceImpl prescriptionItemService;
    @MockBean
    private PrescriptionItemMapperImpl prescriptionItemMapper;
    @MockBean
    private PrescriptionItemRepo prescriptionItemRepo;

    @Test
    public void getPrescriptionItemDTOByPrescriptionID() {
        prescriptionItemService.getPrescriptionItemDTOByPrescriptionID(1L);
        Mockito.verify(prescriptionItemRepo, Mockito.times(1)).findPrescriptionItemByPrescriptionId(1L);
    }

    @Test
    public void getPrescriptionItemDTOById() {
        PrescriptionItem prescriptionItem = new PrescriptionItem();
        PrescriptionItemDto prescriptionItemDto = new PrescriptionItemDto();
        Mockito.when(prescriptionItemRepo.findById(1L)).thenReturn(Optional.of(prescriptionItem));
        Mockito.when(prescriptionItemMapper.toDTO(prescriptionItem)).thenReturn(prescriptionItemDto);
        prescriptionItemService.getPrescriptionItemDTOById(1L);
        Mockito.verify(prescriptionItemRepo, Mockito.times(1)).findById(1L);
    }

    @Test
    public void getPrescriptionItemById() {
        prescriptionItemService.getPrescriptionItemById(1L);
        Mockito.verify(prescriptionItemRepo, Mockito.times(1)).findById(1L);
    }

    @Test
    public void createDateTimeListForEvents() {
        List<LocalDate> dateList = new ArrayList<>();
        List<LocalTime> timeList = new ArrayList<>();
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        dateList.add(date);
        timeList.add(time);
        LocalDateTime dateTime = date.atTime(time);
        Assert.assertEquals(dateTime,prescriptionItemService.createDateTimeListForEvents(dateList, timeList).get(0));
    }
}