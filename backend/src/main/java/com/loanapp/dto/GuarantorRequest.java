package com.loanapp.dto;

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
public class GuarantorRequest {

    @NotNull(message = "Guarantor ID inahitajika")
    private Long guarantorId;

    @NotNull(message = "Kiasi cha dhamana kinahitajika")
    private BigDecimal guaranteedAmount;

    private String relationship;
}
