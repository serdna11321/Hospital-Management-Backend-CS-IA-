package com.example.ia_cs.services;

import com.example.ia_cs.entities.MedicalRecord;
import com.example.ia_cs.entities.Patient;
import com.example.ia_cs.repositories.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public MedicalRecord addMedicalRecord(Patient patient, LocalDate date, String description){
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setPatient(patient);
        medicalRecord.setRecordDate(date);
        medicalRecord.setDescription(description);
        medicalRecordRepository.save(medicalRecord);
        return medicalRecord;
    }
}
