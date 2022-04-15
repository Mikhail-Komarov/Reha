package com.javaschool.komarov.reha;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;

@RequiredArgsConstructor
@SpringBootApplication
public class Application {
    private final MessageSource messageSource;
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}