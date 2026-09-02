package com.example.ia_cs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IaCsApplication {

    public static void main(String[] args) {
        SpringApplication.run(IaCsApplication.class, args);
    }
}