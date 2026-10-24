package com.example.ia_cs.Controllers;

import com.example.ia_cs.dataTransferObjects.AppointmentControllerDTOs.CancelAppointmentRequestDTO;
import com.example.ia_cs.dataTransferObjects.AppointmentControllerDTOs.CreateAppointmentRequestDTO;
import com.example.ia_cs.dataTransferObjects.AppointmentControllerDTOs.ChangeAppointmentRequestDTO;
import com.example.ia_cs.entities.Appointment;
import com.example.ia_cs.services.AppointmentService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public String showAppointmentsPage(Model model){
        model.addAttribute("createRequest", new CreateAppointmentRequestDTO());
        model.addAttribute("changeRequest", new ChangeAppointmentRequestDTO());
        model.addAttribute("cancelRequest",new CancelAppointmentRequestDTO());
        return "appointments/index";
    }

    @PostMapping("/create")
    public String requestNewAppointment(@ModelAttribute("createRequest") CreateAppointmentRequestDTO request, Authentication authentication){
        Jwt jwt = (Jwt) authentication.getPrincipal();
        long userID = jwt.getClaim("userID");
        Appointment appointment = appointmentService.requestAppointment(request, userID);
        return "redirect:/appointments";
    }

    @PutMapping("/update")
    public String updateAppointment(@ModelAttribute("changeRequest") ChangeAppointmentRequestDTO request, Authentication authentication){
        Appointment appointment = appointmentService.changeAppointmentDateTime(request);
        return "redirect:/appointments";
    }

    @PutMapping("/cancel")
    public String cancelAppointment(@ModelAttribute("cancelRequest") CancelAppointmentRequestDTO request){
        Appointment appointment = appointmentService.cancelAppointment(request);
        return "redirect:/appointments";
    }
}
