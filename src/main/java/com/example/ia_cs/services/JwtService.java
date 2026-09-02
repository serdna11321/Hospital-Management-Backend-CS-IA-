package com.example.ia_cs.services;

import com.example.ia_cs.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final UserRepository userRepository;

    public JwtService(SecretKey signingKey, UserRepository userRepository) {
        this.signingKey = signingKey;
        this.userRepository = userRepository;
    }

    public String createToken(Authentication authentication) {

        CustomUserDetailsService userDetailsService =
                (CustomUserDetailsService) authentication.getPrincipal();

        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(authentication.getName())
                .claim("userID", userRepository.getUserIdFromEmail(authentication.getName()))
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(signingKey)
                .compact();
    }
}