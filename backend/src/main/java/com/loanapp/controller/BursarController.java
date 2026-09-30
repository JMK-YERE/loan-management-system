package com.loanapp.controller;

import com.loanapp.model.Loan;
import com.loanapp.model.Payment;
import com.loanapp.repository.LoanRepository;
import com.loanapp.repository.PaymentRepository;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/api/bursar")
@PreAuthorize("hasAnyRole('BURSER','ADMIN')")
public class BursarController {
    private final LoanRepository loans;
    private final PaymentRepository payments;

    public BursarController(LoanRepository loans, PaymentRepository payments) {
        this.loans = loans;
        this.payments = payments;
    }

    @GetMapping("/summary")
    public Map<String,Object> summary() {
        List<Loan> ls = loans.findAll();
        List<Payment> ps = payments.findAll();
        BigDecimal disbursed = sum(ls.stream()
                .filter(l -> l.getStatus() == Loan.LoanStatus.DISBURSED
                        || l.getStatus() == Loan.LoanStatus.PAID
                        || l.getStatus() == Loan.LoanStatus.DEFAULTED)
                .map(Loan::getAmount).toList());
        BigDecimal receivable = sum(ls.stream()
                .filter(l -> l.getStatus() == Loan.LoanStatus.DISBURSED
                        || l.getStatus() == Loan.LoanStatus.DEFAULTED)
                .map(Loan::getTotalRepayment).toList());
        BigDecimal received = sum(ps.stream()
                .filter(p -> p.getStatus() == Payment.PaymentStatus.SUCCESS)
                .map(Payment::getAmount).toList());
        BigDecimal pending = sum(ps.stream()
                .filter(p -> p.getStatus() == Payment.PaymentStatus.PENDING)
                .map(Payment::getAmount).toList());

        return Map.of(
                "loanCount", ls.size(),
                "pendingLoans", ls.stream().filter(l -> l.getStatus() == Loan.LoanStatus.PENDING).count(),
                "activeLoans", ls.stream().filter(l -> l.getStatus() == Loan.LoanStatus.DISBURSED).count(),
                "paidLoans", ls.stream().filter(l -> l.getStatus() == Loan.LoanStatus.PAID).count(),
                "defaultedLoans", ls.stream().filter(l -> l.getStatus() == Loan.LoanStatus.DEFAULTED).count(),
                "disbursedValue", disbursed,
                "portfolioReceivable", receivable,
                "paymentsReceived", received,
                "pendingPayments", pending,
                "outstanding", receivable.subtract(received).max(BigDecimal.ZERO)
        );
    }

    @GetMapping("/loans")
    public List<Map<String,Object>> loanList() {
        return loans.findAll().stream().map(l -> {
            Map<String,Object> m = new LinkedHashMap<>();
            m.put("id", l.getId());
            m.put("borrower", l.getBorrower() == null ? null : l.getBorrower().getFullName());
            m.put("borrowerEmail", l.getBorrower() == null ? null : l.getBorrower().getEmail());
            m.put("lender", l.getLender() == null ? null : l.getLender().getFullName());
            m.put("amount", l.getAmount());
            m.put("totalRepayment", l.getTotalRepayment());
            m.put("status", l.getStatus());
            m.put("interestRate", l.getInterestRate());
            m.put("durationMonths", l.getDurationMonths());
            m.put("nextDueDate", l.getNextDueDate());
            return m;
        }).toList();
    }

    @GetMapping("/payments")
    public List<Map<String,Object>> paymentList() {
        return payments.findAll().stream().map(p -> {
            Map<String,Object> m = new LinkedHashMap<>();
            m.put("id", p.getId());
            m.put("loanId", p.getLoan().getId());
            m.put("borrower", p.getLoan().getBorrower().getFullName());
            m.put("amount", p.getAmount());
            m.put("method", p.getPaymentMethod());
            m.put("transactionId", p.getTransactionId());
            m.put("status", p.getStatus());
            m.put("createdAt", p.getCreatedAt());
            m.put("paidAt", p.getPaidAt());
            return m;
        }).toList();
    }

    @GetMapping("/report.csv")
    public ResponseEntity<byte[]> reportCsv() {
        StringBuilder s = new StringBuilder(
                "loanId,borrower,lender,amount,totalRepayment,status,interestRate,durationMonths,nextDueDate\n");
        for (Loan l : loans.findAll()) {
            s.append(l.getId()).append(',')
             .append(q(l.getBorrower() == null ? "" : l.getBorrower().getEmail())).append(',')
             .append(q(l.getLender() == null ? "" : l.getLender().getEmail())).append(',')
             .append(l.getAmount()).append(',')
             .append(l.getTotalRepayment()).append(',')
             .append(l.getStatus()).append(',')
             .append(l.getInterestRate()).append(',')
             .append(l.getDurationMonths()).append(',')
             .append(l.getNextDueDate()).append('\n');
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=bursar-loan-report.csv")
                .body(s.toString().getBytes(StandardCharsets.UTF_8));
    }

    private String q(Object v) {
        return """ + String.valueOf(v == null ? "" : v).replace(""", """") + """;
    }

    private BigDecimal sum(List<BigDecimal> xs) {
        return xs.stream().filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}