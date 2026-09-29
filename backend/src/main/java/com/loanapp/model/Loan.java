package com.loanapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id", nullable = false)
    private User borrower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lender_id", nullable = false)
    private User lender;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(nullable = false)
    private Integer durationMonths;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalRepayment;

    @Column(precision = 15, scale = 2)
    private BigDecimal processingFee = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    private BigDecimal lawyerFee = BigDecimal.ZERO;

    @Column(nullable = false)
    private Boolean lawyerRequired = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LoanStatus status = LoanStatus.PENDING;

    @Column(length = 500)
    private String purpose;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDate disbursementDate;

    private LocalDate nextDueDate;

    private LocalDateTime lastOverdueReminderAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum LoanStatus {
        PENDING, APPROVED, REJECTED, DISBURSED, PAID, DEFAULTED
    }
}
