package com.loanapp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class LoanQuoteResponse {
    public Long productId;
    public String productName;
    public String loanType;
    public String durationUnit;
    public String interestType;
    public BigDecimal principal;
    public BigDecimal interest;
    public BigDecimal processingFee;
    public BigDecimal totalRepayment;
    public BigDecimal installmentAmount;
    public Integer installmentCount;
    public String repaymentFrequency;
    public BigDecimal lateFee;
    public Integer gracePeriodDays;
    public List<LocalDate> dueDates;
    public String termsVersion;

    public LoanQuoteResponse(){}
}
