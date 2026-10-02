package com.loanapp.dto;
import com.loanapp.model.LoanProduct.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public class LoanProductRequest{
 @NotBlank private String name;
 @NotNull private LoanType loanType;
 @NotNull @DecimalMin("0.01") private BigDecimal minAmount;
 @NotNull @DecimalMin("0.01") private BigDecimal maxAmount;
 @NotNull @Min(1) private Integer minDuration;
 @NotNull @Min(1) private Integer maxDuration;
 @NotNull private DurationUnit durationUnit;
 @NotNull @DecimalMin("0") private BigDecimal interestRate;
 @NotNull private InterestType interestType;
 @DecimalMin("0") private BigDecimal processingFee=BigDecimal.ZERO;
 @DecimalMin("0") private BigDecimal lateFee=BigDecimal.ZERO;
 @DecimalMin("0") private BigDecimal otherCharges=BigDecimal.ZERO;
 private String currency="TZS";
 @Min(0) private Integer gracePeriodDays=0;
 @NotNull private RepaymentFrequency repaymentFrequency;
 public String getName(){return name;} public void setName(String v){name=v;}
 public LoanType getLoanType(){return loanType;} public void setLoanType(LoanType v){loanType=v;}
 public BigDecimal getMinAmount(){return minAmount;} public void setMinAmount(BigDecimal v){minAmount=v;}
 public BigDecimal getMaxAmount(){return maxAmount;} public void setMaxAmount(BigDecimal v){maxAmount=v;}
 public Integer getMinDuration(){return minDuration;} public void setMinDuration(Integer v){minDuration=v;}
 public Integer getMaxDuration(){return maxDuration;} public void setMaxDuration(Integer v){maxDuration=v;}
 public DurationUnit getDurationUnit(){return durationUnit;} public void setDurationUnit(DurationUnit v){durationUnit=v;}
 public BigDecimal getInterestRate(){return interestRate;} public void setInterestRate(BigDecimal v){interestRate=v;}
 public InterestType getInterestType(){return interestType;} public void setInterestType(InterestType v){interestType=v;}
 public BigDecimal getProcessingFee(){return processingFee;} public void setProcessingFee(BigDecimal v){processingFee=v;}
 public BigDecimal getLateFee(){return lateFee;} public void setLateFee(BigDecimal v){lateFee=v;}
 public BigDecimal getOtherCharges(){return otherCharges;} public void setOtherCharges(BigDecimal v){otherCharges=v;}
 public String getCurrency(){return currency;} public void setCurrency(String v){currency=v;}
 public Integer getGracePeriodDays(){return gracePeriodDays;} public void setGracePeriodDays(Integer v){gracePeriodDays=v;}
 public RepaymentFrequency getRepaymentFrequency(){return repaymentFrequency;} public void setRepaymentFrequency(RepaymentFrequency v){repaymentFrequency=v;}
}