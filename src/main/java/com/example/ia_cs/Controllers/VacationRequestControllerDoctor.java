package com.example.ia_cs.Controllers;

import com.example.ia_cs.dataTransferObjects.VacationRequestControllerDTOs.CreateVacationRequestDTO;
import com.example.ia_cs.entities.VacationRequest;
import com.example.ia_cs.services.VacationRequestService;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


@Controller
@RequestMapping("/vacationRequests/doctors")
public class VacationRequestControllerDoctor {

    private final VacationRequestService vacationRequestService;

    public VacationRequestControllerDoctor(VacationRequestService vacationRequestService) {
        this.vacationRequestService = vacationRequestService;
    }

    @GetMapping
    public String showVacationRequestsDoctor(Model model, Authentication authentication) {
        model.addAttribute("requestVacation", new CreateVacationRequestDTO());
        model.addAttribute("vacationHistory",
                vacationRequestService.getHistoryForUser(getUserID(authentication)));
        return "vacationRequests/doctors/index";
    }

    private long getUserID(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return ((Number) jwt.getClaim("userID")).longValue();
    }

    @PostMapping("/requestVacation")
    public String requestVacation(@ModelAttribute("requestVacation") CreateVacationRequestDTO request, Authentication authentication){
        long userID = getUserID(authentication);
        vacationRequestService.requestVacation(request, userID);
        return "redirect:/vacationRequests/doctors";
    }
}
