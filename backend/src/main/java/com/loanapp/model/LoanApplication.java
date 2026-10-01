package com.loanapp.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="loan_applications")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class LoanApplication {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="borrower_id",nullable=false) private User borrower;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="product_id",nullable=true) private LoanProduct product;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="lender_id") private User lender;
 @Column(nullable=false,precision=15,scale=2) private BigDecimal amount;
 @Column(nullable=false) private Integer duration;
 @Enumerated(EnumType.STRING) @Column(nullable=true,length=20) private LoanProduct.DurationUnit durationUnit;
 @Column(length=500) private String purpose;
 @Column(nullable=true,precision=15,scale=2) private BigDecimal monthlyExpenses=BigDecimal.ZERO;
 @Column(nullable=true,precision=15,scale=2) private BigDecimal existingMonthlyDebt=BigDecimal.ZERO;
 @Column(length=500) private String collateralDescription;
 @Lob @Column(columnDefinition="TEXT") private String collateralPhotoData;
 @Column(nullable=true,precision=15,scale=2) private BigDecimal collateralValue=BigDecimal.ZERO;
 @Column(nullable=true,precision=15,scale=2) private BigDecimal interestSnapshot=BigDecimal.ZERO;
 @Column(nullable=true,precision=15,scale=2) private BigDecimal processingFeeSnapshot=BigDecimal.ZERO;
 @Column(nullable=true,precision=15,scale=2) private BigDecimal lateFeeSnapshot=BigDecimal.ZERO;
 @Column(nullable=true,precision=15,scale=2) private BigDecimal totalRepaymentSnapshot=BigDecimal.ZERO;
 @Column(nullable=true,precision=15,scale=2) private BigDecimal installmentAmountSnapshot=BigDecimal.ZERO;
 @Column(nullable=true) private Integer installmentCountSnapshot=1;
 @Column(nullable=true) private Integer gracePeriodDaysSnapshot=0;
 @Column(nullable=true,length=30) private String interestTypeSnapshot;
 @Column(nullable=true,length=30) private String repaymentFrequencySnapshot;
 @Column(nullable=true,length=100) private String termsVersion;
 @Column(nullable=true) private Boolean termsAccepted=false;
 private LocalDateTime termsAcceptedAt;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=30) private Status status=Status.SUBMITTED;
 @Column(length=1000) private String rejectionReason;
 @JsonIgnore @OneToOne(fetch=FetchType.LAZY) @JoinColumn(name="loan_id") private Loan loan;
 @Column(nullable=false,updatable=false) private LocalDateTime createdAt;
 private LocalDateTime updatedAt;
 public LoanApplication(){}
 public Long getId(){return id;} public void setId(Long v){id=v;}
 public User getBorrower(){return borrower;} public void setBorrower(User v){borrower=v;}
 public LoanProduct getProduct(){return product;} public void setProduct(LoanProduct v){product=v;}
 public User getLender(){return lender;} public void setLender(User v){lender=v;}
 public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal v){amount=v;}
 public Integer getDuration(){return duration;} public void setDuration(Integer v){duration=v;}
 public Integer getDurationMonths(){return duration;} public void setDurationMonths(Integer v){duration=v;}
 public LoanProduct.DurationUnit getDurationUnit(){return durationUnit;} public void setDurationUnit(LoanProduct.DurationUnit v){durationUnit=v;}
 public String getPurpose(){return purpose;} public void setPurpose(String v){purpose=v;}
 public BigDecimal getMonthlyExpenses(){return monthlyExpenses;} public void setMonthlyExpenses(BigDecimal v){monthlyExpenses=v;}
 public BigDecimal getExistingMonthlyDebt(){return existingMonthlyDebt;} public void setExistingMonthlyDebt(BigDecimal v){existingMonthlyDebt=v;}
 public String getCollateralDescription(){return collateralDescription;} public void setCollateralDescription(String v){collateralDescription=v;}
 public String getCollateralPhotoData(){return collateralPhotoData;} public void setCollateralPhotoData(String v){collateralPhotoData=v;}
 public BigDecimal getCollateralValue(){return collateralValue;} public void setCollateralValue(BigDecimal v){collateralValue=v;}
 public BigDecimal getInterestSnapshot(){return interestSnapshot;} public void setInterestSnapshot(BigDecimal v){interestSnapshot=v;}
 public BigDecimal getProcessingFeeSnapshot(){return processingFeeSnapshot;} public void setProcessingFeeSnapshot(BigDecimal v){processingFeeSnapshot=v;}
 public BigDecimal getLateFeeSnapshot(){return lateFeeSnapshot;} public void setLateFeeSnapshot(BigDecimal v){lateFeeSnapshot=v;}
 public BigDecimal getTotalRepaymentSnapshot(){return totalRepaymentSnapshot;} public void setTotalRepaymentSnapshot(BigDecimal v){totalRepaymentSnapshot=v;}
 public BigDecimal getInstallmentAmountSnapshot(){return installmentAmountSnapshot;} public void setInstallmentAmountSnapshot(Integer v){installmentAmountSnapshot=BigDecimal.valueOf(v); } public void setInstallmentAmountSnapshot(BigDecimal v){installmentAmountSnapshot=v;}
 public Integer getInstallmentCountSnapshot(){return installmentCountSnapshot;} public void setInstallmentCountSnapshot(Integer v){installmentCountSnapshot=v;}
 public Integer getGracePeriodDaysSnapshot(){return gracePeriodDaysSnapshot;} public void setGracePeriodDaysSnapshot(Integer v){gracePeriodDaysSnapshot=v;}
 public String getInterestTypeSnapshot(){return interestTypeSnapshot;} public void setInterestTypeSnapshot(String v){interestTypeSnapshot=v;}
 public String getRepaymentFrequencySnapshot(){return repaymentFrequencySnapshot;} public void setRepaymentFrequencySnapshot(String v){repaymentFrequencySnapshot=v;}
 public String getTermsVersion(){return termsVersion;} public void setTermsVersion(String v){termsVersion=v;}
 public Boolean getTermsAccepted(){return termsAccepted;} public void setTermsAccepted(Boolean v){termsAccepted=v;}
 public LocalDateTime getTermsAcceptedAt(){return termsAcceptedAt;} public void setTermsAcceptedAt(LocalDateTime v){termsAcceptedAt=v;}
 public Status getStatus(){return status;} public void setStatus(Status v){status=v;}
 public String getRejectionReason(){return rejectionReason;} public void setRejectionReason(String v){rejectionReason=v;}
 public Loan getLoan(){return loan;} public void setLoan(Loan v){loan=v;}
 public LocalDateTime getCreatedAt(){return createdAt;} public LocalDateTime getUpdatedAt(){return updatedAt;}
 @PrePersist void create(){createdAt=LocalDateTime.now();updatedAt=LocalDateTime.now();}
 @PreUpdate void update(){updatedAt=LocalDateTime.now();}
 public enum Status { SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED, CONVERTED }
}
