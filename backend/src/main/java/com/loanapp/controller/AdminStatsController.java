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
        long totalUsers = userRepository.count();
        long pendingUsers = userRepository.countByStatus(com.loanapp.model.User.UserStatus.PENDING);
        long activeUsers = userRepository.countByActiveTrue();
        long totalLoans = loanRepository.count();
        long pendingLoans = loanRepository.countByStatus(Loan.LoanStatus.PENDING);
        long approvedLoans = loanRepository.countByStatusIn(java.util.List.of(Loan.LoanStatus.APPROVED, Loan.LoanStatus.DISBURSED));
        long paidLoans = loanRepository.countByStatus(Loan.LoanStatus.PAID);
        long defaultedLoans = loanRepository.countByStatus(Loan.LoanStatus.DEFAULTED);
        long totalPayments = paymentRepository.count();
        BigDecimal portfolio = loanRepository.sumAmount();
        BigDecimal repayments = paymentRepository.sumSuccessfulAmount();

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalUsers", totalUsers);
        data.put("activeUsers", activeUsers);
        data.put("pendingUsers", pendingUsers);
        data.put("totalLoans", totalLoans);
        data.put("pendingLoans", pendingLoans);
        data.put("approvedLoans", approvedLoans);
        data.put("paidLoans", paidLoans);
        data.put("defaultedLoans", defaultedLoans);
        data.put("portfolioAmount", portfolio);
        data.put("successfulRepayments", repayments);
        data.put("totalPayments", totalPayments);
        return ResponseEntity.ok(ApiResponse.success("Takwimu za mfumo", data));
    }
}
