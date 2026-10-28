package com.example.ia_cs.Controllers;

import com.example.ia_cs.dataTransferObjects.UserControllerDTOs.*;
import com.example.ia_cs.services.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users/administrators")
public class UserControllerAdministrator {

    private final UserService userService;

    public UserControllerAdministrator(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showAdministratorUser(Model model){
        model.addAttribute("createDoctor", new CreateDoctorRequestDTO());
        model.addAttribute("createAdmin", new CreateAdministratorRequestDTO());
        return "users/administrators/index";
    }

    @PostMapping("/createDoctor")
    public String createDoctorAccount(@ModelAttribute("/createDoctor") CreateDoctorRequestDTO request){
        userService.createDoctorAccount(request);
        return "redirect:/users/administrators";
    }

    @PostMapping("/createAdmin")
    public String createAdministratorAccount(@ModelAttribute("/createAdmin") CreateAdministratorRequestDTO request){
        userService.createAdministratorAccount(request);
        return "redirect:/users/administrators";
    }
}
