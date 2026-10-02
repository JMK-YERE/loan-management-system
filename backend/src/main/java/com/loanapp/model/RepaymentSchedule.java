package com.loanapp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="repayment_schedules",uniqueConstraints=@UniqueConstraint(columnNames={"loan_id","installment_number"}))
public class RepaymentSchedule {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="loan_id") private Loan loan;
 @Column(nullable=false) private Integer installmentNumber;
 @Column(nullable=false) private LocalDate dueDate;
 @Column(nullable=false,precision=15,scale=2) private BigDecimal principalDue;
 @Column(nullable=false,precision=15,scale=2) private BigDecimal interestDue;
 @Column(nullable=false,precision=15,scale=2) private BigDecimal feesDue;
 @Column(nullable=false,precision=15,scale=2) private BigDecimal amountDue;
 @Column(nullable=false,precision=15,scale=2) private BigDecimal amountPaid=BigDecimal.ZERO;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private ScheduleStatus status=ScheduleStatus.PENDING;
 private LocalDateTime paidAt;
 private LocalDateTime lastReminderAt;
 public RepaymentSchedule(){}
 public Long getId(){return id;} public void setId(Long v){id=v;}
 public Loan getLoan(){return loan;} public void setLoan(Loan v){loan=v;}
 public Integer getInstallmentNumber(){return installmentNumber;} public void setInstallmentNumber(Integer v){installmentNumber=v;}
 public LocalDate getDueDate(){return dueDate;} public void setDueDate(LocalDate v){dueDate=v;}
 public BigDecimal getPrincipalDue(){return principalDue;} public void setPrincipalDue(BigDecimal v){principalDue=v;}
 public BigDecimal getInterestDue(){return interestDue;} public void setInterestDue(BigDecimal v){interestDue=v;}
 public BigDecimal getFeesDue(){return feesDue;} public void setFeesDue(BigDecimal v){feesDue=v;}
 public BigDecimal getAmountDue(){return amountDue;} public void setAmountDue(BigDecimal v){amountDue=v;}
 public BigDecimal getAmountPaid(){return amountPaid;} public void setAmountPaid(BigDecimal v){amountPaid=v;}
 public ScheduleStatus getStatus(){return status;} public void setStatus(ScheduleStatus v){status=v;}
 public LocalDateTime getPaidAt(){return paidAt;} public void setPaidAt(LocalDateTime v){paidAt=v;}
 public LocalDateTime getLastReminderAt(){return lastReminderAt;} public void setLastReminderAt(LocalDateTime v){lastReminderAt=v;}
 public enum ScheduleStatus{PENDING,PARTIALLY_PAID,PAID,OVERDUE,WAIVED}
}