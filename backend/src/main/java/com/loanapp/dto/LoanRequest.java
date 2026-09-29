package com.loanapp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class LoanRequest {

    @NotNull(message = "Kiasi kinahitajika")
    @DecimalMin(value = "1000.00", message = "Kiasi kiwe angalau TZS 1,000")
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
