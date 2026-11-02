package com.example.ia_cs.Controllers;

import com.example.ia_cs.dataTransferObjects.AuthControllerDTOs.LoginRequestDTO;
import com.example.ia_cs.services.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @GetMapping
    public String showAuthenticationPage(Model model) {
        model.addAttribute("login", new LoginRequestDTO());
        model.addAttribute("logout");
        return "auth/index";
    }

    @PostMapping
    public String login(@ModelAttribute("login") LoginRequestDTO request,
                        HttpServletResponse response,
                        Model model) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            String token = jwtService.createToken(authentication);

            Cookie jwtCookie = new Cookie("JWT_TOKEN", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(false);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(86400);

            response.addCookie(jwtCookie);

            return "redirect:/appointments";

        } catch (AuthenticationException e) {
            System.out.println("Authentication failed type: " + e.getClass().getName());
            System.out.println("Message: " + e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "auth/index";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("JWT_TOKEN", "");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);
        return "redirect:/auth";
    }
}