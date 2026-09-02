package com.example.ia_cs.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "medicalRecords")
@Getter
@Setter
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordID;

    @ManyToOne
    @JoinColumn(name = "patientID")
    private Patient patient;

    @Column(nullable = false)
    private LocalDate recordDate;

    @Column(nullable = false)
    private String description;
}
