package com.loanapp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="loan_applications")
public class LoanApplication {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="borrower_id",nullable=false) private User borrower;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="product_id",nullable=false) private LoanProduct product;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="lender_id") private User lender;
 @Column(nullable=false,precision=15,scale=2) private BigDecimal amount;
 @Column(nullable=false) private Integer durationMonths;
 @Column(length=500) private String purpose;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private Status status=Status.SUBMITTED;
 @Column(length=1000) private String rejectionReason;
 @OneToOne(fetch=FetchType.LAZY) @JoinColumn(name="loan_id") private Loan loan;
 @Column(nullable=false,updatable=false) private LocalDateTime createdAt;
 private LocalDateTime updatedAt;
 public LoanApplication(){}
 public Long getId(){return id;} public void setId(Long v){id=v;}
 public User getBorrower(){return borrower;} public void setBorrower(User v){borrower=v;}
 public LoanProduct getProduct(){return product;} public void setProduct(LoanProduct v){product=v;}
 public User getLender(){return lender;} public void setLender(User v){lender=v;}
 public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal v){amount=v;}
 public Integer getDurationMonths(){return durationMonths;} public void setDurationMonths(Integer v){durationMonths=v;}
 public String getPurpose(){return purpose;} public void setPurpose(String v){purpose=v;}
 public Status getStatus(){return status;} public void setStatus(Status v){status=v;}
 public String getRejectionReason(){return rejectionReason;} public void setRejectionReason(String v){rejectionReason=v;}
 public Loan getLoan(){return loan;} public void setLoan(Loan v){loan=v;}
 public LocalDateTime getCreatedAt(){return createdAt;} public LocalDateTime getUpdatedAt(){return updatedAt;}
 @PrePersist void create(){createdAt=LocalDateTime.now();updatedAt=LocalDateTime.now();}
 @PreUpdate void update(){updatedAt=LocalDateTime.now();}
 public enum Status { SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED, CONVERTED }
}
