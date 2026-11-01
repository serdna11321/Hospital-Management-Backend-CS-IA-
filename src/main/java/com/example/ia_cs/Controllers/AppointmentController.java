package com.example.ia_cs.Controllers;

import com.example.ia_cs.dataTransferObjects.AppointmentControllerDTOs.CancelAppointmentRequestDTO;
import com.example.ia_cs.dataTransferObjects.AppointmentControllerDTOs.ChangeAppointmentRequestDTO;
import com.example.ia_cs.dataTransferObjects.AppointmentControllerDTOs.CreateAppointmentRequestDTO;
import com.example.ia_cs.repositories.AppointmentRepository;
import com.example.ia_cs.repositories.DoctorRepository;
import com.example.ia_cs.services.AppointmentService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DoctorRepository doctorRepository;

    public AppointmentController(AppointmentService appointmentService, DoctorRepository doctorRepository, AppointmentRepository appointmentRepository) {
        this.appointmentService = appointmentService;
        this.doctorRepository = doctorRepository;
    }

    @GetMapping({"", "/index"})
    public String showAppointmentsPage(@RequestParam(required = false) Long selectedDoctorID,
                                       Authentication authentication,
                                       Model model) {

        long userID = getUserID(authentication);
        boolean isDoctor = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"));

        model.addAttribute("appointments", appointmentService.getAppointmentsByUserID(userID));

        CreateAppointmentRequestDTO createRequest = new CreateAppointmentRequestDTO();
        createRequest.setDoctorID(selectedDoctorID);          // field must be Long, not long
        model.addAttribute("createRequest", createRequest);
        model.addAttribute("changeRequest", new ChangeAppointmentRequestDTO());
        model.addAttribute("cancelRequest", new CancelAppointmentRequestDTO());

        model.addAttribute("doctors", doctorRepository.findAll());
        model.addAttribute("selectedDoctorID", selectedDoctorID);
        model.addAttribute("availableSlots",
                selectedDoctorID != null
                        ? appointmentService.getAvailableSlots(LocalDate.now(), selectedDoctorID)
                        : java.util.List.of());

        return "appointments/index";
    }

    @PostMapping("/create")
    public String requestNewAppointment(@ModelAttribute("createRequest") CreateAppointmentRequestDTO request,
                                        Authentication authentication) {
        appointmentService.requestAppointment(request, getUserID(authentication));
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

    private long getUserID(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return ((Number) jwt.getClaim("userID")).longValue();
    }
}