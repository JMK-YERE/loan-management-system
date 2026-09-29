package com.loanapp.service;

import com.loanapp.model.Loan; import com.loanapp.model.User; import com.loanapp.repository.LoanRepository;
import org.springframework.scheduling.annotation.Scheduled; import org.springframework.stereotype.Component; import java.time.*; import java.util.List;
@Component
public class OverdueReminderScheduler{
 private final LoanRepository loans; private final NotificationService notifications;
 public OverdueReminderScheduler(LoanRepository loans,NotificationService notifications){this.loans=loans;this.notifications=notifications;}
 @Scheduled(cron="${app.reminders.cron:0 0 8 * * *}")
 public void run(){
  LocalDate today=LocalDate.now();
  List<Loan> overdue=loans.findAll().stream().filter(l->l.getNextDueDate()!=null&&l.getNextDueDate().isBefore(today)&&l.getStatus()!=Loan.LoanStatus.PAID&&l.getStatus()!=Loan.LoanStatus.REJECTED).toList();
  for(Loan l:overdue){if(l.getLastOverdueReminderAt()==null||l.getLastOverdueReminderAt().isBefore(LocalDateTime.now().minusHours(23))){User u=l.getBorrower();notifications.sendOverdue(u,"Mkopo #"+l.getId()+" una malipo yaliyochelewa tangu "+l.getNextDueDate()+". Tafadhali wasiliana na mkopeshaji au fanya malipo kupitia mfumo.");l.setLastOverdueReminderAt(LocalDateTime.now());loans.save(l);}}
 }
}
