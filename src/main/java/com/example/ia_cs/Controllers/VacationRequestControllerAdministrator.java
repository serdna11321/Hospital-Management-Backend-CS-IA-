package com.example.ia_cs.Controllers;

import com.example.ia_cs.dataTransferObjects.VacationRequestControllerDTOs.EvaluateVacationRequestDTO;
import com.example.ia_cs.entities.VacationRequest;
import com.example.ia_cs.services.VacationRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/vacationRequests/administrators")
public class VacationRequestControllerAdministrator {

    private final VacationRequestService vacationRequestService;

    public VacationRequestControllerAdministrator(VacationRequestService vacationRequestService) {
        this.vacationRequestService = vacationRequestService;
    }

    @GetMapping
    public String showVacationRequestsAdmin(Model model){
        model.addAttribute("evaluateRequest", new EvaluateVacationRequestDTO());
        return "vacationRequests/administrators/index";
    }

    @PostMapping("/approve")
    public String approveVacation(@ModelAttribute("/evaluateRequest") EvaluateVacationRequestDTO request){
        vacationRequestService.approveVacationRequest(request);
        return "redirect:/vacationRequests/administrators";
    }

    @PostMapping("/deny")
    public String denyVacation(@ModelAttribute("/deny") EvaluateVacationRequestDTO request){
        vacationRequestService.denyVacationRequest(request);
        return "redirect:/vacationRequests/administrators";
    }
}
