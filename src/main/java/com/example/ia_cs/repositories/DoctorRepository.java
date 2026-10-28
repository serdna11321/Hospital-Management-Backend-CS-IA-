package com.example.ia_cs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ia_cs.entities.Doctor;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByPersonID(long personID);
    Optional<Doctor> findByDoctorID(long doctorID);
    Optional<Doctor> findByUserID(long userID);
    List<Doctor> findAll();

}
