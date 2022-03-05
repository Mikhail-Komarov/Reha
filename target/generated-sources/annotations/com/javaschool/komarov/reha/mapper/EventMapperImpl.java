package com.javaschool.komarov.reha.mapper;

import com.javaschool.komarov.reha.dto.EmployeeDto;
import com.javaschool.komarov.reha.dto.EventDto;
import com.javaschool.komarov.reha.dto.PatientDto;
import com.javaschool.komarov.reha.dto.PrescriptionDto;
import com.javaschool.komarov.reha.dto.PrescriptionItemDto;
import com.javaschool.komarov.reha.dto.TherapyDto;
import com.javaschool.komarov.reha.model.Employee;
import com.javaschool.komarov.reha.model.Event;
import com.javaschool.komarov.reha.model.Patient;
import com.javaschool.komarov.reha.model.Prescription;
import com.javaschool.komarov.reha.model.PrescriptionItem;
import com.javaschool.komarov.reha.model.Therapy;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-03-05T19:36:29+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_312 (BellSoft)"
)
@Component
public class EventMapperImpl implements EventMapper {

    @Override
    public EventDto toDTO(Event model) {
        if ( model == null ) {
            return null;
        }

        EventDto eventDto = new EventDto();

        eventDto.setId( model.getId() );
        eventDto.setEventStatus( model.getEventStatus() );
        eventDto.setCancellationReason( model.getCancellationReason() );
        eventDto.setDateTime( model.getDateTime() );
        eventDto.setPrescriptionItem( prescriptionItemToPrescriptionItemDto( model.getPrescriptionItem() ) );

        return eventDto;
    }

    @Override
    public Event toModel(EventDto dto) {
        if ( dto == null ) {
            return null;
        }

        Event event = new Event();

        event.setId( dto.getId() );
        event.setEventStatus( dto.getEventStatus() );
        event.setCancellationReason( dto.getCancellationReason() );
        event.setDateTime( dto.getDateTime() );
        event.setPrescriptionItem( prescriptionItemDtoToPrescriptionItem( dto.getPrescriptionItem() ) );

        return event;
    }

    protected EmployeeDto employeeToEmployeeDto(Employee employee) {
        if ( employee == null ) {
            return null;
        }

        EmployeeDto employeeDto = new EmployeeDto();

        employeeDto.setId( employee.getId() );
        employeeDto.setFirstName( employee.getFirstName() );
        employeeDto.setLastName( employee.getLastName() );
        employeeDto.setLogin( employee.getLogin() );
        employeeDto.setRole( employee.getRole() );

        return employeeDto;
    }

    protected TherapyDto therapyToTherapyDto(Therapy therapy) {
        if ( therapy == null ) {
            return null;
        }

        TherapyDto therapyDto = new TherapyDto();

        therapyDto.setId( therapy.getId() );
        therapyDto.setName( therapy.getName() );
        therapyDto.setTherapyType( therapy.getTherapyType() );

        return therapyDto;
    }

    protected PatientDto patientToPatientDto(Patient patient) {
        if ( patient == null ) {
            return null;
        }

        PatientDto patientDto = new PatientDto();

        patientDto.setId( patient.getId() );
        patientDto.setFirstName( patient.getFirstName() );
        patientDto.setLastName( patient.getLastName() );
        patientDto.setHealthInsurance( patient.getHealthInsurance() );
        patientDto.setStatus( patient.getStatus() );

        return patientDto;
    }

    protected PrescriptionDto prescriptionToPrescriptionDto(Prescription prescription) {
        if ( prescription == null ) {
            return null;
        }

        PrescriptionDto prescriptionDto = new PrescriptionDto();

        prescriptionDto.setId( prescription.getId() );
        prescriptionDto.setDiagnosis( prescription.getDiagnosis() );
        prescriptionDto.setPatient( patientToPatientDto( prescription.getPatient() ) );
        prescriptionDto.setEmployee( employeeToEmployeeDto( prescription.getEmployee() ) );

        return prescriptionDto;
    }

