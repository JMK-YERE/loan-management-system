package com.loanapp.controller;

import com.loanapp.model.Payment; import com.loanapp.repository.PaymentRepository; import com.loanapp.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Value; import org.springframework.http.ResponseEntity; import org.springframework.web.bind.annotation.*;
import javax.crypto.Mac; import javax.crypto.spec.SecretKeySpec; import java.security.MessageDigest; import java.nio.charset.StandardCharsets; import java.time.LocalDateTime; import java.util.Map;

@RestController @RequestMapping("/api/webhooks/payments")
public class PaymentWebhookController{
 private final PaymentRepository payments; private final LoanRepository loans;
 @Value("${app.payments.webhook-secret:}") private String secret;
 public PaymentWebhookController(PaymentRepository payments,LoanRepository loans){this.payments=payments;this.loans=loans;}
 @PostMapping public ResponseEntity<?> webhook(@RequestHeader(value="X-Signature",required=false) String signature,@RequestBody Map<String,Object> body){
  if(secret.isBlank()) return ResponseEntity.status(503).body(Map.of("status","NOT_CONFIGURED"));
  String tx=String.valueOf(body.getOrDefault("transactionId",""));String status=String.valueOf(body.getOrDefault("status","")).toUpperCase();
  String expected=hmac(tx+"|"+status);
  if(signature==null||!MessageDigest.isEqual(expected.toLowerCase().getBytes(StandardCharsets.UTF_8),signature.toLowerCase().getBytes(StandardCharsets.UTF_8))) return ResponseEntity.status(401).body(Map.of("status","INVALID_SIGNATURE"));
  Payment p=payments.findByTransactionId(tx).orElseThrow(()->new RuntimeException("Transaction not found"));
  if("SUCCESS".equals(status)){ if(p.getStatus()==Payment.PaymentStatus.SUCCESS) return ResponseEntity.ok(Map.of("status","OK","idempotent",true)); p.setStatus(Payment.PaymentStatus.SUCCESS);p.setPaidAt(LocalDateTime.now());payments.save(p);var loan=p.getLoan();var paid=payments.findByLoanAndStatus(loan,Payment.PaymentStatus.SUCCESS).stream().map(Payment::getAmount).reduce(java.math.BigDecimal.ZERO,java.math.BigDecimal::add);if(paid.compareTo(loan.getTotalRepayment())>=0)loan.setStatus(com.loanapp.model.Loan.LoanStatus.PAID);else loan.setStatus(com.loanapp.model.Loan.LoanStatus.DISBURSED);loans.save(loan);}
  else if("FAILED".equals(status)) {p.setStatus(Payment.PaymentStatus.FAILED);payments.save(p);}
  return ResponseEntity.ok(Map.of("status","OK"));
 }
 private String hmac(String value){try{Mac m=Mac.getInstance("HmacSHA256");m.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),"HmacSHA256"));byte[] d=m.doFinal(value.getBytes(StandardCharsets.UTF_8));StringBuilder b=new StringBuilder();for(byte x:d)b.append(String.format("%02x",x));return b.toString();}catch(Exception e){throw new RuntimeException(e);}}
}
