package com.loanapp.dto;

import com.loanapp.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "Jina kamili linahitajika")
    @Size(min = 3, max = 200, message = "Jina liwe kati ya 3 na 200 herufi")
    private String fullName;

    @NotBlank(message = "Barua pepe inahitajika")
    @Email(message = "Barua pepe si sahihi")
    private String email;

    @NotBlank(message = "Namba ya simu inahitajika")
    @Pattern(regexp = "^\\+?255[0-9]{9}$", message = "Namba ya simu iwe format ya Tanzania (+255XXXXXXXXX)")
    private String phone;

    @NotBlank(message = "Password inahitajika")
    @Size(min = 6, message = "Password iwe angalau herufi 6")
    private String password;

    @NotNull(message = "Role inahitajika")
    private User.Role role;

    private String nationalId;
}
