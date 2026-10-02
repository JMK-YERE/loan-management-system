package com.loanapp.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class LoanQuoteRequest {
    @NotNull private Long productId;
    @NotNull @DecimalMin("1.00") private BigDecimal amount;
    @NotNull @Min(1) private Integer duration;
    private BigDecimal monthlyExpenses;
    private BigDecimal existingMonthlyDebt;
    private String collateralDescription;
    private BigDecimal collateralValue;

    public Long getProductId(){return productId;}
    public void setProductId(Long v){productId=v;}
    public BigDecimal getAmount(){return amount;}
    public void setAmount(BigDecimal v){amount=v;}
    public Integer getDuration(){return duration;}
    public void setDuration(Integer v){duration=v;}
    public BigDecimal getMonthlyExpenses(){return monthlyExpenses;}
    public void setMonthlyExpenses(BigDecimal v){monthlyExpenses=v;}
    public BigDecimal getExistingMonthlyDebt(){return existingMonthlyDebt;}
    public void setExistingMonthlyDebt(BigDecimal v){existingMonthlyDebt=v;}
    public String getCollateralDescription(){return collateralDescription;}
    public void setCollateralDescription(String v){collateralDescription=v;}
    public BigDecimal getCollateralValue(){return collateralValue;}
    public void setCollateralValue(BigDecimal v){collateralValue=v;}
}
