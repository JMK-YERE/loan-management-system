package com.loanapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPasswordRequest {

    @NotBlank(message = "Token inahitajika")
    private String token;

    @NotBlank(message = "Password inahitajika")
    @Size(min = 6, message = "Password iwe angalau herufi 6")
    private String password;
}
