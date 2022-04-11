package com.javaschool.komarov.reha.service.api;

import com.javaschool.komarov.reha.model.dto.PrescriptionDto;
import com.javaschool.komarov.reha.model.entity.Prescription;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface PrescriptionService {
    Iterable<PrescriptionDto> getPrescriptionsDTOByPatientId(Long id);

    void savePrescription(PrescriptionDto prescriptionDto, UserDetails userDetails, Long patientId);

    PrescriptionDto getPrescriptionDTOById(Long id);

    Optional<Prescription> getPrescriptionById(Long id);

}
