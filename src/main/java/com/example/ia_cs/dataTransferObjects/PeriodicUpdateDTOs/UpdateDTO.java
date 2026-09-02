package com.example.ia_cs.dataTransferObjects.PeriodicUpdateDTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateDTO {

    //The user receives different DTOs depending on their level of access:
    //Patients receive DoctorOccupiedDTO and AppointmentDTO
    //Doctors receive AppointmentDoctorDTO
    //Administrators receive VacationRequestDTO

    private List<DoctorOccupiedDTO> doctorAvailability;
    private List<AppointmentDTO> patientAppointments;
    private List<VacationRequestDTO> administratorVacationRequests;
    private List<AppointmentDoctorDTO> doctorAppointments;
    private List<VacationRequestDTO> doctorVacationRequests;
    private List<RescheduledAppointmentDTO> rescheduledAppointments;
}
