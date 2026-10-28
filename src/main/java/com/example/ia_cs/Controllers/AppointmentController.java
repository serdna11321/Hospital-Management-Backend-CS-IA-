package com.example.ia_cs.Controllers;

import com.example.ia_cs.dataTransferObjects.AppointmentControllerDTOs.CancelAppointmentRequestDTO;
import com.example.ia_cs.dataTransferObjects.AppointmentControllerDTOs.CreateAppointmentRequestDTO;
import com.example.ia_cs.dataTransferObjects.AppointmentControllerDTOs.ChangeAppointmentRequestDTO;
import com.example.ia_cs.repositories.DoctorRepository;
import com.example.ia_cs.services.AppointmentService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DoctorRepository doctorRepository;

    public AppointmentController(AppointmentService appointmentService, DoctorRepository doctorRepository) {
        this.appointmentService = appointmentService;
        this.doctorRepository = doctorRepository;
    }

    @GetMapping
    public String showAppointmentsPage(
            @RequestParam(required = false) Long selectedDoctorId,
            Model model) {

        // 1. Bind form backing objects
        model.addAttribute("createRequest", new CreateAppointmentRequestDTO());
        model.addAttribute("changeRequest", new ChangeAppointmentRequestDTO());
        model.addAttribute("cancelRequest", new CancelAppointmentRequestDTO());

        model.addAttribute("doctors", doctorRepository.findAll());

        if (selectedDoctorId != null) {
            model.addAttribute("selectedDoctorId", selectedDoctorId);
            List<LocalDateTime> availableSlots = appointmentService.getAvailableSlots(LocalDate.now(), selectedDoctorId);
            model.addAttribute("availableSlots", availableSlots);
        }

        return "appointments/index";
    }

    @PostMapping("/create")
    public String requestNewAppointment(
            @ModelAttribute("createRequest") CreateAppointmentRequestDTO request,
            Authentication authentication) {

        Jwt jwt = (Jwt) authentication.getPrincipal();
        long userID = jwt.getClaim("userID");
        appointmentService.requestAppointment(request, userID);

        return "redirect:/appointments";
    }

    @PostMapping("/update")
    public String updateAppointment(@ModelAttribute("changeRequest") ChangeAppointmentRequestDTO request) {
        appointmentService.changeAppointmentDateTime(request);
        return "redirect:/appointments";
    }

    @PostMapping("/cancel")
    public String cancelAppointment(@ModelAttribute("cancelRequest") CancelAppointmentRequestDTO request) {
        appointmentService.cancelAppointment(request);
        return "redirect:/appointments";
    }
}