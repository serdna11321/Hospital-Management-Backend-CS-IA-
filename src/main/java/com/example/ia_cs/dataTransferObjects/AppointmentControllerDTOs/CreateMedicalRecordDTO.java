package com.example.ia_cs.dataTransferObjects.AppointmentControllerDTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateMedicalRecordDTO {
    private Long patientID;
    private LocalDate date;
    private String description;
}
