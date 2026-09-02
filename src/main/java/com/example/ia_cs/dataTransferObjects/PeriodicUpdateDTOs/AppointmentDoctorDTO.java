package com.example.ia_cs.dataTransferObjects.PeriodicUpdateDTOs;

import com.example.ia_cs.entities.MedicalRecord;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class AppointmentDoctorDTO {

    private long appointmentID;
    private String patientName;
    private LocalDate date;
    private LocalTime time;
    private List<MedicalRecord> medicalRecords;
}
