package com.example.ia_cs.dataTransferObjects.PeriodicUpdateDTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class RescheduledAppointmentDTO {
    private LocalDate oldDate;
    private LocalDate newDate;
    private LocalTime oldTime;
    private LocalTime newTime;
    private String doctorName;
    private long doctorID;
    private long patientID;
}
