package com.example.ia_cs.Controllers;

import com.example.ia_cs.dataTransferObjects.VacationRequestControllerDTOs.EvaluateVacationRequestDTO;
import com.example.ia_cs.entities.VacationRequestStatus;
import com.example.ia_cs.repositories.VacationRequestRepository;
import com.example.ia_cs.services.VacationRequestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/vacationRequests/administrators")
public class VacationRequestControllerAdministrator {

    private final VacationRequestService vacationRequestService;
    private final VacationRequestRepository vacationRequestRepository;

    public VacationRequestControllerAdministrator(VacationRequestService vacationRequestService,
                                                  VacationRequestRepository vacationRequestRepository) {
        this.vacationRequestService = vacationRequestService;
        this.vacationRequestRepository = vacationRequestRepository;
    }

    @GetMapping
    public String showVacationRequestsAdmin(Model model){
        model.addAttribute("evaluateRequest", new EvaluateVacationRequestDTO());
        model.addAttribute("pendingRequests", vacationRequestRepository.findByStatus(VacationRequestStatus.PENDING));
        return "vacationRequests/administrators/index";
    }

    @PostMapping("/approve")
    public String approveVacation(@ModelAttribute("evaluateRequest") EvaluateVacationRequestDTO request){
        vacationRequestService.approveVacationRequest(request);
        return "redirect:/vacationRequests/administrators";
    }

    @PostMapping("/deny")
    public String denyVacation(@ModelAttribute("/deny") EvaluateVacationRequestDTO request){
        vacationRequestService.denyVacationRequest(request);
        return "redirect:/vacationRequests/administrators";
    }
}
