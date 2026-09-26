package com.loanapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    @NotBlank(message = "Barua pepe au namba ya simu inahitajika")
    private String username;

    @NotBlank(message = "Password inahitajika")
    private String password;
}
