package com.example.ia_cs.Controllers;

import com.example.ia_cs.Configuration.SecurityConfiguration;
import com.example.ia_cs.dataTransferObjects.AuthControllerDTOs.LoginRequestDTO;
import com.example.ia_cs.dataTransferObjects.AuthControllerDTOs.LoginResponseDTO;
import com.example.ia_cs.repositories.UserRepository;
import com.example.ia_cs.services.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthenticationController(SecurityConfiguration securityConfiguration, AuthenticationConfiguration authenticationConfiguration, AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDTO> login(LoginRequestDTO request) throws Exception {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        String token = jwtService.createToken(authentication);
        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
}
