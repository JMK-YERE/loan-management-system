package com.loanapp.dto;

import com.loanapp.model.Signature;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignatureRequest {

    @NotNull(message = "Loan ID inahitajika")
    private Long loanId;

    @NotBlank(message = "Sahihi inahitajika")
    private String signatureData;

    @NotNull(message = "Aina ya sahihi inahitajika")
    private Signature.SignatureType signatureType;

    private String deviceInfo;
}
