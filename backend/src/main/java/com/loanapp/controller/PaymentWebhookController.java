package com.loanapp.controller;

import com.loanapp.model.Payment; import com.loanapp.repository.PaymentRepository; import com.loanapp.repository.LoanRepository; import com.loanapp.repository.WebhookEventRepository; import com.loanapp.model.WebhookEvent;
import org.springframework.beans.factory.annotation.Value; import org.springframework.http.ResponseEntity; import org.springframework.web.bind.annotation.*;
import javax.crypto.Mac; import javax.crypto.spec.SecretKeySpec; import java.security.MessageDigest; import java.nio.charset.StandardCharsets; import java.time.LocalDateTime; import java.util.Map; import org.springframework.transaction.annotation.Transactional;

@RestController @RequestMapping("/api/webhooks/payments")
public class PaymentWebhookController{
 private final PaymentRepository payments; private final LoanRepository loans; private final WebhookEventRepository events;
 @Value("${app.payments.webhook-secret:}") private String secret;
 public PaymentWebhookController(PaymentRepository payments,LoanRepository loans,WebhookEventRepository events){this.payments=payments;this.loans=loans;this.events=events;}
 @PostMapping @Transactional public ResponseEntity<?> webhook(@RequestHeader(value="X-Signature",required=false) String signature,@RequestBody Map<String,Object> body){
  if(secret.isBlank()) return ResponseEntity.status(503).body(Map.of("status","NOT_CONFIGURED"));
  String tx=String.valueOf(body.getOrDefault("transactionId",""));String status=String.valueOf(body.getOrDefault("status","")).toUpperCase();
  String expected=hmac(tx+"|"+status);
  if(signature==null||!MessageDigest.isEqual(expected.toLowerCase().getBytes(StandardCharsets.UTF_8),signature.toLowerCase().getBytes(StandardCharsets.UTF_8))) return ResponseEntity.status(401).body(Map.of("status","INVALID_SIGNATURE"));
  Payment p=payments.findByTransactionId(tx).orElseThrow(()->new RuntimeException("Transaction not found"));
  String eventKey="PAYMENT:"+tx;
  if(events.existsByEventKey(eventKey)) return ResponseEntity.ok(Map.of("status","OK","idempotent",true));
  if("SUCCESS".equals(status)){ p.setStatus(Payment.PaymentStatus.SUCCESS);p.setPaidAt(LocalDateTime.now());payments.save(p);var loan=p.getLoan();var paid=payments.findByLoanAndStatus(loan,Payment.PaymentStatus.SUCCESS).stream().map(Payment::getAmount).reduce(java.math.BigDecimal.ZERO,java.math.BigDecimal::add);if(paid.compareTo(loan.getTotalRepayment())>=0)loan.setStatus(com.loanapp.model.Loan.LoanStatus.PAID);else if(loan.getStatus()==com.loanapp.model.Loan.LoanStatus.APPROVED||loan.getStatus()==com.loanapp.model.Loan.LoanStatus.PENDING)loan.setStatus(com.loanapp.model.Loan.LoanStatus.DISBURSED);loans.save(loan);}
  else if("FAILED".equals(status)) {p.setStatus(Payment.PaymentStatus.FAILED);payments.save(p);}
  else if("REVERSED".equals(status)) {p.setStatus(Payment.PaymentStatus.REVERSED);payments.save(p);}
  else return ResponseEntity.badRequest().body(Map.of("status","UNSUPPORTED_STATUS"));
  events.save(new WebhookEvent(eventKey,status));
  return ResponseEntity.ok(Map.of("status","OK","idempotent",false));
 }
 private String hmac(String value){try{Mac m=Mac.getInstance("HmacSHA256");m.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),"HmacSHA256"));byte[] d=m.doFinal(value.getBytes(StandardCharsets.UTF_8));StringBuilder b=new StringBuilder();for(byte x:d)b.append(String.format("%02x",x));return b.toString();}catch(Exception e){throw new RuntimeException(e);}}
}
