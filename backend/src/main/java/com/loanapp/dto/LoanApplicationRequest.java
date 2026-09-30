package com.loanapp.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class LoanApplicationRequest {
 @NotNull private Long productId;
 @NotNull @DecimalMin("1.00") private BigDecimal amount;
 @NotNull @Min(1) private Integer duration;
 @NotBlank @Size(max=500) private String purpose;
 @AssertTrue(message="Lazima ukubali masharti ya mkopo baada ya kuyasoma") private Boolean termsAccepted;

 public LoanApplicationRequest(){}
 public Long getProductId(){return productId;} public void setProductId(Long v){productId=v;}
 public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal v){amount=v;}
 public Integer getDuration(){return duration;} public void setDuration(Integer v){duration=v;}
 public String getPurpose(){return purpose;} public void setPurpose(String v){purpose=v;}
 public Boolean getTermsAccepted(){return termsAccepted;} public void setTermsAccepted(Boolean v){termsAccepted=v;}
}
