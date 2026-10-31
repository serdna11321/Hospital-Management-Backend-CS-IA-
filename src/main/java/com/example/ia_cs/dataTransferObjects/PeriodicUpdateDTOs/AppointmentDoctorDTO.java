package com.example.ia_cs.dataTransferObjects.PeriodicUpdateDTOs;

import com.example.ia_cs.entities.MedicalRecord;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class AppointmentDoctorDTO {

    private long appointmentID;
    private String patientName;
    private LocalDateTime dateTime;
    private List<MedicalRecord> medicalRecords;
}
