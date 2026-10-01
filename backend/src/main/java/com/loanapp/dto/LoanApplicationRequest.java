package com.loanapp.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class LoanApplicationRequest {
 @DecimalMin("1.00") @NotNull private BigDecimal amount;
 @NotNull @Min(1) private Integer duration;
 @NotBlank @Size(max=500) private String purpose;
 private Long productId;
 @NotNull @DecimalMin("0.00") private BigDecimal monthlyExpenses;
 @NotNull @DecimalMin("0.00") private BigDecimal existingMonthlyDebt;
 @Size(max=500) private String collateralDescription;
 @DecimalMin("0.00") private BigDecimal collateralValue=BigDecimal.ZERO;
 @Size(max=5000000) private String collateralPhotoData;
 private Boolean termsAccepted = false;

 public LoanApplicationRequest(){}
 public Long getProductId(){return productId;} public void setProductId(Long v){productId=v;}
 public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal v){amount=v;}
 public Integer getDuration(){return duration;} public void setDuration(Integer v){duration=v;}
 public String getPurpose(){return purpose;} public void setPurpose(String v){purpose=v;}
 public BigDecimal getMonthlyExpenses(){return monthlyExpenses;} public void setMonthlyExpenses(BigDecimal v){monthlyExpenses=v;}
 public BigDecimal getExistingMonthlyDebt(){return existingMonthlyDebt;} public void setExistingMonthlyDebt(BigDecimal v){existingMonthlyDebt=v;}
 public String getCollateralDescription(){return collateralDescription;} public void setCollateralDescription(String v){collateralDescription=v;}
 public BigDecimal getCollateralValue(){return collateralValue;} public void setCollateralValue(BigDecimal v){collateralValue=v;}
 public String getCollateralPhotoData(){return collateralPhotoData;} public void setCollateralPhotoData(String v){collateralPhotoData=v;}
 public Boolean getTermsAccepted(){return termsAccepted;} public void setTermsAccepted(Boolean v){termsAccepted=v;}
}
