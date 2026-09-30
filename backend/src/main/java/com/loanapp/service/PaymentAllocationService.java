package com.loanapp.service;

import com.loanapp.model.*;
import com.loanapp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.*;
import java.time.LocalDate;
import java.util.*;

@Service
public class PaymentAllocationService {
 private final RepaymentScheduleRepository schedules;
 private final PaymentAllocationRepository allocations;
 public PaymentAllocationService(RepaymentScheduleRepository s,PaymentAllocationRepository a){schedules=s;allocations=a;}

 @Transactional
 public void allocate(Payment payment){
  if(payment==null||payment.getStatus()!=Payment.PaymentStatus.SUCCESS)return;
  if(!allocations.findByPayment(payment).isEmpty())return;
  BigDecimal remaining=payment.getAmount();
  for(RepaymentSchedule s:schedules.findByLoanOrderByInstallmentNumberAsc(payment.getLoan())){
   BigDecimal outstanding=s.getAmountDue().subtract(Optional.ofNullable(s.getAmountPaid()).orElse(BigDecimal.ZERO)).max(BigDecimal.ZERO);
   if(outstanding.signum()<=0)continue;
   BigDecimal applied=remaining.min(outstanding);
   if(applied.signum()<=0)break;
   allocations.save(new PaymentAllocation(payment,s,applied));
   s.setAmountPaid(s.getAmountPaid().add(applied));
   if(s.getAmountPaid().compareTo(s.getAmountDue())>=0){s.setAmountPaid(s.getAmountDue());s.setStatus(RepaymentSchedule.ScheduleStatus.PAID);s.setPaidAt(java.time.LocalDateTime.now());}
   else{s.setStatus(RepaymentSchedule.ScheduleStatus.PARTIALLY_PAID);}
   schedules.save(s);
   remaining=remaining.subtract(applied);
   if(remaining.signum()<=0)break;
  }
 }
}
