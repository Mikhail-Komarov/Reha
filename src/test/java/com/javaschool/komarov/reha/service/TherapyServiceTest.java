package com.javaschool.komarov.reha.service;

import com.javaschool.komarov.reha.mapper.TherapyMapperImpl;
import com.javaschool.komarov.reha.model.dto.TherapyDto;
import com.javaschool.komarov.reha.model.entity.Therapy;
import com.javaschool.komarov.reha.repository.TherapyRepo;
import com.javaschool.komarov.reha.service.impl.TherapyServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TherapyServiceTest {
    @Autowired
    private TherapyServiceImpl therapyService;
    @MockBean
    private TherapyRepo therapyRepo;
    @MockBean
    private TherapyMapperImpl therapyMapper;

    @Test
    public void getAllTherapiesDTO() {
        therapyService.getAllTherapiesDTO();
        Iterable<Therapy> therapies = new ArrayList<>();
        Mockito.when(therapyRepo.findAll()).thenReturn(therapies);
        Mockito.verify(therapyRepo, Mockito.times(1)).findAll();
        Mockito.verify(therapyMapper, Mockito.times(1)).toDTOList(therapies);
    }

    @Test
    public void getTherapyById() {
        therapyService.getTherapyById(1L);
        Mockito.verify(therapyRepo, Mockito.times(1)).findById(1L);
    }

    @Test
    public void saveTherapy() {
        TherapyDto therapyDto = new TherapyDto();
        therapyService.saveTherapy(therapyDto);
        Mockito.verify(therapyMapper, Mockito.times(1)).toModel(therapyDto);
        Mockito.verify(therapyRepo, Mockito.times(1)).save(therapyMapper.toModel(therapyDto));
    }
}
