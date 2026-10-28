package com.example.ia_cs.Controllers;

import com.example.ia_cs.dataTransferObjects.UserControllerDTOs.*;
import com.example.ia_cs.services.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


@Controller
@RequestMapping("/users/patients")
public class UserControllerPatient {

    private final UserService userService;

    public UserControllerPatient(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showPatientDashboard(Model model){
        model.addAttribute("createPatient", new CreatePatientRequestDTO());
        return "users/patients/index";
    }

    @PostMapping("/createPatient")
    public String createPatientAccount(@ModelAttribute("createPatient") CreatePatientRequestDTO request){
        userService.createPatientAccount(request);
        return "redirect:/users/patients";
    }

}