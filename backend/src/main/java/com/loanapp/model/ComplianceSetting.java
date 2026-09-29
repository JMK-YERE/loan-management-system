package com.loanapp.model;

import jakarta.persistence.*; import lombok.*; import java.time.LocalDateTime; import java.math.BigDecimal;

@Entity @Table(name="compliance_settings") @Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ComplianceSetting{
 @Id private Long id=1L;
 @Column(nullable=false,length=10) private String currency="TZS";
 private BigDecimal maxLoanAmount;
 private BigDecimal maxProcessingFeePercent;
 private BigDecimal maxAnnualInterestPercent;
 private Integer gracePeriodDays=0;
 @Column(columnDefinition="TEXT") private String termsAndConditions;
 @Column(columnDefinition="TEXT") private String privacyNotice;
 @Column(columnDefinition="TEXT") private String complaintsProcedure;
 private LocalDateTime updatedAt;
 @PrePersist @PreUpdate void touch(){updatedAt=LocalDateTime.now();}
}
