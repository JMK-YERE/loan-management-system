package com.loanapp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    @NotNull(message = "Loan ID inahitajika")
    private Long loanId;

    @NotNull(message = "Kiasi kinahitajika")
    @DecimalMin(value = "1.00", message = "Kiasi kiwe angalau TZS 1")
    private BigDecimal amount;

    @NotBlank(message = "Njia ya malipo inahitajika")
    private String paymentMethod;
}
