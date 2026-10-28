package com.example.ia_cs.services;

import com.example.ia_cs.dataTransferObjects.AppointmentControllerDTOs.CreateMedicalRecordDTO;
import com.example.ia_cs.entities.MedicalRecord;
import com.example.ia_cs.entities.Patient;
import com.example.ia_cs.repositories.MedicalRecordRepository;
import com.example.ia_cs.repositories.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository, PatientRepository patientRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
    }

    public MedicalRecord addMedicalRecord(CreateMedicalRecordDTO createMedicalRecordDTO){
        Optional<Patient> optionalPatient = patientRepository.findByPatientID(createMedicalRecordDTO.getPatientID());
        if (optionalPatient.isEmpty()){
            throw new RuntimeException("Patient not found");
        }
        Patient patient = optionalPatient.get();

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setPatient(patient);
        medicalRecord.setRecordDate(createMedicalRecordDTO.getDate());
        medicalRecord.setDescription(createMedicalRecordDTO.getDescription());
        medicalRecordRepository.save(medicalRecord);
        return medicalRecord;
    }
}
