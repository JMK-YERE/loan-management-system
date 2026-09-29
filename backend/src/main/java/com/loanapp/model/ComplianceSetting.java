package com.loanapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity @Table(name="compliance_settings")
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
 public ComplianceSetting(){}
 public ComplianceSetting(Long id,String currency,BigDecimal maxLoanAmount,BigDecimal maxProcessingFeePercent,BigDecimal maxAnnualInterestPercent,Integer gracePeriodDays,String termsAndConditions,String privacyNotice,String complaintsProcedure,LocalDateTime updatedAt){this.id=id;this.currency=currency;this.maxLoanAmount=maxLoanAmount;this.maxProcessingFeePercent=maxProcessingFeePercent;this.maxAnnualInterestPercent=maxAnnualInterestPercent;this.gracePeriodDays=gracePeriodDays;this.termsAndConditions=termsAndConditions;this.privacyNotice=privacyNotice;this.complaintsProcedure=complaintsProcedure;this.updatedAt=updatedAt;}
 public static Builder builder(){return new Builder();} public static class Builder{private final ComplianceSetting x=new ComplianceSetting(); public Builder id(Long v){x.id=v;return this;} public Builder currency(String v){x.currency=v;return this;} public Builder maxLoanAmount(BigDecimal v){x.maxLoanAmount=v;return this;} public Builder maxProcessingFeePercent(BigDecimal v){x.maxProcessingFeePercent=v;return this;} public Builder maxAnnualInterestPercent(BigDecimal v){x.maxAnnualInterestPercent=v;return this;} public Builder gracePeriodDays(Integer v){x.gracePeriodDays=v;return this;} public Builder termsAndConditions(String v){x.termsAndConditions=v;return this;} public Builder privacyNotice(String v){x.privacyNotice=v;return this;} public Builder complaintsProcedure(String v){x.complaintsProcedure=v;return this;} public Builder updatedAt(LocalDateTime v){x.updatedAt=v;return this;} public ComplianceSetting build(){return x;}}
 public Long getId(){return id;} public void setId(Long v){id=v;} public String getCurrency(){return currency;} public void setCurrency(String v){currency=v;} public BigDecimal getMaxLoanAmount(){return maxLoanAmount;} public void setMaxLoanAmount(BigDecimal v){maxLoanAmount=v;} public BigDecimal getMaxProcessingFeePercent(){return maxProcessingFeePercent;} public void setMaxProcessingFeePercent(BigDecimal v){maxProcessingFeePercent=v;} public BigDecimal getMaxAnnualInterestPercent(){return maxAnnualInterestPercent;} public void setMaxAnnualInterestPercent(BigDecimal v){maxAnnualInterestPercent=v;} public Integer getGracePeriodDays(){return gracePeriodDays;} public void setGracePeriodDays(Integer v){gracePeriodDays=v;} public String getTermsAndConditions(){return termsAndConditions;} public void setTermsAndConditions(String v){termsAndConditions=v;} public String getPrivacyNotice(){return privacyNotice;} public void setPrivacyNotice(String v){privacyNotice=v;} public String getComplaintsProcedure(){return complaintsProcedure;} public void setComplaintsProcedure(String v){complaintsProcedure=v;} public LocalDateTime getUpdatedAt(){return updatedAt;} public void setUpdatedAt(LocalDateTime v){updatedAt=v;}
 @PrePersist @PreUpdate void touch(){updatedAt=LocalDateTime.now();}
}