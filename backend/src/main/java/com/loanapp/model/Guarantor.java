package com.loanapp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "guarantors")
public class Guarantor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "loan_id", nullable = false) private Loan loan;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "guarantor_id", nullable = false) private User guarantor;
    @Column(nullable = false, precision = 15, scale = 2) private BigDecimal guaranteedAmount;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private GuarantorStatus status = GuarantorStatus.PENDING;
    @Column(length = 500) private String relationship;
    private LocalDateTime approvedAt;
    @Column(nullable = false, updatable = false) private LocalDateTime createdAt;

    public Guarantor() {}
    public Guarantor(Long id, Loan loan, User guarantor, BigDecimal guaranteedAmount, GuarantorStatus status, String relationship, LocalDateTime approvedAt, LocalDateTime createdAt) {
        this.id=id; this.loan=loan; this.guarantor=guarantor; this.guaranteedAmount=guaranteedAmount; this.status=status; this.relationship=relationship; this.approvedAt=approvedAt; this.createdAt=createdAt;
    }
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private Loan loan; private User guarantor; private BigDecimal guaranteedAmount;
        private GuarantorStatus status = GuarantorStatus.PENDING; private String relationship; private LocalDateTime approvedAt, createdAt;
        public Builder id(Long v){id=v;return this;} public Builder loan(Loan v){loan=v;return this;} public Builder guarantor(User v){guarantor=v;return this;}
        public Builder guaranteedAmount(BigDecimal v){guaranteedAmount=v;return this;} public Builder status(GuarantorStatus v){status=v;return this;}
        public Builder relationship(String v){relationship=v;return this;} public Builder approvedAt(LocalDateTime v){approvedAt=v;return this;} public Builder createdAt(LocalDateTime v){createdAt=v;return this;}
        public Guarantor build(){return new Guarantor(id,loan,guarantor,guaranteedAmount,status,relationship,approvedAt,createdAt);}
    }
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public Loan getLoan(){return loan;} public void setLoan(Loan v){loan=v;}
    public User getGuarantor(){return guarantor;} public void setGuarantor(User v){guarantor=v;}
    public BigDecimal getGuaranteedAmount(){return guaranteedAmount;} public void setGuaranteedAmount(BigDecimal v){guaranteedAmount=v;}
    public GuarantorStatus getStatus(){return status;} public void setStatus(GuarantorStatus v){status=v;}
    public String getRelationship(){return relationship;} public void setRelationship(String v){relationship=v;}
    public LocalDateTime getApprovedAt(){return approvedAt;} public void setApprovedAt(LocalDateTime v){approvedAt=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
    @PrePersist protected void onCreate(){if(createdAt==null) createdAt=LocalDateTime.now();}
    public enum GuarantorStatus { PENDING, APPROVED, REJECTED, RELEASED }
}