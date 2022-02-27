package com.javaschool.komarov.reha.repository;

import com.javaschool.komarov.reha.model.PrescriptionItem;
import org.springframework.data.repository.CrudRepository;

public interface PrescriptionItemRepo extends CrudRepository<PrescriptionItem, Long> {
}
