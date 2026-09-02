package com.example.ia_cs.dataTransferObjects.AuthControllerDTOs;

import com.example.ia_cs.entities.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LoginResponseDTO {
    private String token;
}
