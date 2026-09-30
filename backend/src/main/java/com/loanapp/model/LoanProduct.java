package com.loanapp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="loan_products")
public class LoanProduct {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true,length=100) private String name;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private LoanType loanType;
 @Column(nullable=false,precision=15,scale=2) private BigDecimal minAmount;
 @Column(nullable=false,precision=15,scale=2) private BigDecimal maxAmount;
 @Column(nullable=false) private Integer minDuration;
 @Column(nullable=false) private Integer maxDuration;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private DurationUnit durationUnit;
 @Column(nullable=false,precision=7,scale=4) private BigDecimal interestRate;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private InterestType interestType=InterestType.FLAT;
 @Column(nullable=false,precision=15,scale=2) private BigDecimal processingFee=BigDecimal.ZERO;
 @Column(nullable=false,precision=15,scale=2) private BigDecimal lateFee=BigDecimal.ZERO;
 @Column(nullable=false) private Integer gracePeriodDays=0;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private RepaymentFrequency repaymentFrequency;
 @Column(nullable=false) private Boolean active=true;
 @Column(nullable=false,updatable=false) private LocalDateTime createdAt;
 public LoanProduct(){}
 public Long getId(){return id;} public void setId(Long v){id=v;}
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
 public Integer getGracePeriodDays(){return gracePeriodDays;} public void setGracePeriodDays(Integer v){gracePeriodDays=v;}
 public RepaymentFrequency getRepaymentFrequency(){return repaymentFrequency;} public void setRepaymentFrequency(RepaymentFrequency v){repaymentFrequency=v;}
 public Boolean getActive(){return active;} public void setActive(Boolean v){active=v;}
 public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
 @PrePersist void create(){if(createdAt==null)createdAt=LocalDateTime.now();}
 public enum LoanType{QUICK,INSTALLMENT}
 public enum DurationUnit{DAYS,MONTHS}
 public enum InterestType{FLAT,ANNUAL_SIMPLE}
 public enum RepaymentFrequency{ONE_TIME,DAILY,WEEKLY,MONTHLY}
}