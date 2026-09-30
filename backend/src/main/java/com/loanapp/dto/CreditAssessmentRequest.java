package com.loanapp.dto;

import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public class CreditAssessmentRequest {
    @DecimalMin(value="0.0", inclusive=true)
    private BigDecimal monthlyExpenses = BigDecimal.ZERO;

    @DecimalMin(value="0.0", inclusive=true)
    private BigDecimal existingMonthlyDebt = BigDecimal.ZERO;

    public BigDecimal getMonthlyExpenses(){return monthlyExpenses;}
    public void setMonthlyExpenses(BigDecimal v){monthlyExpenses=v;}
    public BigDecimal getExistingMonthlyDebt(){return existingMonthlyDebt;}
    public void setExistingMonthlyDebt(BigDecimal v){existingMonthlyDebt=v;}
}