package com.example.ia_cs.Controllers;

import com.example.ia_cs.dataTransferObjects.UserControllerDTOs.*;
import com.example.ia_cs.services.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/patient")
public class UserControllerPatient {

    private final UserService userService;

    public UserControllerPatient(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<PatientDTO> createPatientAccount(@RequestBody CreatePatientRequestDTO request){
        PatientDTO patientDTO = userService.createPatientAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(patientDTO);
    }

}