package com.example.ia_cs.Controllers;

import com.example.ia_cs.dataTransferObjects.VacationRequestControllerDTOs.EvaluateVacationRequestDTO;
import com.example.ia_cs.dataTransferObjects.VacationRequestControllerDTOs.CreateVacationRequestDTO;
import com.example.ia_cs.entities.VacationRequest;
import com.example.ia_cs.services.VacationRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vacationRequests/administrators")
public class VacationRequestControllerAdministrator {

    private final VacationRequestService vacationRequestService;

    public VacationRequestControllerAdministrator(VacationRequestService vacationRequestService) {
        this.vacationRequestService = vacationRequestService;
    }

    public ResponseEntity<VacationRequest> approveVacation(@RequestBody EvaluateVacationRequestDTO request){
        VacationRequest vacationRequest = vacationRequestService.approveVacationRequest(request);
        return ResponseEntity.status(HttpStatus.OK).body(vacationRequest);
    }

    public ResponseEntity<VacationRequest> denyVacation(@RequestBody EvaluateVacationRequestDTO request){
        VacationRequest vacationRequest = vacationRequestService.denyVacationRequest(request);
        return ResponseEntity.status(HttpStatus.OK).body(vacationRequest);
    }
}
