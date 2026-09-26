package com.loanapp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
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
public class LoanRequest {

    @NotNull(message = "Kiasi kinahitajika")
    @DecimalMin(value = "1000.00", message = "Kiasi kiwe angalau TZS 1,000")
    private BigDecimal amount;

    @NotNull(message = "Riba inahitajika")
    @DecimalMin(value = "0.00", message = "Riba isiwe negative")
    private BigDecimal interestRate;

    @NotNull(message = "Muda unahitajika")
    @Min(value = 1, message = "Muda uwe angalau mwezi 1")
    private Integer durationMonths;

    private String purpose;

    private BigDecimal processingFee;

    private Boolean lawyerRequired = false;

    private BigDecimal lawyerFee;
}
