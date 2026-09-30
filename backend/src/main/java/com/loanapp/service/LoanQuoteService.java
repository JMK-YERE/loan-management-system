package com.loanapp.service;

import com.loanapp.dto.LoanQuoteRequest;
import com.loanapp.dto.LoanQuoteResponse;
import com.loanapp.model.LoanProduct;
import com.loanapp.repository.LoanProductRepository;
import org.springframework.stereotype.Service;

import java.math.*;
import java.time.LocalDate;
import java.util.*;

@Service
public class LoanQuoteService {
    private final LoanProductRepository products;

    public LoanQuoteService(LoanProductRepository products){this.products=products;}

    public LoanQuoteResponse quote(LoanQuoteRequest r){
        LoanProduct p=products.findById(r.getProductId()).orElseThrow(()->new RuntimeException("Loan product haijapatikana"));
        if(!Boolean.TRUE.equals(p.getActive())) throw new RuntimeException("Loan product haifanyi kazi");
        if(r.getAmount().compareTo(p.getMinAmount())<0||r.getAmount().compareTo(p.getMaxAmount())>0)
            throw new RuntimeException("Kiasi lazima kiwe kati ya "+p.getMinAmount()+" na "+p.getMaxAmount());
        if(r.getDuration()<p.getMinDuration()||r.getDuration()>p.getMaxDuration())
            throw new RuntimeException("Muda lazima uwe kati ya "+p.getMinDuration()+" na "+p.getMaxDuration()+" "+p.getDurationUnit().name().toLowerCase());

        BigDecimal principal=r.getAmount().setScale(2,RoundingMode.HALF_UP);
        BigDecimal interest;
        if(p.getInterestType()==LoanProduct.InterestType.FLAT){
            interest=principal.multiply(p.getInterestRate()).divide(BigDecimal.valueOf(100),2,RoundingMode.HALF_UP);
        }else{
            BigDecimal yearFraction=p.getDurationUnit()==LoanProduct.DurationUnit.DAYS
                    ?BigDecimal.valueOf(r.getDuration()).divide(BigDecimal.valueOf(365),10,RoundingMode.HALF_UP)
                    :BigDecimal.valueOf(r.getDuration()).divide(BigDecimal.valueOf(12),10,RoundingMode.HALF_UP);
            interest=principal.multiply(p.getInterestRate()).divide(BigDecimal.valueOf(100),10,RoundingMode.HALF_UP)
                    .multiply(yearFraction).setScale(2,RoundingMode.HALF_UP);
        }

        BigDecimal fee=Optional.ofNullable(p.getProcessingFee()).orElse(BigDecimal.ZERO).setScale(2,RoundingMode.HALF_UP);
        BigDecimal total=principal.add(interest).add(fee).setScale(2,RoundingMode.HALF_UP);
        int count=installmentCount(r.getDuration(),p);
        BigDecimal installment=total.divide(BigDecimal.valueOf(count),2,RoundingMode.HALF_UP);

        List<LocalDate> dueDates=new ArrayList<>();
        LocalDate start=LocalDate.now();
        for(int i=1;i<=count;i++) dueDates.add(scheduleDate(start,i,r.getDuration(),p));

        LoanQuoteResponse q=new LoanQuoteResponse();
        q.productId=p.getId(); q.productName=p.getName(); q.loanType=p.getLoanType().name();
        q.durationUnit=p.getDurationUnit().name(); q.interestType=p.getInterestType().name();
        q.principal=principal; q.interest=interest; q.processingFee=fee; q.totalRepayment=total;
        q.installmentAmount=installment; q.installmentCount=count;
        q.repaymentFrequency=p.getRepaymentFrequency().name();
        q.lateFee=Optional.ofNullable(p.getLateFee()).orElse(BigDecimal.ZERO);
        q.gracePeriodDays=Optional.ofNullable(p.getGracePeriodDays()).orElse(0);
        q.dueDates=dueDates; q.termsVersion="PRODUCT-"+p.getId()+"-"+p.getCreatedAt();
        return q;
    }

    private int installmentCount(int duration,LoanProduct p){
        return switch(p.getRepaymentFrequency()){
            case ONE_TIME -> 1;
            case DAILY -> p.getDurationUnit()==LoanProduct.DurationUnit.DAYS ? duration : Math.max(1,duration*30);
            case WEEKLY -> p.getDurationUnit()==LoanProduct.DurationUnit.DAYS
                    ? Math.max(1,(int)Math.ceil(duration/7.0))
                    : Math.max(1,(int)Math.ceil(duration*30/7.0));
            case MONTHLY -> p.getDurationUnit()==LoanProduct.DurationUnit.MONTHS
                    ? duration : Math.max(1,(int)Math.ceil(duration/30.0));
        };
    }

    private LocalDate scheduleDate(LocalDate start,int i,int duration,LoanProduct p){
        return switch(p.getRepaymentFrequency()){
            case ONE_TIME -> p.getDurationUnit()==LoanProduct.DurationUnit.DAYS?start.plusDays(duration):start.plusMonths(duration);
            case DAILY -> start.plusDays(i);
            case WEEKLY -> start.plusWeeks(i);
            case MONTHLY -> start.plusMonths(i);
        };
    }
}
