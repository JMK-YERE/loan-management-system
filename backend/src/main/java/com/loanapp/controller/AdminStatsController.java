package com.loanapp.controller;

import com.loanapp.dto.ApiResponse;
import com.loanapp.model.Loan;
import com.loanapp.model.Payment;
import com.loanapp.repository.LoanRepository;
import com.loanapp.repository.PaymentRepository;
import com.loanapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminStatsController {

    @Autowired private UserRepository userRepository;
    @Autowired private LoanRepository loanRepository;
    @Autowired private PaymentRepository paymentRepository;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> stats() {
        var users = userRepository.findAll();
        var loans = loanRepository.findAll();
        var payments = paymentRepository.findAll();

        BigDecimal portfolio = loans.stream()
                .map(Loan::getAmount)
                .filter(java.util.Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal repayments = payments.stream()
                .filter(p -> p.getStatus() == Payment.PaymentStatus.SUCCESS)
                .map(Payment::getAmount)
                .filter(java.util.Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long pendingUsers = users.stream().filter(u -> u.getStatus() == com.loanapp.model.User.UserStatus.PENDING).count();
        long activeUsers = users.stream().filter(u -> Boolean.TRUE.equals(u.getActive())).count();
        long pendingLoans = loans.stream().filter(l -> l.getStatus() == Loan.LoanStatus.PENDING).count();
        long approvedLoans = loans.stream().filter(l -> l.getStatus() == Loan.LoanStatus.APPROVED || l.getStatus() == Loan.LoanStatus.DISBURSED).count();
        long paidLoans = loans.stream().filter(l -> l.getStatus() == Loan.LoanStatus.PAID).count();
        long defaultedLoans = loans.stream().filter(l -> l.getStatus() == Loan.LoanStatus.DEFAULTED).count();

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalUsers", users.size());
        data.put("activeUsers", activeUsers);
        data.put("pendingUsers", pendingUsers);
        data.put("totalLoans", loans.size());
        data.put("pendingLoans", pendingLoans);
        data.put("approvedLoans", approvedLoans);
        data.put("paidLoans", paidLoans);
        data.put("defaultedLoans", defaultedLoans);
        data.put("portfolioAmount", portfolio);
        data.put("successfulRepayments", repayments);
        data.put("totalPayments", payments.size());

        return ResponseEntity.ok(ApiResponse.success("Takwimu za mfumo", data));
    }
}
