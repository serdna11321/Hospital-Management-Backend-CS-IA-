package com.example.ia_cs.dataTransferObjects.AppointmentControllerDTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ChangeAppointmentRequestDTO {

    private long appointmentID;
    private LocalDate newDate;
    private LocalTime newTime;
}
