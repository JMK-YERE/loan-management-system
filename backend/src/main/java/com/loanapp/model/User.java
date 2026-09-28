package com.loanapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String fullName;

    @Column(unique = true, nullable = false, length = 150)
    private String email;

    @Column(unique = true, nullable = false, length = 20)
    private String phone;

    @Column(nullable = true)
    private String password;

    @Column(name = "national_id", unique = true, length = 50)
    private String nidaNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status = UserStatus.PENDING;

    @Column(unique = true, length = 100)
    private String verificationToken;

    private LocalDateTime tokenExpiry;

    @Column(length = 500)
    private String profilePicture;

    @Column(length = 500)
    private String rejectionReason;

    @Column(nullable = false)
    private Boolean active = false;

    @Column(length = 30) private String idType;
    private java.time.LocalDate dateOfBirth;
    @Column(length = 10) private String gender;
    @Column(length = 20) private String maritalStatus;
    @Column(length = 60) private String nationality;
    @Column(length = 200) private String address;
    @Column(length = 80) private String city;
    @Column(length = 80) private String country;
    @Column(length = 30) private String employmentStatus;
    @Column(length = 120) private String occupation;
    @Column(length = 150) private String employer;
    private java.math.BigDecimal monthlyIncome;
    @Column(length = 200) private String kinName;
    @Column(length = 20) private String kinPhone;
    @Column(length = 40) private String kinRelationship;
    @Column(columnDefinition = "TEXT") private String photoData;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

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

    public enum Role {
        LENDER, BORROWER, GUARANTOR, ADMIN
    }

    public enum UserStatus {
        PENDING, APPROVED, REJECTED
    }
}
