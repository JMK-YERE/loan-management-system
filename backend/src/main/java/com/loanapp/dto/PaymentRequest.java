package com.loanapp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PaymentRequest {
 @NotNull(message="Loan ID inahitajika") private Long loanId;
 @NotNull(message="Kiasi kinahitajika") @DecimalMin(value="1.00",message="Kiasi kiwe angalau TZS 1") private BigDecimal amount;
 @NotBlank(message="Njia ya malipo inahitajika") private String paymentMethod;
 public PaymentRequest(){} public PaymentRequest(Long loanId,BigDecimal amount,String paymentMethod){this.loanId=loanId;this.amount=amount;this.paymentMethod=paymentMethod;}
 public static Builder builder(){return new Builder();} public static class Builder{private final PaymentRequest x=new PaymentRequest(); public Builder loanId(Long v){x.loanId=v;return this;} public Builder amount(BigDecimal v){x.amount=v;return this;} public Builder paymentMethod(String v){x.paymentMethod=v;return this;} public PaymentRequest build(){return x;}}
 public Long getLoanId(){return loanId;} public void setLoanId(Long v){loanId=v;} public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal v){amount=v;} public String getPaymentMethod(){return paymentMethod;} public void setPaymentMethod(String v){paymentMethod=v;}
}