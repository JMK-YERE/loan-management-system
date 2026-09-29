package com.loanapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "signatures")
public class Signature {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "loan_id", nullable = false) private Loan loan;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false) private User user;
    @Column(nullable = false, columnDefinition = "TEXT") private String signatureData;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30) private SignatureType signatureType;
    @Column(length = 100) private String ipAddress;
    @Column(length = 500) private String deviceInfo;
    @Column(nullable = false) private Boolean isValid = true;
    @Column(nullable = false, updatable = false) private LocalDateTime signedAt;

    public Signature() {}
    public Signature(Long id, Loan loan, User user, String signatureData, SignatureType signatureType,
                     String ipAddress, String deviceInfo, Boolean isValid, LocalDateTime signedAt) {
        this.id=id; this.loan=loan; this.user=user; this.signatureData=signatureData;
        this.signatureType=signatureType; this.ipAddress=ipAddress; this.deviceInfo=deviceInfo;
        this.isValid=isValid; this.signedAt=signedAt;
    }
    public static Builder builder(){return new Builder();}
    public static class Builder {
        private Long id; private Loan loan; private User user; private String signatureData;
        private SignatureType signatureType; private String ipAddress; private String deviceInfo;
        private Boolean isValid=true; private LocalDateTime signedAt;
        public Builder id(Long v){id=v;return this;} public Builder loan(Loan v){loan=v;return this;}
        public Builder user(User v){user=v;return this;} public Builder signatureData(String v){signatureData=v;return this;}
        public Builder signatureType(SignatureType v){signatureType=v;return this;} public Builder ipAddress(String v){ipAddress=v;return this;}
        public Builder deviceInfo(String v){deviceInfo=v;return this;} public Builder isValid(Boolean v){isValid=v;return this;}
        public Builder signedAt(LocalDateTime v){signedAt=v;return this;}
        public Signature build(){return new Signature(id,loan,user,signatureData,signatureType,ipAddress,deviceInfo,isValid,signedAt);}
    }
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public Loan getLoan(){return loan;} public void setLoan(Loan v){loan=v;}
    public User getUser(){return user;} public void setUser(User v){user=v;}
    public String getSignatureData(){return signatureData;} public void setSignatureData(String v){signatureData=v;}
    public SignatureType getSignatureType(){return signatureType;} public void setSignatureType(SignatureType v){signatureType=v;}
    public String getIpAddress(){return ipAddress;} public void setIpAddress(String v){ipAddress=v;}
    public String getDeviceInfo(){return deviceInfo;} public void setDeviceInfo(String v){deviceInfo=v;}
    public Boolean getIsValid(){return isValid;} public void setIsValid(Boolean v){isValid=v;}
    public LocalDateTime getSignedAt(){return signedAt;} public void setSignedAt(LocalDateTime v){signedAt=v;}
    @PrePersist protected void onCreate(){if(signedAt==null) signedAt=LocalDateTime.now();}
    public enum SignatureType { BORROWER, LENDER, GUARANTOR, WITNESS, LAWYER }
}
