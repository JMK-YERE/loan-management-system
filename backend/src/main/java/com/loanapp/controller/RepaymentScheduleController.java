package com.loanapp.controller;
import com.loanapp.model.RepaymentSchedule;
import com.loanapp.service.LoanService;
import com.loanapp.service.RepaymentScheduleService;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/loans")
public class RepaymentScheduleController{
 private final LoanService loans; private final RepaymentScheduleService schedules;
 public RepaymentScheduleController(LoanService loans,RepaymentScheduleService schedules){this.loans=loans;this.schedules=schedules;}
 @GetMapping("/{id}/schedule") @PreAuthorize("isAuthenticated()")
 public List<RepaymentSchedule> schedule(@PathVariable Long id,Authentication auth){
  var loan=loans.getLoanById(id);
  if(!loans.canView(loan,auth.getName())) throw new RuntimeException("Huna ruhusa kuona ratiba ya mkopo huu");
  return schedules.byLoan(loan);
 }
}