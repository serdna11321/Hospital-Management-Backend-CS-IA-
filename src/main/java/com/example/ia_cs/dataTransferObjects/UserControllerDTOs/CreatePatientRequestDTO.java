package com.example.ia_cs.dataTransferObjects.UserControllerDTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreatePatientRequestDTO {
    private String nif;
    private String email;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String city;
    private String streetName;
    private String streetNumber;
    private LocalDate dateOfBirth;
    private String telephoneNumber;
    private String postcode;
    private String password;
}
