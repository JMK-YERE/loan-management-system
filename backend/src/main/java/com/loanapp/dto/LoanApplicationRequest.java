package com.loanapp.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public class LoanApplicationRequest {
 @NotNull private Long productId;
 @NotNull @DecimalMin("1.00") private BigDecimal amount;
 @NotNull @Min(1) private Integer durationMonths;
 @NotBlank @Size(max=500) private String purpose;
 public LoanApplicationRequest(){}
 public Long getProductId(){return productId;} public void setProductId(Long v){productId=v;}
 public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal v){amount=v;}
 public Integer getDurationMonths(){return durationMonths;} public void setDurationMonths(Integer v){durationMonths=v;}
 public String getPurpose(){return purpose;} public void setPurpose(String v){purpose=v;}
}
