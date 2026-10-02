package com.loanapp.service;
import com.loanapp.dto.LoanProductRequest;
import com.loanapp.model.LoanProduct;
import com.loanapp.repository.LoanProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class LoanProductService{
 private final LoanProductRepository repo;
 public LoanProductService(LoanProductRepository repo){this.repo=repo;}
 public LoanProduct create(LoanProductRequest r){
  if(r.getMinAmount().compareTo(r.getMaxAmount())>0)throw new RuntimeException("Minimum amount cannot exceed maximum amount");
  if(r.getMinDuration()>r.getMaxDuration())throw new RuntimeException("Minimum duration cannot exceed maximum duration");
  if(repo.findByNameIgnoreCase(r.getName()).isPresent())throw new RuntimeException("Loan product already exists");
  LoanProduct p=new LoanProduct();p.setName(r.getName());p.setLoanType(r.getLoanType());p.setMinAmount(r.getMinAmount());p.setMaxAmount(r.getMaxAmount());p.setMinDuration(r.getMinDuration());p.setMaxDuration(r.getMaxDuration());p.setDurationUnit(r.getDurationUnit());p.setInterestRate(r.getInterestRate());p.setInterestType(r.getInterestType());p.setProcessingFee(r.getProcessingFee());p.setLateFee(r.getLateFee());p.setOtherCharges(r.getOtherCharges());p.setCurrency(r.getCurrency()==null||r.getCurrency().isBlank()?"TZS":r.getCurrency().toUpperCase());p.setTermsVersion("V1-"+System.currentTimeMillis());p.setGracePeriodDays(r.getGracePeriodDays());p.setRepaymentFrequency(r.getRepaymentFrequency());return repo.save(p);
 }
 public List<LoanProduct> active(){return repo.findByActiveTrueOrderByNameAsc();}
 public List<LoanProduct> all(){return repo.findAll();}
 public LoanProduct get(Long id){return repo.findById(id).orElseThrow(()->new RuntimeException("Loan product not found"));}
 public LoanProduct setActive(Long id,boolean active){LoanProduct p=get(id);p.setActive(active);return repo.save(p);}
}