package com.javaschool.komarov.reha.service;

import com.javaschool.komarov.reha.dto.EmployeeDto;
import com.javaschool.komarov.reha.dto.PatientDto;
import com.javaschool.komarov.reha.dto.PrescriptionDto;
import com.javaschool.komarov.reha.mapper.PrescriptionMapper;
import com.javaschool.komarov.reha.repository.PrescriptionRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class PrescriptionService {
    private final PrescriptionMapper prescriptionMapper;
    private final EmployeeService employeeService;
    private final PatientService patientService;
    private final PrescriptionRepo prescriptionRepo;

    public PrescriptionService(PrescriptionMapper prescriptionMapper,
                               EmployeeService employeeService, PatientService patientService,
                               PrescriptionRepo prescriptionRepo) {
        this.prescriptionMapper = prescriptionMapper;
        this.employeeService = employeeService;
        this.patientService = patientService;
        this.prescriptionRepo = prescriptionRepo;
    }

    public Iterable<PrescriptionDto> getPrescriptionByPatientId(Long id) {
        return prescriptionMapper.toDTOList(prescriptionRepo.findByPatientId(id));
    }

    public void savePrescription(PrescriptionDto prescriptionDto, UserDetails userDetails, Long patientId) {

        EmployeeDto employee = employeeService.getEmployeeByLogin(userDetails.getUsername());
        PatientDto patientDto = patientService.getPatientById(patientId);
        prescriptionDto.setEmployee(employee);
        prescriptionDto.setPatient(patientDto);
        prescriptionRepo.save(prescriptionMapper.toModel(prescriptionDto));
    }

    public PrescriptionDto getPrescriptionById(Long id) {
        if (prescriptionRepo.findById(id).isPresent()) {
            return prescriptionMapper.toDTO(prescriptionRepo.findById(id).get());
        } else {
            return null;
        }
    }

}

