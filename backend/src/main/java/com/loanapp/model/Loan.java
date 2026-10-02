package com.loanapp.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id", nullable = false)
    private User borrower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lender_id", nullable = false)
    private User lender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_product_id")
    private LoanProduct loanProduct;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private LoanProduct.DurationUnit durationUnit = LoanProduct.DurationUnit.MONTHS;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(nullable = false)
    private Integer durationMonths;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalRepayment;

    @Column(precision = 15, scale = 2)
    private BigDecimal processingFee = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    private BigDecimal lawyerFee = BigDecimal.ZERO;

    @Column(nullable = false)
    private Boolean lawyerRequired = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LoanStatus status = LoanStatus.PENDING;

    @Column(length = 500)
    private String purpose;

    @Column(length = 500)
    private String collateralDescription;

    @Column(precision = 15, scale = 2)
    private BigDecimal collateralValue = BigDecimal.ZERO;

    @JsonIgnore
    @Lob
    @Column(columnDefinition = "TEXT")
    private String collateralPhotoData;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDate disbursementDate;
    private LocalDate nextDueDate;
    private LocalDateTime lastOverdueReminderAt;
    private LocalDateTime updatedAt;
    @Lob @Column(columnDefinition="TEXT") private String termsSnapshot;
    @Column(length=128) private String termsHash;

    public Loan() {}

    public Loan(Long id, User borrower, User lender, BigDecimal amount, BigDecimal interestRate,
                Integer durationMonths, BigDecimal totalRepayment, BigDecimal processingFee,
                BigDecimal lawyerFee, Boolean lawyerRequired, LoanStatus status, String purpose,
                LocalDateTime createdAt, LocalDate disbursementDate, LocalDate nextDueDate,
                LocalDateTime lastOverdueReminderAt, LocalDateTime updatedAt) {
        this.id = id;
        this.borrower = borrower;
        this.lender = lender;
        this.amount = amount;
        this.interestRate = interestRate;
        this.durationMonths = durationMonths;
        this.totalRepayment = totalRepayment;
        this.processingFee = processingFee;
        this.lawyerFee = lawyerFee;
        this.lawyerRequired = lawyerRequired;
        this.status = status;
        this.purpose = purpose;
        this.createdAt = createdAt;
        this.disbursementDate = disbursementDate;
        this.nextDueDate = nextDueDate;
        this.lastOverdueReminderAt = lastOverdueReminderAt;
        this.updatedAt = updatedAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private User borrower;
        private User lender;
        private BigDecimal amount;
        private BigDecimal interestRate;
        private Integer durationMonths;
        private BigDecimal totalRepayment;
        private BigDecimal processingFee = BigDecimal.ZERO;
        private BigDecimal lawyerFee = BigDecimal.ZERO;
        private Boolean lawyerRequired = false;
        private LoanStatus status = LoanStatus.PENDING;
        private String purpose;
        private LocalDateTime createdAt;
        private LocalDate disbursementDate;
        private LocalDate nextDueDate;
        private LocalDateTime lastOverdueReminderAt;
        private LocalDateTime updatedAt;

        public Builder id(Long v) { id=v; return this; }
        public Builder borrower(User v) { borrower=v; return this; }
        public Builder lender(User v) { lender=v; return this; }
        public Builder amount(BigDecimal v) { amount=v; return this; }
        public Builder interestRate(BigDecimal v) { interestRate=v; return this; }
        public Builder durationMonths(Integer v) { durationMonths=v; return this; }
        public Builder totalRepayment(BigDecimal v) { totalRepayment=v; return this; }
        public Builder processingFee(BigDecimal v) { processingFee=v; return this; }
        public Builder lawyerFee(BigDecimal v) { lawyerFee=v; return this; }
        public Builder lawyerRequired(Boolean v) { lawyerRequired=v; return this; }
        public Builder status(LoanStatus v) { status=v; return this; }
        public Builder purpose(String v) { purpose=v; return this; }
        public Builder createdAt(LocalDateTime v) { createdAt=v; return this; }
        public Builder disbursementDate(LocalDate v) { disbursementDate=v; return this; }
        public Builder nextDueDate(LocalDate v) { nextDueDate=v; return this; }
        public Builder lastOverdueReminderAt(LocalDateTime v) { lastOverdueReminderAt=v; return this; }
        public Builder updatedAt(LocalDateTime v) { updatedAt=v; return this; }

        public Loan build() {
            return new Loan(id, borrower, lender, amount, interestRate, durationMonths,
                    totalRepayment, processingFee, lawyerFee, lawyerRequired, status, purpose,
                    createdAt, disbursementDate, nextDueDate, lastOverdueReminderAt, updatedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long v) { id=v; }
    public User getBorrower() { return borrower; }
    public void setBorrower(User v) { borrower=v; }
    public User getLender() { return lender; }
    public void setLender(User v) { lender=v; }
    public LoanProduct getLoanProduct() { return loanProduct; }
    public void setLoanProduct(LoanProduct v) { loanProduct=v; }
    public LoanProduct.DurationUnit getDurationUnit() { return durationUnit; }
    public void setDurationUnit(LoanProduct.DurationUnit v) { durationUnit=v; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal v) { amount=v; }
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal v) { interestRate=v; }
    public Integer getDurationMonths() { return durationMonths; }
    public void setDurationMonths(Integer v) { durationMonths=v; }
    public BigDecimal getTotalRepayment() { return totalRepayment; }
    public void setTotalRepayment(BigDecimal v) { totalRepayment=v; }
    public BigDecimal getProcessingFee() { return processingFee; }
    public void setProcessingFee(BigDecimal v) { processingFee=v; }
    public BigDecimal getLawyerFee() { return lawyerFee; }
    public void setLawyerFee(BigDecimal v) { lawyerFee=v; }
    public Boolean getLawyerRequired() { return lawyerRequired; }
    public void setLawyerRequired(Boolean v) { lawyerRequired=v; }
    public LoanStatus getStatus() { return status; }
    public void setStatus(LoanStatus v) { status=v; }
    public String getPurpose() { return purpose; }
    public void setPurpose(String v) { purpose=v; }
    public String getCollateralDescription() { return collateralDescription; }
    public void setCollateralDescription(String v) { collateralDescription=v; }
    public BigDecimal getCollateralValue() { return collateralValue; }
    public void setCollateralValue(BigDecimal v) { collateralValue=v; }
    public String getCollateralPhotoData() { return collateralPhotoData; }
    public void setCollateralPhotoData(String v) { collateralPhotoData=v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { createdAt=v; }
    public LocalDate getDisbursementDate() { return disbursementDate; }
    public void setDisbursementDate(LocalDate v) { disbursementDate=v; }
    public LocalDate getNextDueDate() { return nextDueDate; }
    public void setNextDueDate(LocalDate v) { nextDueDate=v; }
    public LocalDateTime getLastOverdueReminderAt() { return lastOverdueReminderAt; }
    public void setLastOverdueReminderAt(LocalDateTime v) { lastOverdueReminderAt=v; }
    public String getTermsSnapshot(){return termsSnapshot;} public void setTermsSnapshot(String v){termsSnapshot=v;}
    public String getTermsHash(){return termsHash;} public void setTermsHash(String v){termsHash=v;}
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime v) { updatedAt=v; }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum LoanStatus {
        PENDING, APPROVED, REJECTED, DISBURSED, PAID, DEFAULTED
    }
}
