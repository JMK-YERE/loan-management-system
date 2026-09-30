package com.loanapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="credit_assessments")
public class CreditAssessment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="application_id", nullable=false, unique=true)
    @JsonIgnore
    private LoanApplication application;

    @Column(nullable=false, precision=15, scale=2)
    private BigDecimal monthlyIncome = BigDecimal.ZERO;

    @Column(nullable=false, precision=15, scale=2)
    private BigDecimal monthlyExpenses = BigDecimal.ZERO;

    @Column(nullable=false, precision=15, scale=2)
    private BigDecimal existingMonthlyDebt = BigDecimal.ZERO;

    @Column(nullable=false, precision=15, scale=2)
    private BigDecimal monthlySurplus = BigDecimal.ZERO;

    @Column(nullable=false, precision=15, scale=2)
    private BigDecimal estimatedInstallment = BigDecimal.ZERO;

    @Column(nullable=false, precision=15, scale=2)
    private BigDecimal recommendedAmount = BigDecimal.ZERO;

    @Column(nullable=false)
    private Integer score;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private RiskLevel riskLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private Affordability affordability;

    @Column(nullable=false, length=1000)
    private String assessmentSummary;

    @Column(nullable=false, updatable=false)
    private LocalDateTime createdAt;

    @Column(nullable=false)
    private LocalDateTime updatedAt;

    public CreditAssessment() {}

    public Long getId(){return id;}
    public void setId(Long v){id=v;}
    public LoanApplication getApplication(){return application;}
    public void setApplication(LoanApplication v){application=v;}
    public BigDecimal getMonthlyIncome(){return monthlyIncome;}
    public void setMonthlyIncome(BigDecimal v){monthlyIncome=v;}
    public BigDecimal getMonthlyExpenses(){return monthlyExpenses;}
    public void setMonthlyExpenses(BigDecimal v){monthlyExpenses=v;}
    public BigDecimal getExistingMonthlyDebt(){return existingMonthlyDebt;}
    public void setExistingMonthlyDebt(BigDecimal v){existingMonthlyDebt=v;}
    public BigDecimal getMonthlySurplus(){return monthlySurplus;}
    public void setMonthlySurplus(BigDecimal v){monthlySurplus=v;}
    public BigDecimal getEstimatedInstallment(){return estimatedInstallment;}
    public void setEstimatedInstallment(BigDecimal v){estimatedInstallment=v;}
    public BigDecimal getRecommendedAmount(){return recommendedAmount;}
    public void setRecommendedAmount(BigDecimal v){recommendedAmount=v;}
    public Integer getScore(){return score;}
    public void setScore(Integer v){score=v;}
    public RiskLevel getRiskLevel(){return riskLevel;}
    public void setRiskLevel(RiskLevel v){riskLevel=v;}
    public Affordability getAffordability(){return affordability;}
    public void setAffordability(Affordability v){affordability=v;}
    public String getAssessmentSummary(){return assessmentSummary;}
    public void setAssessmentSummary(String v){assessmentSummary=v;}
    public LocalDateTime getCreatedAt(){return createdAt;}
    public LocalDateTime getUpdatedAt(){return updatedAt;}

    @PrePersist protected void onCreate(){
        LocalDateTime now=LocalDateTime.now();
        if(createdAt==null) createdAt=now;
        updatedAt=now;
    }
    @PreUpdate protected void onUpdate(){updatedAt=LocalDateTime.now();}

    public enum RiskLevel { LOW, MEDIUM, HIGH, REVIEW_REQUIRED }
    public enum Affordability { PASS, BORDERLINE, FAIL }
}