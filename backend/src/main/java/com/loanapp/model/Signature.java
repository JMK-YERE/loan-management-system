package com.loanapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "signatures")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Signature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String signatureData;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SignatureType signatureType;

    @Column(length = 100)
    private String ipAddress;

    @Column(length = 500)
    private String deviceInfo;

    @Column(nullable = false)
    private Boolean isValid = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime signedAt;

    @PrePersist
    protected void onCreate() {
        signedAt = LocalDateTime.now();
    }

    public enum SignatureType {
        BORROWER, LENDER, GUARANTOR, WITNESS, LAWYER
    }
}
