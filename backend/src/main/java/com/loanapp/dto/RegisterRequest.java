package com.loanapp.dto;

import com.loanapp.model.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    @Pattern(regexp = "^\\+[1-9][0-9]{6,14}$", message = "Simu iwe na code ya nchi, mfano +255712345678")
    private String phone;

    @NotNull(message = "Aina ya akaunti inahitajika")
    private User.Role role;

    @NotBlank(message = "Aina ya kitambulisho inahitajika")
    private String idType;

    @NotBlank(message = "Namba ya kitambulisho inahitajika")
    @Size(min = 5, max = 30, message = "Namba ya kitambulisho iwe herufi 5 hadi 30")
    private String idNumber;

    @NotNull(message = "Tarehe ya kuzaliwa inahitajika")
    @Past(message = "Tarehe ya kuzaliwa si sahihi")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Jinsia inahitajika")
    private String gender;

    @NotBlank(message = "Hali ya ndoa inahitajika")
    private String maritalStatus;

    @NotBlank(message = "Utaifa unahitajika")
    private String nationality;

    @NotBlank(message = "Anwani inahitajika")
    private String address;

    @NotBlank(message = "Mji unahitajika")
    private String city;

    @NotBlank(message = "Nchi inahitajika")
    private String country;

    @NotBlank(message = "Hali ya ajira inahitajika")
    private String employmentStatus;

    private String occupation;
    private String employer;

    @NotNull(message = "Kipato cha mwezi kinahitajika")
    @DecimalMin(value = "0", message = "Kipato hakiwezi kuwa hasi")
    private BigDecimal monthlyIncome;

    @NotBlank(message = "Jina la ndugu wa karibu linahitajika")
    private String kinName;

    @NotBlank(message = "Simu ya ndugu wa karibu inahitajika")
    @Pattern(regexp = "^\\+[1-9][0-9]{6,14}$", message = "Simu ya ndugu iwe na code ya nchi")
    private String kinPhone;

    @NotBlank(message = "Uhusiano na ndugu unahitajika")
    private String kinRelationship;

    @NotBlank(message = "Picha inahitajika")
    private String photo;
}
