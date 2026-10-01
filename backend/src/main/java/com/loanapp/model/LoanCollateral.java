package com.loanapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_collaterals")
public class LoanCollateral {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loan_id", nullable = false)
    @JsonIgnore
    private Loan loan;

    @Column(nullable = false, length = 80)
    private String type;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(precision = 15, scale = 2)
    private BigDecimal value = BigDecimal.ZERO;

    @Column(length = 30)
    private String verificationStatus = "PENDING";

    @Column(length = 1000)
    private String documentReference;

    @JsonIgnore
    @Column(columnDefinition = "TEXT")
    private String documentDataJson;

    @JsonIgnore
    @Column(columnDefinition = "TEXT")
    private String photoDataJson;

    @Column(length = 150)
    private String capturedBy;

    private LocalDateTime capturedAt;
    private LocalDateTime verifiedAt;

    public LoanCollateral() {}

    public Long getId(){return id;}
    public void setId(Long v){id=v;}
    public Loan getLoan(){return loan;}
    public void setLoan(Loan v){loan=v;}
    public String getType(){return type;}
    public void setType(String v){type=v;}
    public String getDescription(){return description;}
    public void setDescription(String v){description=v;}
    public BigDecimal getValue(){return value;}
    public void setValue(BigDecimal v){value=v;}
    public String getVerificationStatus(){return verificationStatus;}
    public void setVerificationStatus(String v){verificationStatus=v;}
    public String getDocumentReference(){return documentReference;}
    public void setDocumentReference(String v){documentReference=v;}
    public String getDocumentDataJson(){return documentDataJson;}
    public void setDocumentDataJson(String v){documentDataJson=v;}
    public String getPhotoDataJson(){return photoDataJson;}
    public void setPhotoDataJson(String v){photoDataJson=v;}
    public String getCapturedBy(){return capturedBy;}
    public void setCapturedBy(String v){capturedBy=v;}
    public LocalDateTime getCapturedAt(){return capturedAt;}
    public void setCapturedAt(LocalDateTime v){capturedAt=v;}
    public LocalDateTime getVerifiedAt(){return verifiedAt;}
    public void setVerifiedAt(LocalDateTime v){verifiedAt=v;}

    @PrePersist protected void onCreate(){if(capturedAt==null)capturedAt=LocalDateTime.now();}
}