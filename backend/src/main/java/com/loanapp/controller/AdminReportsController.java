package com.loanapp.controller;

import com.loanapp.model.Loan; import com.loanapp.model.Payment;
import com.loanapp.repository.LoanRepository; import com.loanapp.repository.PaymentRepository;
import org.springframework.http.*; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*;
import java.nio.charset.StandardCharsets;

@RestController @RequestMapping("/api/admin/reports")
@PreAuthorize("hasRole('ADMIN')")
public class AdminReportsController{
 private final LoanRepository loans; private final PaymentRepository payments;
 public AdminReportsController(LoanRepository loans,PaymentRepository payments){this.loans=loans;this.payments=payments;}
 @GetMapping("/loans.csv") public ResponseEntity<byte[]> loansCsv(){
  StringBuilder s=new StringBuilder("id,borrower,lender,amount,interestRate,durationMonths,totalRepayment,status,createdAt,nextDueDate\n");
  for(Loan l:loans.findAll()) s.append(l.getId()).append(',').append(q(l.getBorrower().getEmail())).append(',').append(q(l.getLender().getEmail())).append(',').append(l.getAmount()).append(',').append(l.getInterestRate()).append(',').append(l.getDurationMonths()).append(',').append(l.getTotalRepayment()).append(',').append(l.getStatus()).append(',').append(l.getCreatedAt()).append(',').append(l.getNextDueDate()).append('\n');
  return csv("loans.csv",s.toString());
 }
 @GetMapping("/payments.csv") public ResponseEntity<byte[]> paymentsCsv(){
  StringBuilder s=new StringBuilder("id,loanId,borrower,amount,method,transactionId,status,createdAt,paidAt\n");
  for(Payment p:payments.findAll()) s.append(p.getId()).append(',').append(p.getLoan().getId()).append(',').append(q(p.getLoan().getBorrower().getEmail())).append(',').append(p.getAmount()).append(',').append(p.getPaymentMethod()).append(',').append(q(p.getTransactionId())).append(',').append(p.getStatus()).append(',').append(p.getCreatedAt()).append(',').append(p.getPaidAt()).append('\n');
  return csv("payments.csv",s.toString());
 }
 private String q(Object v){return "\"" + String.valueOf(v==null?"":v).replace("\"","\"\"") + "\"";}
 private ResponseEntity<byte[]> csv(String name,String body){return ResponseEntity.ok().contentType(MediaType.parseMediaType("text/csv")).header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+name).body(body.getBytes(StandardCharsets.UTF_8));}
}
