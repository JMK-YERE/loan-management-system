package com.loanapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private Long id;
    private Long loanId;
    private BigDecimal amount;
    private String paymentMethod;
    private String transactionId;
    private String status;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
}
