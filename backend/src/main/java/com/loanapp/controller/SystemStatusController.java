package com.loanapp.controller;

import com.loanapp.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/system")
@PreAuthorize("hasRole('ADMIN')")
public class SystemStatusController {
 @Value("${spring.mail.username:}") private String mail;
 @Value("${app.twilio.account-sid:}") private String twilio;
 @Value("${app.twilio.auth-token:}") private String twilioToken;
 @Value("${app.twilio.from:}") private String twilioFrom;
 @Value("${app.payments.provider:DISABLED}") private String paymentProvider;
 @Value("${app.payments.base-url:}") private String paymentUrl;
 @Value("${app.payments.api-key:}") private String paymentKey;
 @Value("${app.payments.api-secret:}") private String paymentSecret;
 @Value("${app.nida.verification-url:}") private String nidaUrl;
 @Value("${app.nida.api-key:}") private String nidaKey;
 @GetMapping("/status")
 public ResponseEntity<ApiResponse<Map<String,Object>>> status(){
  Map<String,Object> m=new LinkedHashMap<>();
  m.put("emailConfigured",!mail.isBlank());
  m.put("smsConfigured",!twilio.isBlank()&&!twilioToken.isBlank()&&!twilioFrom.isBlank());
  m.put("mobileMoneyConfigured",!"DISABLED".equalsIgnoreCase(paymentProvider)&&!paymentUrl.isBlank()&&!paymentKey.isBlank()&&!paymentSecret.isBlank());
  m.put("mobileMoneyProvider",paymentProvider);
  m.put("nidaConfigured",!nidaUrl.isBlank()&&!nidaKey.isBlank());
  m.put("overdueRemindersEnabled",true);
  m.put("pdfAgreementsEnabled",true);
  m.put("eSignatureEnabled",true);
  m.put("auditTrailEnabled",true);
  m.put("reportsExportEnabled",true);
  m.put("rateLimitingEnabled",true);
  m.put("webhookIdempotencyEnabled",true);
  m.put("roleAccountManagementEnabled",true);
  return ResponseEntity.ok(ApiResponse.success("System feature status",m));
 }
}
