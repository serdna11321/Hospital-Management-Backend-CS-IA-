package com.example.ia_cs.dataTransferObjects.AuthControllerDTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    private String email;
    private String password;
}
