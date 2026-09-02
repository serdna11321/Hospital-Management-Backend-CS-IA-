package com.example.ia_cs.Controllers;

import com.example.ia_cs.dataTransferObjects.UserControllerDTOs.*;
import com.example.ia_cs.services.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/administrators")
public class UserControllerAdministrator {

    private final UserService userService;

    public UserControllerAdministrator(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<DoctorDTO> createDoctorAccount(@RequestBody CreateDoctorRequestDTO request){
        DoctorDTO doctorDTO = userService.createDoctorAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorDTO);
    }

    @PostMapping
    public ResponseEntity<AdministratorDTO> createAdministratorAccount(@RequestBody CreateAdministratorRequestDTO request){
        AdministratorDTO administratorDTO = userService.createAdministratorAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(administratorDTO);
    }
}
