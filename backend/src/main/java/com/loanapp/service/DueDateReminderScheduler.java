package com.loanapp.service;

import com.loanapp.model.RepaymentSchedule;
import com.loanapp.repository.RepaymentScheduleRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DueDateReminderScheduler {
 private final RepaymentScheduleRepository schedules;
 private final NotificationService notifications;
 public DueDateReminderScheduler(RepaymentScheduleRepository schedules,NotificationService notifications){this.schedules=schedules;this.notifications=notifications;}

 @Scheduled(cron="${app.due-reminders.cron:0 0 9 * * *}", zone="Africa/Dar_es_Salaam")
 public void run(){
  LocalDate today=LocalDate.now();
  LocalDate until=today.plusDays(3);
  schedules.findAll().stream()
    .filter(s->(s.getStatus()==RepaymentSchedule.ScheduleStatus.PENDING||s.getStatus()==RepaymentSchedule.ScheduleStatus.PARTIALLY_PAID))
    .filter(s->s.getDueDate()!=null&&!s.getDueDate().isBefore(today)&&!s.getDueDate().isAfter(until))
    .filter(s->s.getLastReminderAt()==null||s.getLastReminderAt().isBefore(LocalDateTime.now().minusHours(23)))
    .forEach(s->{
      var u=s.getLoan().getBorrower();
      long days=java.time.temporal.ChronoUnit.DAYS.between(today,s.getDueDate());
      String msg=days==0
        ?"JmkLoanApp: Leo ni tarehe ya malipo ya TZS "+s.getAmountDue()+" kwa mkopo #"+s.getLoan().getId()+". Tafadhali lipa kupitia mfumo."
        :"JmkLoanApp: Kumbusho la malipo ya TZS "+s.getAmountDue()+" kwa mkopo #"+s.getLoan().getId()+". Due date ni "+s.getDueDate()+" (imebaki siku "+days+").";
      notifications.sendSms(u.getPhone(),msg);
      notifications.sendWhatsApp(u.getPhone(),msg);
      if(u.getEmail()!=null&&!u.getEmail().isBlank()) notifications.sendEmail(u.getEmail(),"JmkLoanApp - Kumbusho la Malipo","<p>Habari "+u.getFullName()+",</p><p>"+msg+"</p>");
      s.setLastReminderAt(LocalDateTime.now());
      schedules.save(s);
    });
 }
}
