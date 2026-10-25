package com.example.ia_cs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ia_cs.entities.Doctor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    boolean doctorOccupiedAtDateTime(LocalDate date, LocalTime time);
    boolean existsWithPersonID(long personID);
    Optional<Doctor> getByDoctorID(long doctorID);
    Optional<Doctor> getByUserID(long userID);
    List<Doctor> getAllDoctors();

}
