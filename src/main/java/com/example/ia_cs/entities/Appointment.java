package com.example.ia_cs.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;
import com.example.ia_cs.entities.AppointmentStatus;

@Entity
@Table(
        name = "appointments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uniqueDoctorAppointment",
                        columnNames = {"doctor_id", "date", "time"}
                ),
                @UniqueConstraint(
                        name = "uniquePatientAppointment",
                        columnNames = {"patient_id", "date", "time"}
                )
        }
)
@Getter
@Setter
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentID;

    @ManyToOne
    @JoinColumn(name = "patientID", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctorID", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "vacationRequestID")
    private VacationRequest affectingVacationRequest;

    @Column(nullable = false)
    private LocalTime appointmentTime;

    @Column(nullable = false)
    private LocalDate appointmentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;
}
