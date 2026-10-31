package com.example.ia_cs.dataTransferObjects.PeriodicUpdateDTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class RescheduledAppointmentDTO {
    private LocalDateTime oldDateTime;
    private LocalDateTime newDateTime;
    private String doctorName;
    private long doctorID;
    private long patientID;
}
