package com.example.ia_cs.dataTransferObjects.PeriodicUpdateDTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class DoctorOccupiedDTO {

    private long doctorID;
    private List<LocalDateTime> occupiedDateTimes;
}
