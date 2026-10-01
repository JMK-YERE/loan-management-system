package com.loanapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "guarantors")
public class Guarantor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "loan_id", nullable = false) private Loan loan;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "guarantor_id", nullable = true) private User guarantor;

    @Column(length = 200) private String guarantorName;
    @Column(length = 50) private String guarantorPhone;
    @Column(length = 80) private String guarantorIdNumber;
    @JsonIgnore @Lob @Column(columnDefinition = "TEXT") private String guarantorPhotoData;
    @JsonIgnore @Lob @Column(columnDefinition = "TEXT") private String onsiteSignatureData;
    @Column(nullable = false, precision = 15, scale = 2) private BigDecimal guaranteedAmount;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private GuarantorStatus status = GuarantorStatus.PENDING;
    @Column(length = 500) private String relationship;
    @Column(length = 30) private String captureMode = "REGISTERED_ACCOUNT";
    @Column(length = 150) private String capturedBy;
    private LocalDateTime approvedAt;
    private LocalDateTime capturedAt;
    @Column(nullable = false, updatable = false) private LocalDateTime createdAt;

    public Guarantor() {}

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final Guarantor g = new Guarantor();
        public Builder id(Long v){g.id=v;return this;} public Builder loan(Loan v){g.loan=v;return this;}
        public Builder guarantor(User v){g.guarantor=v;return this;} public Builder guaranteedAmount(BigDecimal v){g.guaranteedAmount=v;return this;}
        public Builder status(GuarantorStatus v){g.status=v;return this;} public Builder relationship(String v){g.relationship=v;return this;}
        public Builder guarantorName(String v){g.guarantorName=v;return this;} public Builder guarantorPhone(String v){g.guarantorPhone=v;return this;}
        public Builder guarantorIdNumber(String v){g.guarantorIdNumber=v;return this;} public Builder.guarantorPhotoData(String v){g.guarantorPhotoData=v;return this;}
        public Builder.onsiteSignatureData(String v){g.onsiteSignatureData=v;return this;} public Builder.captureMode(String v){g.captureMode=v;return this;}
        public Builder.capturedBy(String v){g.capturedBy=v;return this;} public Builder.approvedAt(LocalDateTime v){g.approvedAt=v;return this;}
        public Builder.capturedAt(LocalDateTime v){g.capturedAt=v;return this;} public Builder.createdAt(LocalDateTime v){g.createdAt=v;return this;}
        public Guarantor build(){return g;}
    }

    public Long getId(){return id;} public void setId(Long v){id=v;}
    public Loan getLoan(){return loan;} public void setLoan(Loan v){loan=v;}
    public User getGuarantor(){return guarantor;} public void setGuarantor(User v){guarantor=v;}
    public String getGuarantorName(){return guarantorName;} public void setGuarantorName(String v){guarantorName=v;}
    public String getGuarantorPhone(){return guarantorPhone;} public void setGuarantorPhone(String v){guarantorPhone=v;}
    public String getGuarantorIdNumber(){return guarantorIdNumber;} public void setGuarantorIdNumber(String v){guarantorIdNumber=v;}
    public String getGuarantorPhotoData(){return guarantorPhotoData;} public void setGuarantorPhotoData(String v){guarantorPhotoData=v;}
    public String getOnsiteSignatureData(){return onsiteSignatureData;} public void setOnsiteSignatureData(String v){onsiteSignatureData=v;}
    public BigDecimal getGuaranteedAmount(){return guaranteedAmount;} public void setGuaranteedAmount(BigDecimal v){guaranteedAmount=v;}
    public GuarantorStatus getStatus(){return status;} public void setStatus(GuarantorStatus v){status=v;}
    public String getRelationship(){return relationship;} public void setRelationship(String v){relationship=v;}
    public String getCaptureMode(){return captureMode;} public void setCaptureMode(String v){captureMode=v;}
    public String getCapturedBy(){return capturedBy;} public void setCapturedBy(String v){capturedBy=v;}
    public LocalDateTime getApprovedAt(){return approvedAt;} public void setApprovedAt(LocalDateTime v){approvedAt=v;}
    public LocalDateTime getCapturedAt(){return capturedAt;} public void setCapturedAt(LocalDateTime v){capturedAt=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}

    @PrePersist protected void onCreate(){if(createdAt==null) createdAt=LocalDateTime.now();}
    public enum GuarantorStatus { PENDING, APPROVED, REJECTED, RELEASED }
}
