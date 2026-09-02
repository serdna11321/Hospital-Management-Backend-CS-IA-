package com.example.ia_cs.repositories;

import com.example.ia_cs.entities.Doctor;
import com.example.ia_cs.entities.VacationRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ia_cs.entities.VacationRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long> {

    boolean existsVacationRequestAtDateWithStatus(LocalDate date, VacationRequestStatus status);
    boolean existsVacationRequestBetweenDatesAndNotStatusAndDoctor(Doctor doctor, LocalDate startDate, LocalDate endDate, VacationRequestStatus status);

    Optional<VacationRequest> findByVacationRequestID(long vacationRequestID);
    List<VacationRequest> findByDoctor(Doctor doctor);
    List<VacationRequest> getAll();
}

