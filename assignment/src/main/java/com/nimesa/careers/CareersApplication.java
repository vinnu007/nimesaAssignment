package com.nimesa.careers;

import com.nimesa.careers.multithreading_assignment.ParallelProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class CareersApplication {

    public static void main(String[] args) {
        SpringApplication.run(CareersApplication.class, args);
    }
}

