package com.example.ia_cs.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "vacationRequests")
@Getter
@Setter
public class VacationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vacationRequestID;

    @ManyToOne
    @JoinColumn(name = "doctorID", nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VacationRequestStatus status;
}
