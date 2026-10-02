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
  int effectiveDays=loan.getDurationUnit()==LoanProduct.DurationUnit.DAYS?periods:Math.max(1,periods*30);
  int count=switch(product.getRepaymentFrequency()){
   case ONE_TIME -> 1;
   case DAILY -> effectiveDays;
   case WEEKLY -> Math.max(1,(int)Math.ceil(effectiveDays/7.0));
   case MONTHLY -> loan.getDurationUnit()==LoanProduct.DurationUnit.MONTHS?periods:Math.max(1,(int)Math.ceil(effectiveDays/30.0));
  };
  BigDecimal principal=loan.getAmount().setScale(2,RoundingMode.HALF_UP);
  BigDecimal fees=Optional.ofNullable(loan.getProcessingFee()).orElse(BigDecimal.ZERO).add(Optional.ofNullable(loan.getLawyerFee()).orElse(BigDecimal.ZERO));
  BigDecimal rate=product.getInterestRate().divide(BigDecimal.valueOf(100),12,RoundingMode.HALF_UP);
  BigDecimal periodicRate=switch(product.getRepaymentFrequency()){
   case DAILY -> rate.divide(BigDecimal.valueOf(365),12,RoundingMode.HALF_UP);
   case WEEKLY -> rate.divide(BigDecimal.valueOf(52),12,RoundingMode.HALF_UP);
   default -> rate.divide(BigDecimal.valueOf(12),12,RoundingMode.HALF_UP);
  };
  BigDecimal totalInterest=loan.getTotalRepayment().subtract(principal).subtract(fees).max(BigDecimal.ZERO);
  List<RepaymentSchedule> out=new ArrayList<>();
  BigDecimal balance=principal;
  BigDecimal equalPrincipal=principal.divide(BigDecimal.valueOf(count),10,RoundingMode.HALF_UP);
  BigDecimal equalInterest=totalInterest.divide(BigDecimal.valueOf(count),10,RoundingMode.HALF_UP);
  BigDecimal reducingInstallment=periodicRate.signum()==0?principal.divide(BigDecimal.valueOf(count),10,RoundingMode.HALF_UP)
    :principal.multiply(periodicRate).multiply(BigDecimal.ONE.add(periodicRate).pow(count))
      .divide(BigDecimal.ONE.add(periodicRate).pow(count).subtract(BigDecimal.ONE),10,RoundingMode.HALF_UP);
  for(int i=1;i<=count;i++){
   BigDecimal interest;
   BigDecimal principalDue;
   if(product.getInterestType()==LoanProduct.InterestType.REDUCING_BALANCE){
    interest=balance.multiply(periodicRate).setScale(2,RoundingMode.HALF_UP);
    principalDue=reducingInstallment.subtract(interest).max(BigDecimal.ZERO);
    if(i==count) principalDue=balance;
   } else {
    interest=i==count?totalInterest.subtract(equalInterest.multiply(BigDecimal.valueOf(count-1))).setScale(2,RoundingMode.HALF_UP):equalInterest.setScale(2,RoundingMode.HALF_UP);
    principalDue=i==count?balance:equalPrincipal.setScale(2,RoundingMode.HALF_UP);
   }
   balance=balance.subtract(principalDue).max(BigDecimal.ZERO);
   BigDecimal fee=i==1?fees:BigDecimal.ZERO;
   LocalDate due=scheduleDate(startDate,i,periods,product.getRepaymentFrequency(),product.getDurationUnit());
   RepaymentSchedule s=new RepaymentSchedule();s.setLoan(loan);s.setInstallmentNumber(i);s.setDueDate(due);s.setPrincipalDue(principalDue);s.setInterestDue(interest);s.setFeesDue(fee);s.setAmountDue(principalDue.add(interest).add(fee).setScale(2,RoundingMode.HALF_UP));out.add(repo.save(s));
  }
  return out;
 }
 private LocalDate scheduleDate(LocalDate start,int i,int duration,LoanProduct.RepaymentFrequency f,LoanProduct.DurationUnit unit){
  return switch(f){
   case ONE_TIME -> unit==LoanProduct.DurationUnit.DAYS?start.plusDays(duration):start.plusMonths(duration);
   case DAILY -> start.plusDays(i);
   case WEEKLY -> start.plusWeeks(i);
   case MONTHLY -> start.plusMonths(i);
  };
 }

 public List<RepaymentSchedule> byLoan(Loan loan){return repo.findByLoanOrderByInstallmentNumberAsc(loan);}
 public void markOverdue(){repo.findByStatusAndDueDateBefore(RepaymentSchedule.ScheduleStatus.PENDING,LocalDate.now()).forEach(s->{s.setStatus(RepaymentSchedule.ScheduleStatus.OVERDUE);repo.save(s);});}
}