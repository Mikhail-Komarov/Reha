package com.javaschool.komarov.reha.mapper;

import com.javaschool.komarov.reha.dto.EmployeeDto;
import com.javaschool.komarov.reha.dto.PatientDto;
import com.javaschool.komarov.reha.dto.PrescriptionDto;
import com.javaschool.komarov.reha.dto.PrescriptionItemDto;
import com.javaschool.komarov.reha.dto.TherapyDto;
import com.javaschool.komarov.reha.model.Employee;
import com.javaschool.komarov.reha.model.Patient;
import com.javaschool.komarov.reha.model.Prescription;
import com.javaschool.komarov.reha.model.PrescriptionItem;
import com.javaschool.komarov.reha.model.Therapy;
import java.util.ArrayList;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-03-18T15:36:14+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_312 (BellSoft)"
)
@Component
public class PrescriptionItemMapperImpl implements PrescriptionItemMapper {

    @Override
    public PrescriptionItemDto toDTO(PrescriptionItem model) {
        if ( model == null ) {
            return null;
        }

        PrescriptionItemDto prescriptionItemDto = new PrescriptionItemDto();

        prescriptionItemDto.setId( model.getId() );
        prescriptionItemDto.setDose( model.getDose() );
        prescriptionItemDto.setStartTreatment( model.getStartTreatment() );
        prescriptionItemDto.setEndTreatment( model.getEndTreatment() );
        prescriptionItemDto.setTimePattern( model.getTimePattern() );
        prescriptionItemDto.setPrescriptionItemStatus( model.getPrescriptionItemStatus() );
        prescriptionItemDto.setCancellationReason( model.getCancellationReason() );
        prescriptionItemDto.setEmployee( employeeToEmployeeDto( model.getEmployee() ) );
        prescriptionItemDto.setTherapy( therapyToTherapyDto( model.getTherapy() ) );
        prescriptionItemDto.setPrescription( prescriptionToPrescriptionDto( model.getPrescription() ) );

        return prescriptionItemDto;
    }

    @Override
    public PrescriptionItem toModel(PrescriptionItemDto dto) {
        if ( dto == null ) {
            return null;
        }

        PrescriptionItem prescriptionItem = new PrescriptionItem();

        prescriptionItem.setId( dto.getId() );
        prescriptionItem.setDose( dto.getDose() );
        prescriptionItem.setStartTreatment( dto.getStartTreatment() );
        prescriptionItem.setEndTreatment( dto.getEndTreatment() );
        prescriptionItem.setTimePattern( dto.getTimePattern() );
        prescriptionItem.setPrescription( prescriptionDtoToPrescription( dto.getPrescription() ) );
        prescriptionItem.setTherapy( therapyDtoToTherapy( dto.getTherapy() ) );
        prescriptionItem.setEmployee( employeeDtoToEmployee( dto.getEmployee() ) );
        prescriptionItem.setPrescriptionItemStatus( dto.getPrescriptionItemStatus() );
        prescriptionItem.setCancellationReason( dto.getCancellationReason() );

        return prescriptionItem;
    }

    @Override
    public Iterable<PrescriptionItemDto> toDTOList(Iterable<PrescriptionItem> models) {
        if ( models == null ) {
            return null;
        }

        ArrayList<PrescriptionItemDto> iterable = new ArrayList<PrescriptionItemDto>();
        for ( PrescriptionItem prescriptionItem : models ) {
            iterable.add( toDTO( prescriptionItem ) );
        }

        return iterable;
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
        employeeDto.setPassword( employee.getPassword() );
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
        employee.setPassword( employeeDto.getPassword() );
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
}
