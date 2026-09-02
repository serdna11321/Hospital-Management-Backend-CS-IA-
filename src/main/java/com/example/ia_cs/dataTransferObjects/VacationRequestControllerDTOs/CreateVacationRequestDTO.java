package com.example.ia_cs.dataTransferObjects.VacationRequestControllerDTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateVacationRequestDTO {
    private long doctorID;
    private LocalDate startDate;
    private LocalDate endDate;
}
