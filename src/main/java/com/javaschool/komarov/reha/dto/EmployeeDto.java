package com.javaschool.komarov.reha.dto;

import com.javaschool.komarov.reha.model.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class EmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String login;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

}
