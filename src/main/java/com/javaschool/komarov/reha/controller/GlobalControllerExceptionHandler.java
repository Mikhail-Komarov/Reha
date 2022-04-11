package com.javaschool.komarov.reha.controller;

import com.javaschool.komarov.reha.exception.PatientNotFoundException;
import com.javaschool.komarov.reha.exception.PrescriptionItemNotFoundException;
import com.javaschool.komarov.reha.exception.PrescriptionNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

@Slf4j
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(value = {PatientNotFoundException.class})
    public ModelAndView handlePatientNotFound(PatientNotFoundException e) {
        ModelAndView model = new ModelAndView("error");
        model.addObject("exception", ":");
        model.addObject("errorMessage", e.getMessage());
        log.warn("Patient not found: " + e.getMessage());
        log.warn("!!!" + Arrays.toString(e.getStackTrace()));
        return model;
    }

    @ExceptionHandler(value = {PrescriptionNotFoundException.class})
    public ModelAndView handlePrescriptionNotFound(PrescriptionNotFoundException e) {
        ModelAndView model = new ModelAndView("error");
        model.addObject("exception", ":");
        model.addObject("errorMessage", e.getMessage());
        log.warn("Prescription not found: " + e.getMessage());
        log.warn(Arrays.toString(e.getStackTrace()));
        return model;
    }

    @ExceptionHandler(value = {PrescriptionItemNotFoundException.class})
    public ModelAndView handleItemNotFound(PrescriptionItemNotFoundException e) {
        ModelAndView model = new ModelAndView("error");
        model.addObject("exception", ":");
        model.addObject("errorMessage", e.getMessage());
        log.warn("Item not found: " + e.getMessage());
        log.warn(Arrays.toString(e.getStackTrace()));
        return model;
    }

    @ExceptionHandler(value = {Exception.class})
    public ModelAndView handleOtherException(Exception e) {
        ModelAndView model = new ModelAndView("error");
        model.addObject("exception", ":");
        model.addObject("errorMessage", "Sorry, something went wrong...");
        log.error("Exception: " + e);
        log.error(Arrays.toString(e.getStackTrace()));
        return model;
    }
}
