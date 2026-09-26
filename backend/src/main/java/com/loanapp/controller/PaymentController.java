package com.loanapp.controller;

import com.loanapp.dto.ApiResponse;
import com.loanapp.dto.PaymentRequest;
import com.loanapp.model.Payment;
import com.loanapp.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ApiResponse<Payment>> createPayment(
            @Valid @RequestBody PaymentRequest request) {
        Payment payment = paymentService.createPayment(request);
        return ResponseEntity.ok(ApiResponse.success("Malipo yameanzishwa", payment));
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse<Payment>> confirmPayment(
            @PathVariable Long id,
            @RequestParam String transactionId) {
        Payment payment = paymentService.confirmPayment(id, transactionId);
        return ResponseEntity.ok(ApiResponse.success("Malipo yamethibitishwa", payment));
    }

    @GetMapping("/loan/{loanId}")
    public ResponseEntity<ApiResponse<List<Payment>>> getPaymentsByLoan(@PathVariable Long loanId) {
        List<Payment> payments = paymentService.getPaymentsByLoan(loanId);
        return ResponseEntity.ok(ApiResponse.success("Malipo yote", payments));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Payment>> getPaymentById(@PathVariable Long id) {
        Payment payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(ApiResponse.success("Malipo yamepatikana", payment));
    }
}
