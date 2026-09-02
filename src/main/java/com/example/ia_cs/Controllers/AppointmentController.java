package com.example.ia_cs.Controllers;

import com.example.ia_cs.dataTransferObjects.AppointmentControllerDTOs.CancelAppointmentRequestDTO;
import com.example.ia_cs.dataTransferObjects.AppointmentControllerDTOs.CreateAppointmentRequestDTO;
import com.example.ia_cs.dataTransferObjects.AppointmentControllerDTOs.ChangeAppointmentRequestDTO;
import com.example.ia_cs.entities.Appointment;
import com.example.ia_cs.services.AppointmentService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<Appointment> requestNewAppointment(@RequestBody CreateAppointmentRequestDTO request, Authentication authentication){
        Jwt jwt = (Jwt) authentication.getPrincipal();
        long userID = jwt.getClaim("userID");
        Appointment appointment = appointmentService.requestAppointment(request, userID);
        return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
    }

    @PutMapping
    public ResponseEntity<Appointment> updateAppointment(@RequestBody ChangeAppointmentRequestDTO request, Authentication authentication){
        Appointment appointment = appointmentService.changeAppointmentDateTime(request);
        return ResponseEntity.status(HttpStatus.OK).body(appointment);
    }

    @PutMapping
    public ResponseEntity<Appointment> cancelAppointment(@RequestBody CancelAppointmentRequestDTO request){
        Appointment appointment = appointmentService.cancelAppointment(request);
        return ResponseEntity.status(HttpStatus.OK).body(appointment);
    }
}
