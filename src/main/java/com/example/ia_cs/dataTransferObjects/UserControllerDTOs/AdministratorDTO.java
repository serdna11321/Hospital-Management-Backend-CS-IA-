package com.example.ia_cs.dataTransferObjects.UserControllerDTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AdministratorDTO {
    private Long administratorID;
    private String firstName;
    private String lastName;
    private String telephoneNumber;
    private String city;
    private String streetName;
    private String streetNumber;
    private String postcode;
    private LocalDate dateOfBirth;
}
