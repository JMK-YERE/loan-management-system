package com.loanapp.service;

import com.loanapp.dto.PaymentRequest;
import com.loanapp.model.Loan;
import com.loanapp.model.Payment;
import com.loanapp.repository.LoanRepository;
import com.loanapp.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private LoanRepository loanRepository;

    public Payment createPayment(PaymentRequest request) {
        Loan loan = loanRepository.findById(request.getLoanId())
                .orElseThrow(() -> new RuntimeException("Mkopo haujapatikana"));

        Payment.PaymentMethod method;
        try {
            method = Payment.PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Njia ya malipo si sahihi: " + request.getPaymentMethod());
        }

        Payment payment = Payment.builder()
                .loan(loan)
                .amount(request.getAmount())
                .paymentMethod(method)
                .transactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .status(Payment.PaymentStatus.PENDING)
                .build();

        return paymentRepository.save(payment);
    }

    public Payment confirmPayment(Long paymentId, String transactionId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Malipo hayajapatikana"));

        payment.setStatus(Payment.PaymentStatus.SUCCESS);
        payment.setTransactionId(transactionId);
        payment.setPaidAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    public List<Payment> getPaymentsByLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Mkopo haujapatikana"));
        return paymentRepository.findByLoan(loan);
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Malipo hayajapatikana"));
    }
}
