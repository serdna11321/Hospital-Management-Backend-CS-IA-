package com.example.ia_cs.dataTransferObjects.PeriodicUpdateDTOs;

import com.example.ia_cs.entities.AppointmentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class AppointmentDTO {

    private long AppointmentID;
    private String doctorName;
    private String specialty;
    private LocalDate date;
    private LocalTime time;
    private AppointmentStatus appointmentStatus;
}
