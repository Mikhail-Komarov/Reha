package com.javaschool.komarov.reha.mapper;

import com.javaschool.komarov.reha.model.dto.EventDto;
import com.javaschool.komarov.reha.model.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = PrescriptionItemMapper.class)
public interface EventMapper {
    @Mapping(target = "patientName", expression = "java(getPatientFullName(model))")
    @Mapping(target = "patientInsurance", source = "model.prescriptionItem.prescription.patient.healthInsurance")
    @Mapping(target = "therapy", source = "model.prescriptionItem.therapy.name")
    @Mapping(target = "therapyDose", source = "model.prescriptionItem.dose")
    @Mapping(target = "doctorName", expression = "java(getDoctorFullName(model))")
    @Mapping(target = "executorName", expression = "java(getExecutorFullName(model))")
    EventDto toDTO(Event model);

    Event toModel(EventDto dto);

    Iterable<EventDto> toDTOList(Iterable<Event> models);

    default String getPatientFullName(Event model) {
        return model.getPrescriptionItem().getPrescription().getPatient().getFirstName()
                + " " + model.getPrescriptionItem().getPrescription().getPatient().getLastName();
    }

    default String getDoctorFullName(Event model) {
        return model.getPrescriptionItem().getEmployee().getFirstName()
                + " " + model.getPrescriptionItem().getEmployee().getLastName();
    }

    default String getExecutorFullName(Event model) {
        if (model.getEmployee() != null) {
            return model.getEmployee().getFirstName()
                    + " " + model.getEmployee().getLastName();
        } else {
            return null;
        }
    }
}
