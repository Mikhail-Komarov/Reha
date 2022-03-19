package com.javaschool.komarov.reha.mapper;

import com.javaschool.komarov.reha.dto.EmployeeDto;
import com.javaschool.komarov.reha.dto.PrescriptionItemDto;
import com.javaschool.komarov.reha.dto.TherapyDto;
import com.javaschool.komarov.reha.model.Employee;
import com.javaschool.komarov.reha.model.PrescriptionItem;
import com.javaschool.komarov.reha.model.Therapy;
import java.util.ArrayList;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-03-19T18:26:17+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_312 (BellSoft)"
)
@Component
public class PrescriptionItemMapperImpl implements PrescriptionItemMapper {

    @Autowired
    private PrescriptionMapper prescriptionMapper;

    @Override
    public PrescriptionItemDto toDTO(PrescriptionItem model) {
        if ( model == null ) {
            return null;
        }

        PrescriptionItemDto prescriptionItemDto = new PrescriptionItemDto();

        prescriptionItemDto.setItemId( model.getId() );
        prescriptionItemDto.setDose( model.getDose() );
        prescriptionItemDto.setStartTreatment( model.getStartTreatment() );
        prescriptionItemDto.setEndTreatment( model.getEndTreatment() );
        prescriptionItemDto.setTimePattern( model.getTimePattern() );
        prescriptionItemDto.setPrescriptionItemStatus( model.getPrescriptionItemStatus() );
        prescriptionItemDto.setCancellationReason( model.getCancellationReason() );
        prescriptionItemDto.setEmployee( employeeToEmployeeDto( model.getEmployee() ) );
        prescriptionItemDto.setTherapy( therapyToTherapyDto( model.getTherapy() ) );
        prescriptionItemDto.setPrescription( prescriptionMapper.toDTO( model.getPrescription() ) );

        return prescriptionItemDto;
    }

    @Override
    public PrescriptionItem toModel(PrescriptionItemDto dto) {
        if ( dto == null ) {
            return null;
        }

        PrescriptionItem prescriptionItem = new PrescriptionItem();

        prescriptionItem.setId( dto.getItemId() );
        prescriptionItem.setDose( dto.getDose() );
        prescriptionItem.setStartTreatment( dto.getStartTreatment() );
        prescriptionItem.setEndTreatment( dto.getEndTreatment() );
        prescriptionItem.setTimePattern( dto.getTimePattern() );
        prescriptionItem.setPrescription( prescriptionMapper.toModel( dto.getPrescription() ) );
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
}
