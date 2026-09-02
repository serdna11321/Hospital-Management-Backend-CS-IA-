package com.example.ia_cs.repositories;

import com.example.ia_cs.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByDoctorBetweenDates(Doctor doctor, LocalDate startDate, LocalDate endDate);
    boolean existsByDoctorAndDateAtTimeAndStatus(Doctor doctor, LocalDate date, LocalTime time, AppointmentStatus status);
    boolean existsByPatientAndDateAtTimeAndStatus(Patient patient, LocalDate date, LocalTime time, AppointmentStatus status);
    List<Appointment> findByPatient(Patient patient);
    List<Appointment> findByDoctor(Doctor doctor);
    List<Appointment> findByAffectingVacationRequest(VacationRequest affectingVacationRequest);
    Optional<Appointment> getAppointmentByAppointmentID(long appointmentID);
    LocalDateTime findNextAvailableDateTimeFromDateForDoctor(LocalDate date, Doctor doctor);

}
