package com.loanapp.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class LoanQuoteRequest {
    @NotNull private Long productId;
    @NotNull @DecimalMin("1.00") private BigDecimal amount;
    @NotNull @Min(1) private Integer duration;

    public Long getProductId(){return productId;}
    public void setProductId(Long v){productId=v;}
    public BigDecimal getAmount(){return amount;}
    public void setAmount(BigDecimal v){amount=v;}
    public Integer getDuration(){return duration;}
    public void setDuration(Integer v){duration=v;}
}
