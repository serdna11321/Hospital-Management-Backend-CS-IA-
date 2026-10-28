package com.example.ia_cs.repositories;

import com.example.ia_cs.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ia_cs.entities.Patient;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByPersonPersonID(long personID);
    Optional<Patient> findByPatientID(long patientID);
}
