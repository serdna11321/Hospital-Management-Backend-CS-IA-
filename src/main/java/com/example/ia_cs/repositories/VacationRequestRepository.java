package com.example.ia_cs.repositories;

import com.example.ia_cs.entities.Doctor;
import com.example.ia_cs.entities.VacationRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ia_cs.entities.VacationRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long> {

    @Query("""
        SELECT COUNT(v) > 0 FROM VacationRequest v
        WHERE v.doctor = :doctor
          AND v.status = :status
          AND :date BETWEEN v.startDate AND v.endDate
        """)
    boolean existsVacationRequestByDateAndStatus(@Param("doctor") Doctor doctor,
                                   @Param("date") LocalDate date,
                                   @Param("status") VacationRequestStatus status);
    Optional<VacationRequest> findByVacationRequestID(long vacationRequestID);
    List<VacationRequest> findByDoctor(Doctor doctor);
    List<VacationRequest> findAll();
}

