package com.example.ia_cs.repositories;

import com.example.ia_cs.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDateTime > :startDateTime AND a.appointmentDateTime < :endDateTime")
    List<Appointment> findByDoctorBetweenDateTimes(Doctor doctor, LocalDateTime startDateTime, LocalDateTime endDateTime);
    boolean existsByDoctorAndAppointmentDateTimeAndStatus(Doctor doctor, LocalDateTime dateTime, AppointmentStatus status);
    boolean existsByPatientAndAppointmentDateTimeAndStatus(Patient patient, LocalDateTime dateTime, AppointmentStatus status);
    List<Appointment> findByPatient(Patient patient);
    List<Appointment> findByDoctor(Doctor doctor);
    List<Appointment> findByAffectingVacationRequest(VacationRequest affectingVacationRequest);
    Optional<Appointment> findByAppointmentID(long appointmentID);
}
