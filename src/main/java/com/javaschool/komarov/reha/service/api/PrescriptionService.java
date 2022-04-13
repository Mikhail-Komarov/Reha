package com.javaschool.komarov.reha.service.api;

import com.javaschool.komarov.reha.model.dto.PrescriptionDto;
import com.javaschool.komarov.reha.model.entity.Prescription;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface PrescriptionService {
    /**
     * Method to get all prescriptionsDTO by patient id
     *
     * @param id patient id
     * @return prescriptionsDTO
     */
    Iterable<PrescriptionDto> getPrescriptionsDTOByPatientId(Long id);

    /**
     * Method to save prescription
     *
     * @param prescriptionDto prescriptionDTO
     * @param userDetails     employee info
     * @param patientId       patient id
     */
    void savePrescription(PrescriptionDto prescriptionDto, UserDetails userDetails, Long patientId);

    /**
     * Method to get prescriptionDTO by id
     *
     * @param id prescription id
     * @return prescriptionDTO
     */
    PrescriptionDto getPrescriptionDTOById(Long id);

    /**
     * Method to get prescription by id
     *
     * @param id prescription id
     * @return prescription
     */
    Optional<Prescription> getPrescriptionById(Long id);

}
