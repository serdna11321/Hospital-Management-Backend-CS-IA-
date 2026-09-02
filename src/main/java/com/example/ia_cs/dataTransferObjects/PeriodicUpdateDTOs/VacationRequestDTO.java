package com.example.ia_cs.dataTransferObjects.PeriodicUpdateDTOs;

import com.example.ia_cs.entities.VacationRequestStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VacationRequestDTO {

    private long vacationRequestID;
    private String doctorName;
    private LocalDate startDate;
    private LocalDate endDate;
    private VacationRequestStatus vacationRequestStatus;
}
