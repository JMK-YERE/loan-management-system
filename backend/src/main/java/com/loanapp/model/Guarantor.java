package com.loanapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "guarantors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Guarantor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guarantor_id", nullable = false)
    private User guarantor;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal guaranteedAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GuarantorStatus status = GuarantorStatus.PENDING;

    @Column(length = 500)
    private String relationship;

    private LocalDateTime approvedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum GuarantorStatus {
        PENDING, APPROVED, REJECTED, RELEASED
    }
}
