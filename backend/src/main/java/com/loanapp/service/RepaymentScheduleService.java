package com.loanapp.service;
import com.loanapp.model.*;
import com.loanapp.repository.RepaymentScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
@Service
public class RepaymentScheduleService{
 private final RepaymentScheduleRepository repo;
 public RepaymentScheduleService(RepaymentScheduleRepository repo){this.repo=repo;}
 @Transactional
 public List<RepaymentSchedule> generate(Loan loan,LoanProduct product,LocalDate startDate){
  List<RepaymentSchedule> old=repo.findByLoanOrderByInstallmentNumberAsc(loan); if(!old.isEmpty()) return old;
  int periods=loan.getDurationMonths();
  int count=product.getRepaymentFrequency()==LoanProduct.RepaymentFrequency.ONE_TIME?1:periods;
  if(product.getRepaymentFrequency()==LoanProduct.RepaymentFrequency.WEEKLY) count=Math.max(1,(int)Math.ceil(periods/4.345));
  if(product.getRepaymentFrequency()==LoanProduct.RepaymentFrequency.DAILY) count=periods;
  BigDecimal principal=loan.getAmount().setScale(2,RoundingMode.HALF_UP);
  BigDecimal totalInterest=loan.getTotalRepayment().subtract(principal).subtract(Optional.ofNullable(loan.getProcessingFee()).orElse(BigDecimal.ZERO)).subtract(Optional.ofNullable(loan.getLawyerFee()).orElse(BigDecimal.ZERO)).max(BigDecimal.ZERO);
  BigDecimal pEach=principal.divide(BigDecimal.valueOf(count),2,RoundingMode.DOWN);
  BigDecimal iEach=totalInterest.divide(BigDecimal.valueOf(count),2,RoundingMode.DOWN);
  List<RepaymentSchedule> out=new ArrayList<>(); BigDecimal pR=principal,iR=totalInterest;
  for(int i=1;i<=count;i++){
   BigDecimal p=i==count?pR:pEach, in=i==count?iR:iEach; pR=pR.subtract(p);iR=iR.subtract(in);
   LocalDate due=scheduleDate(startDate,i,periods,product.getRepaymentFrequency(),product.getDurationUnit());
   RepaymentSchedule s=new RepaymentSchedule();s.setLoan(loan);s.setInstallmentNumber(i);s.setDueDate(due);s.setPrincipalDue(p);s.setInterestDue(in);s.setFeesDue(i==1?Optional.ofNullable(loan.getProcessingFee()).orElse(BigDecimal.ZERO).add(Optional.ofNullable(loan.getLawyerFee()).orElse(BigDecimal.ZERO)):BigDecimal.ZERO);s.setAmountDue(s.getPrincipalDue().add(s.getInterestDue()).add(s.getFeesDue()));out.add(repo.save(s));
  }
  return out;
 }
 private LocalDate scheduleDate(LocalDate start,int i,int duration,LoanProduct.RepaymentFrequency f,LoanProduct.DurationUnit unit){
  return switch(f){case ONE_TIME->unit==LoanProduct.DurationUnit.DAYS?start.plusDays(duration):start.plusMonths(duration);case DAILY->start.plusDays(i);case WEEKLY->start.plusWeeks(i);case MONTHLY->start.plusMonths(i);};
 }
 public List<RepaymentSchedule> byLoan(Loan loan){return repo.findByLoanOrderByInstallmentNumberAsc(loan);}
 public void markOverdue(){repo.findByStatusAndDueDateBefore(RepaymentSchedule.ScheduleStatus.PENDING,LocalDate.now()).forEach(s->{s.setStatus(RepaymentSchedule.ScheduleStatus.OVERDUE);repo.save(s);});}
}