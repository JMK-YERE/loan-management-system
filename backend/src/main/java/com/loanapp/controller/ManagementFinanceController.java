package com.loanapp.controller;

import com.loanapp.model.Loan;
import com.loanapp.model.Payment;
import com.loanapp.model.RepaymentSchedule;
import com.loanapp.repository.LoanRepository;
import com.loanapp.repository.PaymentRepository;
import com.loanapp.repository.RepaymentScheduleRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/management/financial-statement")
@PreAuthorize("hasAnyRole('DIRECTOR','ADMIN')")
public class ManagementFinanceController {
 private final LoanRepository loans; private final PaymentRepository payments; private final RepaymentScheduleRepository schedules;
 public ManagementFinanceController(LoanRepository loans,PaymentRepository payments,RepaymentScheduleRepository schedules){this.loans=loans;this.payments=payments;this.schedules=schedules;}

 @GetMapping
 public Map<String,Object> statement(){
  List<Loan> ls=loans.findAll();List<Payment> ps=payments.findAll();LocalDate today=LocalDate.now();
  BigDecimal disbursed=sum(ls.stream().filter(l->EnumSet.of(Loan.LoanStatus.DISBURSED,Loan.LoanStatus.PAID,Loan.LoanStatus.DEFAULTED).contains(l.getStatus())).map(Loan::getAmount).toList());
  BigDecimal contracted=sum(ls.stream().filter(l->EnumSet.of(Loan.LoanStatus.DISBURSED,Loan.LoanStatus.PAID,Loan.LoanStatus.DEFAULTED).contains(l.getStatus())).map(Loan::getTotalRepayment).toList());
  BigDecimal received=sum(ps.stream().filter(p->p.getStatus()==Payment.PaymentStatus.SUCCESS).map(Payment::getAmount).toList());
  BigDecimal pending=sum(ps.stream().filter(p->p.getStatus()==Payment.PaymentStatus.PENDING).map(Payment::getAmount).toList());
  List<RepaymentSchedule> overdue=schedules.findByStatusAndDueDateBefore(RepaymentSchedule.ScheduleStatus.PENDING,today);
  BigDecimal overdueAmount=sum(overdue.stream().map(s->s.getAmountDue().subtract(Optional.ofNullable(s.getAmountPaid()).orElse(BigDecimal.ZERO)).max(BigDecimal.ZERO)).toList());
  Map<String,Object> out=new LinkedHashMap<>();
  out.put("asOf",today);out.put("loanCount",ls.size());out.put("activeLoans",ls.stream().filter(l->l.getStatus()==Loan.LoanStatus.DISBURSED).count());
  out.put("paidLoans",ls.stream().filter(l->l.getStatus()==Loan.LoanStatus.PAID).count());out.put("defaultedLoans",ls.stream().filter(l->l.getStatus()==Loan.LoanStatus.DEFAULTED).count());
  out.put("disbursedValue",disbursed);out.put("contractedReceivable",contracted);out.put("paymentsReceived",received);
  out.put("pendingPayments",pending);out.put("outstanding",contracted.subtract(received).max(BigDecimal.ZERO));out.put("overdueAmount",overdueAmount);
  out.put("readOnly",true);
  return out;
 }
 private BigDecimal sum(List<BigDecimal> xs){return xs.stream().filter(Objects::nonNull).reduce(BigDecimal.ZERO,BigDecimal::add);}
}