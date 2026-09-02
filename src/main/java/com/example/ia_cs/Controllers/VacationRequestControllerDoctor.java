package com.example.ia_cs.Controllers;

import com.example.ia_cs.dataTransferObjects.VacationRequestControllerDTOs.CreateVacationRequestDTO;
import com.example.ia_cs.entities.VacationRequest;
import com.example.ia_cs.services.VacationRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vacationRequests/doctors")
public class VacationRequestControllerDoctor {

    private final VacationRequestService vacationRequestService;

    public VacationRequestControllerDoctor(VacationRequestService vacationRequestService) {
        this.vacationRequestService = vacationRequestService;
    }

    public ResponseEntity<VacationRequest> requestVacation(@RequestBody CreateVacationRequestDTO request, Authentication authentication){
        Jwt jwt = (Jwt) authentication.getPrincipal();
        long userID = jwt.getClaim("userID");
        VacationRequest vacationRequest = vacationRequestService.requestVacation(request, userID);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(vacationRequest);
    }
}
