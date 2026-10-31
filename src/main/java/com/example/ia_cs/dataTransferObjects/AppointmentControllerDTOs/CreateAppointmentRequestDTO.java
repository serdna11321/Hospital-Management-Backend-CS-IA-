package com.example.ia_cs.dataTransferObjects.AppointmentControllerDTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class CreateAppointmentRequestDTO {
    private Long doctorID;
    private LocalDateTime appointmentDateTime;
}
