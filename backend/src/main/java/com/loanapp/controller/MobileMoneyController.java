package com.loanapp.controller;

import com.loanapp.dto.ApiResponse; import com.loanapp.model.Payment; import com.loanapp.repository.PaymentRepository; import com.loanapp.repository.UserRepository; import com.loanapp.service.MobileMoneyGatewayService;
import org.springframework.http.ResponseEntity; import org.springframework.security.core.Authentication; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.util.Map;

@RestController @RequestMapping("/api/mobile-money")
public class MobileMoneyController{
 private final PaymentRepository payments; private final UserRepository users; private final MobileMoneyGatewayService gateway;
 public MobileMoneyController(PaymentRepository payments,UserRepository users,MobileMoneyGatewayService gateway){this.payments=payments;this.users=users;this.gateway=gateway;}
 @PostMapping("/checkout/{paymentId}") @PreAuthorize("hasRole('BORROWER')") public ResponseEntity<ApiResponse<Map<String,Object>>> checkout(@PathVariable Long paymentId,@RequestBody Map<String,String> body,Authentication auth){
  Payment p=payments.findById(paymentId).orElseThrow(()->new RuntimeException("Malipo hayajapatikana"));
  if(!p.getLoan().getBorrower().getEmail().equalsIgnoreCase(auth.getName())) throw new RuntimeException("Huna ruhusa");
  Map<String,Object> result=gateway.initiate(p,body.getOrDefault("phone",p.getLoan().getBorrower().getPhone()));
  return ResponseEntity.ok(ApiResponse.success("Mobile money checkout",result));
 }
}
