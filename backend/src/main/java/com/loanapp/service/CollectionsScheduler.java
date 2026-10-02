package com.loanapp.service;

import com.loanapp.model.RepaymentSchedule;
import com.loanapp.model.LoanProduct;
import com.loanapp.model.User;
import com.loanapp.repository.RepaymentScheduleRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.math.BigDecimal;

@Component
public class CollectionsScheduler {
 private final RepaymentScheduleRepository schedules;
 private final NotificationService notifications;
 public CollectionsScheduler(RepaymentScheduleRepository schedules, NotificationService notifications){this.schedules=schedules;this.notifications=notifications;}

 @Scheduled(cron="0 15 7 * * *", zone="Africa/Dar_es_Salaam")
 public void markOverdueAndNotify(){
  schedules.findByStatusAndDueDateBefore(RepaymentSchedule.ScheduleStatus.PENDING, LocalDate.now()).forEach(s -> {
   LoanProduct product=s.getLoan().getLoanProduct();
   if(product!=null && s.getPenaltyAppliedAt()==null && LocalDate.now().isAfter(s.getDueDate().plusDays(product.getGracePeriodDays()))){
    BigDecimal penalty=product.getLateFee()==null?BigDecimal.ZERO:product.getLateFee();
    if(penalty.signum()>0){s.setLatePenalty(penalty);s.setFeesDue(s.getFeesDue().add(penalty));s.setAmountDue(s.getAmountDue().add(penalty));s.setPenaltyAppliedAt(java.time.LocalDateTime.now());}
   }
   s.setStatus(RepaymentSchedule.ScheduleStatus.OVERDUE);
   schedules.save(s);
   User borrower=s.getLoan().getBorrower();
   notifications.sendOverdue(borrower,"Malipo ya TZS "+s.getAmountDue()+" kwa mkopo #"+s.getLoan().getId()+" yamepita tarehe ya malipo.");
  });
 }
}
