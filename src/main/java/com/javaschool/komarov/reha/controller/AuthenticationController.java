package com.javaschool.komarov.reha.controller;

import com.javaschool.komarov.reha.dto.EmployeeDto;
import com.javaschool.komarov.reha.service.EmployeeService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {
    private final EmployeeService employeeService;

    public AuthenticationController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String login(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            EmployeeDto employeeDto = employeeService.getEmployeeByLogin(userDetails.getUsername()).get();
            model.addAttribute("employeeName", employeeDto.getFirstName());
            model.addAttribute("employeeLastName", employeeDto.getLastName());
        }
        return "login";
    }
}
