package com.javaschool.komarov.reha.repository;

import com.javaschool.komarov.reha.model.PrescriptionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionItemRepo extends JpaRepository<PrescriptionItem, Long> {
    Iterable<PrescriptionItem> findPrescriptionItemByPrescriptionId(Long id);
}
