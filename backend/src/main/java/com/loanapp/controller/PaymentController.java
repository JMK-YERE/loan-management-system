package com.loanapp.controller;

import com.loanapp.dto.ApiResponse;
import com.loanapp.dto.PaymentRequest;
import com.loanapp.model.Payment;
import com.loanapp.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired private PaymentService paymentService;

    @PostMapping @PreAuthorize("hasRole('BORROWER')")
    public ResponseEntity<ApiResponse<Payment>> createPayment(@Valid @RequestBody PaymentRequest request,Authentication auth){return ResponseEntity.ok(ApiResponse.success("Malipo yameanzishwa",paymentService.createPayment(request,auth.getName())));}

    @PutMapping("/{id}/confirm") @PreAuthorize("hasAnyRole('LENDER','BURSER')")
    public ResponseEntity<ApiResponse<Payment>> confirmPayment(@PathVariable Long id,@RequestParam String transactionId,Authentication auth){return ResponseEntity.ok(ApiResponse.success("Malipo yamethibitishwa",paymentService.confirmPayment(id,transactionId,auth.getName())));}

    @GetMapping("/loan/{loanId}") @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<Payment>>> getPaymentsByLoan(@PathVariable Long loanId,Authentication auth){return ResponseEntity.ok(ApiResponse.success("Malipo yote",paymentService.getPaymentsByLoan(loanId,auth.getName())));}

    @GetMapping("/{id}") @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Payment>> getPaymentById(@PathVariable Long id,Authentication auth){return ResponseEntity.ok(ApiResponse.success("Malipo yamepatikana",paymentService.getPaymentById(id,auth.getName())));}
}
