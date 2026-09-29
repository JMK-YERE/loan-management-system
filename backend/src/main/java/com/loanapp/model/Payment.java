package com.loanapp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "loan_id", nullable = false) private Loan loan;
    @Column(nullable = false, precision = 15, scale = 2) private BigDecimal amount;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30) private PaymentMethod paymentMethod;
    @Column(length = 100) private String transactionId;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private PaymentStatus status = PaymentStatus.PENDING;
    @Column(length = 500) private String notes;
    private LocalDateTime paidAt;
    @Column(length = 40) private String provider;
    @Column(length = 120) private String gatewayReference;
    @Column(length = 500) private String checkoutUrl;
    @Column(nullable = false, updatable = false) private LocalDateTime createdAt;

    public Payment() {}
    public Payment(Long id, Loan loan, BigDecimal amount, PaymentMethod paymentMethod, String transactionId,
                   PaymentStatus status, String notes, LocalDateTime paidAt, String provider,
                   String gatewayReference, String checkoutUrl, LocalDateTime createdAt) {
        this.id=id; this.loan=loan; this.amount=amount; this.paymentMethod=paymentMethod; this.transactionId=transactionId;
        this.status=status; this.notes=notes; this.paidAt=paidAt; this.provider=provider;
        this.gatewayReference=gatewayReference; this.checkoutUrl=checkoutUrl; this.createdAt=createdAt;
    }
    public static Builder builder(){return new Builder();}
    public static class Builder {
        private Long id; private Loan loan; private BigDecimal amount; private PaymentMethod paymentMethod;
        private String transactionId; private PaymentStatus status=PaymentStatus.PENDING; private String notes;
        private LocalDateTime paidAt; private String provider; private String gatewayReference; private String checkoutUrl; private LocalDateTime createdAt;
        public Builder id(Long v){id=v;return this;} public Builder loan(Loan v){loan=v;return this;} public Builder amount(BigDecimal v){amount=v;return this;}
        public Builder paymentMethod(PaymentMethod v){paymentMethod=v;return this;} public Builder transactionId(String v){transactionId=v;return this;}
        public Builder status(PaymentStatus v){status=v;return this;} public Builder notes(String v){notes=v;return this;}
        public Builder paidAt(LocalDateTime v){paidAt=v;return this;} public Builder provider(String v){provider=v;return this;}
        public Builder gatewayReference(String v){gatewayReference=v;return this;} public Builder checkoutUrl(String v){checkoutUrl=v;return this;}
        public Builder createdAt(LocalDateTime v){createdAt=v;return this;}
        public Payment build(){return new Payment(id,loan,amount,paymentMethod,transactionId,status,notes,paidAt,provider,gatewayReference,checkoutUrl,createdAt);}
    }
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public Loan getLoan(){return loan;} public void setLoan(Loan v){loan=v;}
    public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal v){amount=v;}
    public PaymentMethod getPaymentMethod(){return paymentMethod;} public void setPaymentMethod(PaymentMethod v){paymentMethod=v;}
    public String getTransactionId(){return transactionId;} public void setTransactionId(String v){transactionId=v;}
    public PaymentStatus getStatus(){return status;} public void setStatus(PaymentStatus v){status=v;}
    public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
    public LocalDateTime getPaidAt(){return paidAt;} public void setPaidAt(LocalDateTime v){paidAt=v;}
    public String getProvider(){return provider;} public void setProvider(String v){provider=v;}
    public String getGatewayReference(){return gatewayReference;} public void setGatewayReference(String v){gatewayReference=v;}
    public String getCheckoutUrl(){return checkoutUrl;} public void setCheckoutUrl(String v){checkoutUrl=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
    @PrePersist protected void onCreate(){if(createdAt==null) createdAt=LocalDateTime.now();}
    public enum PaymentMethod { MPESA, TIGO_PESA, AIRTEL_MONEY, HALOPESA, CASH, BANK_TRANSFER }
    public enum PaymentStatus { PENDING, SUCCESS, FAILED, REVERSED }
}
