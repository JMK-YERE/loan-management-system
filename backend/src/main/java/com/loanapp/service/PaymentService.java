package com.loanapp.service;

import com.loanapp.dto.PaymentRequest;
import com.loanapp.model.Loan;
import com.loanapp.model.Payment;
import com.loanapp.repository.LoanRepository;
import com.loanapp.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Transactional
    public Payment createPayment(PaymentRequest request) {
        Loan loan = loanRepository.findById(request.getLoanId())
                .orElseThrow(() -> new RuntimeException("Mkopo haujapatikana"));

        if (loan.getStatus() == Loan.LoanStatus.REJECTED || loan.getStatus() == Loan.LoanStatus.PAID) {
            throw new RuntimeException("Mkopo huu haupokei malipo kwa sasa");
        }

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Kiasi cha malipo lazima kiwe zaidi ya sifuri");
        }

        Payment.PaymentMethod method;
        try {
            method = Payment.PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Njia ya malipo si sahihi: " + request.getPaymentMethod());
        }

        BigDecimal successfulPayments = paymentRepository.findByLoanAndStatus(
                loan, Payment.PaymentStatus.SUCCESS
        ).stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal remaining = loan.getTotalRepayment().subtract(successfulPayments);
        if (request.getAmount().compareTo(remaining) > 0) {
            throw new RuntimeException("Malipo yanazidi salio la mkopo: " + remaining);
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

    @Transactional
    public Payment confirmPayment(Long paymentId, String transactionId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Malipo hayajapatikana"));

        if (payment.getStatus() == Payment.PaymentStatus.SUCCESS) {
            return payment;
        }

        payment.setStatus(Payment.PaymentStatus.SUCCESS);
        if (transactionId != null && !transactionId.isBlank()) {
            payment.setTransactionId(transactionId);
        }
        payment.setPaidAt(LocalDateTime.now());

        Payment saved = paymentRepository.save(payment);
        Loan loan = saved.getLoan();

        BigDecimal successfulPayments = paymentRepository.findByLoanAndStatus(
                loan, Payment.PaymentStatus.SUCCESS
        ).stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (successfulPayments.compareTo(loan.getTotalRepayment()) >= 0) {
            loan.setStatus(Loan.LoanStatus.PAID);
            loanRepository.save(loan);
        } else if (loan.getStatus() == Loan.LoanStatus.APPROVED) {
            loan.setStatus(Loan.LoanStatus.DISBURSED);
            loanRepository.save(loan);
        }

        return saved;
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
