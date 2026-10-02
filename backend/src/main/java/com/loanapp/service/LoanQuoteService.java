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
        int count=installmentCount(r.getDuration(),p);
        BigDecimal periodicRate=periodicRate(p);
        BigDecimal interest;
        BigDecimal installment;
        if(p.getInterestType()==LoanProduct.InterestType.REDUCING_BALANCE){
            if(periodicRate.signum()==0) installment=principal.divide(BigDecimal.valueOf(count),2,RoundingMode.HALF_UP);
            else {
                BigDecimal onePlus=BigDecimal.ONE.add(periodicRate);
                BigDecimal pow=onePlus.pow(count);
                installment=principal.multiply(periodicRate).multiply(pow).divide(pow.subtract(BigDecimal.ONE),10,RoundingMode.HALF_UP).setScale(2,RoundingMode.HALF_UP);
            }
            BigDecimal balance=principal; interest=BigDecimal.ZERO;
            for(int i=0;i<count;i++){BigDecimal in=balance.multiply(periodicRate).setScale(2,RoundingMode.HALF_UP);BigDecimal principalPart=installment.subtract(in).max(BigDecimal.ZERO);if(i==count-1)principalPart=balance;interest=interest.add(in);balance=balance.subtract(principalPart).max(BigDecimal.ZERO);}
        } else if(p.getInterestType()==LoanProduct.InterestType.FLAT){
            BigDecimal durationFactor=p.getDurationUnit()==LoanProduct.DurationUnit.DAYS
                    ?BigDecimal.valueOf(r.getDuration()).divide(BigDecimal.valueOf(30),10,RoundingMode.HALF_UP)
                    :BigDecimal.valueOf(r.getDuration());
            interest=principal.multiply(p.getInterestRate()).divide(BigDecimal.valueOf(100),10,RoundingMode.HALF_UP).multiply(durationFactor).setScale(2,RoundingMode.HALF_UP);
            installment=principal.add(interest).setScale(2,RoundingMode.HALF_UP).divide(BigDecimal.valueOf(count),2,RoundingMode.HALF_UP);
        } else {
            BigDecimal yearFraction=p.getDurationUnit()==LoanProduct.DurationUnit.DAYS
                    ?BigDecimal.valueOf(r.getDuration()).divide(BigDecimal.valueOf(365),10,RoundingMode.HALF_UP)
                    :BigDecimal.valueOf(r.getDuration()).divide(BigDecimal.valueOf(12),10,RoundingMode.HALF_UP);
            interest=principal.multiply(p.getInterestRate()).divide(BigDecimal.valueOf(100),10,RoundingMode.HALF_UP).multiply(yearFraction).setScale(2,RoundingMode.HALF_UP);
            installment=principal.add(interest).divide(BigDecimal.valueOf(count),2,RoundingMode.HALF_UP);
        }

        BigDecimal fee=Optional.ofNullable(p.getProcessingFee()).orElse(BigDecimal.ZERO).setScale(2,RoundingMode.HALF_UP);
        BigDecimal other=Optional.ofNullable(p.getOtherCharges()).orElse(BigDecimal.ZERO).setScale(2,RoundingMode.HALF_UP);
        BigDecimal total=principal.add(interest).add(fee).add(other).setScale(2,RoundingMode.HALF_UP);
        installment=total.divide(BigDecimal.valueOf(count),2,RoundingMode.HALF_UP);

        List<LocalDate> dueDates=new ArrayList<>();
        LocalDate start=LocalDate.now();
        for(int i=1;i<=count;i++) dueDates.add(scheduleDate(start,i,r.getDuration(),p));

        LoanQuoteResponse q=new LoanQuoteResponse();
        q.productId=p.getId(); q.productName=p.getName(); q.loanType=p.getLoanType().name();
        q.durationUnit=p.getDurationUnit().name(); q.interestType=p.getInterestType().name();
        q.principal=principal; q.interest=interest; q.processingFee=fee; q.totalRepayment=total; q.otherCharges=other; q.currency=p.getCurrency();
        q.installmentAmount=installment; q.installmentCount=count;
        q.repaymentFrequency=p.getRepaymentFrequency().name();
        q.lateFee=Optional.ofNullable(p.getLateFee()).orElse(BigDecimal.ZERO);
        q.gracePeriodDays=Optional.ofNullable(p.getGracePeriodDays()).orElse(0);
        q.dueDates=dueDates; q.termsVersion=p.getTermsVersion()+"|PRODUCT-"+p.getId()+"|RATE-"+p.getInterestRate()+"|TYPE-"+p.getInterestType();
        return q;
    }

    private BigDecimal periodicRate(LoanProduct p){
        if(p.getInterestType()!=LoanProduct.InterestType.REDUCING_BALANCE)return BigDecimal.ZERO;
        if(p.getRepaymentFrequency()==LoanProduct.RepaymentFrequency.DAILY)return p.getInterestRate().divide(BigDecimal.valueOf(36500),12,RoundingMode.HALF_UP);
        if(p.getRepaymentFrequency()==LoanProduct.RepaymentFrequency.WEEKLY)return p.getInterestRate().divide(BigDecimal.valueOf(5200),12,RoundingMode.HALF_UP);
        return p.getInterestRate().divide(BigDecimal.valueOf(1200),12,RoundingMode.HALF_UP);
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
