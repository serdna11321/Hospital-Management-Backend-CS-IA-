package com.example.ia_cs.repositories;

import com.example.ia_cs.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate > :startDate AND a.appointmentDate < :endDate")
    List<Appointment> findByDoctorBetweenDates(Doctor doctor, LocalDate startDate, LocalDate endDate);
    boolean existsByDoctorAndAppointmentDateAndAppointmentTimeAndStatus(Doctor doctor, LocalDate date, LocalTime time, AppointmentStatus status);
    boolean existsByPatientAndAppointmentDateAndAppointmentTimeAndStatus(Patient patient, LocalDate date, LocalTime time, AppointmentStatus status);
    List<Appointment> findByPatient(Patient patient);
    List<Appointment> findByDoctor(Doctor doctor);
    List<Appointment> findByAffectingVacationRequest(VacationRequest affectingVacationRequest);
    Optional<Appointment> findByAppointmentID(long appointmentID);
}
