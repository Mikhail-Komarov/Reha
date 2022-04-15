package com.javaschool.komarov.reha.model.dto;

import com.javaschool.komarov.reha.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String login;
    private String password;
    private Role role;
}
