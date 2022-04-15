package com.javaschool.komarov.reha.service.impl;

import com.javaschool.komarov.reha.exception.PrescriptionNotFoundException;
import com.javaschool.komarov.reha.mapper.PrescriptionMapper;
import com.javaschool.komarov.reha.model.dto.PrescriptionDto;
import com.javaschool.komarov.reha.model.entity.Prescription;
import com.javaschool.komarov.reha.repository.PrescriptionRepo;
import com.javaschool.komarov.reha.service.api.PrescriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class PrescriptionServiceImpl implements PrescriptionService {
    private final PrescriptionMapper prescriptionMapper;
    private final EmployeeServiceImpl employeeServiceImpl;
    private final PatientServiceImpl patientServiceImpl;
    private final PrescriptionRepo prescriptionRepo;

    @Override
    public Iterable<PrescriptionDto> getPrescriptionsDTOByPatientId(Long id) {
        return prescriptionMapper.toDTOList(prescriptionRepo.findByPatientId(id));
    }

    @Override
    @Transactional
    public void savePrescription(PrescriptionDto prescriptionDto, UserDetails userDetails, Long patientId) {
        Prescription prescription = prescriptionMapper.toModel(prescriptionDto);
        patientServiceImpl.getPatientById(patientId).ifPresent(prescription::setPatient);
        employeeServiceImpl.getEmployeeByLogin(userDetails.getUsername()).ifPresent(prescription::setEmployee);
        prescription.setAppointmentDate(LocalDate.now());
        prescriptionRepo.save(prescription);
        log.info("Dr. " + prescription.getEmployee().getLastName() + " added a prescription for the patient id " + patientId);
    }

    @Override
    public PrescriptionDto getPrescriptionDTOById(Long id) {
        Optional<Prescription> prescription = prescriptionRepo.findById(id);
        return prescription.map(prescriptionMapper::toDTO).orElseThrow(PrescriptionNotFoundException::new);
    }

    @Override
    public Optional<Prescription> getPrescriptionById(Long id) {
        return prescriptionRepo.findById(id);
    }

}

