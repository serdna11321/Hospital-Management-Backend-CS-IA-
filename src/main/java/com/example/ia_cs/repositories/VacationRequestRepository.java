package com.example.ia_cs.repositories;

import com.example.ia_cs.entities.Doctor;
import com.example.ia_cs.entities.VacationRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ia_cs.entities.VacationRequest;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long> {

    @Query("SELECT v FROM VacationRequest v WHERE v.startDate < :date AND v.endDate > :date")
    boolean existsVacationRequestByDateAndStatus(LocalDate date, VacationRequestStatus status);
    Optional<VacationRequest> findByVacationRequestID(long vacationRequestID);
    List<VacationRequest> findByDoctor(Doctor doctor);
    List<VacationRequest> findAll();
}

