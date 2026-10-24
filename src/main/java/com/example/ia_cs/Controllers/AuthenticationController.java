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
@RequestMapping("/authentication")
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
        return "auth/login";
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
            jwtCookie.setHttpOnly(true);  // Prevents XSS script access
            jwtCookie.setSecure(false);   // Set to true if using HTTPS
            jwtCookie.setPath("/");       // Available across the entire application
            jwtCookie.setMaxAge(86400);   // Expiration in seconds (1 day)

            response.addCookie(jwtCookie);

            return "redirect:/dashboard";

        } catch (AuthenticationException e) {
            model.addAttribute("error", "Invalid credentials. Please try again.");
            return "auth/login"; // Re-render login view with error context
        }
    }
}