    protected PrescriptionItemDto prescriptionItemToPrescriptionItemDto(PrescriptionItem prescriptionItem) {
        if ( prescriptionItem == null ) {
            return null;
        }

        PrescriptionItemDto prescriptionItemDto = new PrescriptionItemDto();

        prescriptionItemDto.setId( prescriptionItem.getId() );
        prescriptionItemDto.setDose( prescriptionItem.getDose() );
        prescriptionItemDto.setStartTreatment( prescriptionItem.getStartTreatment() );
        prescriptionItemDto.setEndTreatment( prescriptionItem.getEndTreatment() );
        prescriptionItemDto.setTimePattern( prescriptionItem.getTimePattern() );
        prescriptionItemDto.setPrescriptionItemStatus( prescriptionItem.getPrescriptionItemStatus() );
        prescriptionItemDto.setCancellationReason( prescriptionItem.getCancellationReason() );
        prescriptionItemDto.setEmployee( employeeToEmployeeDto( prescriptionItem.getEmployee() ) );
        prescriptionItemDto.setTherapy( therapyToTherapyDto( prescriptionItem.getTherapy() ) );
        prescriptionItemDto.setPrescription( prescriptionToPrescriptionDto( prescriptionItem.getPrescription() ) );

        return prescriptionItemDto;
    }

    protected Patient patientDtoToPatient(PatientDto patientDto) {
        if ( patientDto == null ) {
            return null;
        }

        Patient patient = new Patient();

        patient.setId( patientDto.getId() );
        patient.setFirstName( patientDto.getFirstName() );
        patient.setLastName( patientDto.getLastName() );
        patient.setHealthInsurance( patientDto.getHealthInsurance() );
        patient.setStatus( patientDto.getStatus() );

        return patient;
    }

    protected Employee employeeDtoToEmployee(EmployeeDto employeeDto) {
        if ( employeeDto == null ) {
            return null;
        }

        Employee employee = new Employee();

        employee.setId( employeeDto.getId() );
        employee.setFirstName( employeeDto.getFirstName() );
        employee.setLastName( employeeDto.getLastName() );
        employee.setLogin( employeeDto.getLogin() );
        employee.setRole( employeeDto.getRole() );

        return employee;
    }

    protected Prescription prescriptionDtoToPrescription(PrescriptionDto prescriptionDto) {
        if ( prescriptionDto == null ) {
            return null;
        }

        Prescription prescription = new Prescription();

        prescription.setId( prescriptionDto.getId() );
        prescription.setDiagnosis( prescriptionDto.getDiagnosis() );
        prescription.setPatient( patientDtoToPatient( prescriptionDto.getPatient() ) );
        prescription.setEmployee( employeeDtoToEmployee( prescriptionDto.getEmployee() ) );

        return prescription;
    }

    protected Therapy therapyDtoToTherapy(TherapyDto therapyDto) {
        if ( therapyDto == null ) {
            return null;
        }

        Therapy therapy = new Therapy();

        therapy.setId( therapyDto.getId() );
        therapy.setName( therapyDto.getName() );
        therapy.setTherapyType( therapyDto.getTherapyType() );

        return therapy;
    }

    protected PrescriptionItem prescriptionItemDtoToPrescriptionItem(PrescriptionItemDto prescriptionItemDto) {
        if ( prescriptionItemDto == null ) {
            return null;
        }

        PrescriptionItem prescriptionItem = new PrescriptionItem();

        prescriptionItem.setId( prescriptionItemDto.getId() );
        prescriptionItem.setDose( prescriptionItemDto.getDose() );
        prescriptionItem.setStartTreatment( prescriptionItemDto.getStartTreatment() );
        prescriptionItem.setEndTreatment( prescriptionItemDto.getEndTreatment() );
        prescriptionItem.setTimePattern( prescriptionItemDto.getTimePattern() );
        prescriptionItem.setPrescription( prescriptionDtoToPrescription( prescriptionItemDto.getPrescription() ) );
        prescriptionItem.setTherapy( therapyDtoToTherapy( prescriptionItemDto.getTherapy() ) );
        prescriptionItem.setEmployee( employeeDtoToEmployee( prescriptionItemDto.getEmployee() ) );
        prescriptionItem.setPrescriptionItemStatus( prescriptionItemDto.getPrescriptionItemStatus() );
        prescriptionItem.setCancellationReason( prescriptionItemDto.getCancellationReason() );

        return prescriptionItem;
    }
}
