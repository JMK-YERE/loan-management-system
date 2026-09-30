package com.loanapp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="payment_allocations", uniqueConstraints=@UniqueConstraint(name="uk_payment_schedule_allocation", columnNames={"payment_id","schedule_id"}))
public class PaymentAllocation {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="payment_id") private Payment payment;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="schedule_id") private RepaymentSchedule schedule;
 @Column(nullable=false,precision=15,scale=2) private BigDecimal amount;
 @Column(nullable=false,updatable=false) private LocalDateTime allocatedAt;
 public PaymentAllocation(){}
 public PaymentAllocation(Payment p,RepaymentSchedule s,BigDecimal a){payment=p;schedule=s;amount=a;allocatedAt=LocalDateTime.now();}
 public Long getId(){return id;} public Payment getPayment(){return payment;} public RepaymentSchedule getSchedule(){return schedule;} public BigDecimal getAmount(){return amount;} public LocalDateTime getAllocatedAt(){return allocatedAt;}
}
