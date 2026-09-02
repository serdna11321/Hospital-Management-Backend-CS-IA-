package com.example.ia_cs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ia_cs.entities.MedicalRecord;

import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> getByPatientID(long patientID);
}
