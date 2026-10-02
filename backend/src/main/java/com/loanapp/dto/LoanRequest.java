package com.loanapp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import com.loanapp.model.LoanProduct;

public class LoanRequest {

    @NotNull(message = "Kiasi kinahitajika")
    @DecimalMin(value = "1000.00", message = "Kiasi kiwe angalau TZS 1,000")
    private Long loanProductId;

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
    private String collateralDescription;
    private BigDecimal collateralValue;
    private String collateralPhotoData;
    private String termsSnapshot;
    private String termsHash;

    public LoanRequest() {}

    public LoanRequest(BigDecimal amount, BigDecimal interestRate, Integer durationMonths,
                       String purpose, BigDecimal processingFee, Boolean lawyerRequired,
                       BigDecimal lawyerFee) {
        this.amount = amount;
        this.interestRate = interestRate;
        this.durationMonths = durationMonths;
        this.purpose = purpose;
        this.processingFee = processingFee;
        this.lawyerRequired = lawyerRequired;
        this.lawyerFee = lawyerFee;
    }

    public Long getLoanProductId() { return loanProductId; }
    public void setLoanProductId(Long v) { loanProductId=v; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }

    public Integer getDurationMonths() { return durationMonths; }
    public void setDurationMonths(Integer durationMonths) { this.durationMonths = durationMonths; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public BigDecimal getProcessingFee() { return processingFee; }
    public void setProcessingFee(BigDecimal processingFee) { this.processingFee = processingFee; }

    public Boolean getLawyerRequired() { return lawyerRequired; }
    public void setLawyerRequired(Boolean lawyerRequired) { this.lawyerRequired = lawyerRequired; }

    public BigDecimal getLawyerFee() { return lawyerFee; }
    public void setLawyerFee(BigDecimal lawyerFee) { this.lawyerFee = lawyerFee; }
    public String getCollateralDescription() { return collateralDescription; }
    public void setCollateralDescription(String v) { collateralDescription=v; }
    public BigDecimal getCollateralValue() { return collateralValue; }
    public void setCollateralValue(BigDecimal v) { collateralValue=v; }
    public String getTermsSnapshot(){return termsSnapshot;} public void setTermsSnapshot(String v){termsSnapshot=v;}
    public String getTermsHash(){return termsHash;} public void setTermsHash(String v){termsHash=v;}
    public String getCollateralPhotoData() { return collateralPhotoData; }
    public void setCollateralPhotoData(String v) { collateralPhotoData=v; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private BigDecimal amount;
        private BigDecimal interestRate;
        private Integer durationMonths;
        private String purpose;
        private BigDecimal processingFee;
        private Boolean lawyerRequired = false;
        private BigDecimal lawyerFee;

        public Builder amount(BigDecimal value) { this.amount = value; return this; }
        public Builder interestRate(BigDecimal value) { this.interestRate = value; return this; }
        public Builder durationMonths(Integer value) { this.durationMonths = value; return this; }
        public Builder purpose(String value) { this.purpose = value; return this; }
        public Builder processingFee(BigDecimal value) { this.processingFee = value; return this; }
        public Builder lawyerRequired(Boolean value) { this.lawyerRequired = value; return this; }
        public Builder lawyerFee(BigDecimal value) { this.lawyerFee = value; return this; }

        public LoanRequest build() {
            return new LoanRequest(amount, interestRate, durationMonths, purpose,
                    processingFee, lawyerRequired, lawyerFee);
        }
    }
}
