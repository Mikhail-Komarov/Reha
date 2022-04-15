package com.javaschool.komarov.reha.controller;

import com.javaschool.komarov.reha.model.dto.EmployeeDto;
import com.javaschool.komarov.reha.service.impl.EmployeeServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Slf4j
@Controller

public class AuthenticationController {
    private final EmployeeServiceImpl employeeServiceImpl;

    /**
     * Method return page with logout form for
     *
     * @param model       model to adding employee info
     * @param userDetails user info
     * @return html
     */
    @GetMapping
    public String login(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            EmployeeDto employeeDto = employeeServiceImpl.getEmployeeDTOByLogin(userDetails.getUsername());
            model.addAttribute("employeeName", employeeDto.getFirstName());
            model.addAttribute("employeeLastName", employeeDto.getLastName());
        }
        return "login";
    }

    /**
     * Method return page with error message
     *
     * @param model       model to adding error message
     * @param userDetails user info
     * @return html
     */
    @GetMapping("/login-error.html")
    public String loginError(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            EmployeeDto employeeDto = employeeServiceImpl.getEmployeeDTOByLogin(userDetails.getUsername());
            model.addAttribute("employeeName", employeeDto.getFirstName());
            model.addAttribute("employeeLastName", employeeDto.getLastName());
        }
        model.addAttribute("loginError", true);
        log.warn("Failed authentication");
        return "login";
    }
}
