package com.loanapp.service;

import com.loanapp.dto.LoanRequest;
import com.loanapp.model.*;
import com.loanapp.repository.LoanProductRepository;
import com.loanapp.repository.GuarantorRepository;
import com.loanapp.repository.LoanRepository;
import com.loanapp.repository.UserRepository;
import com.loanapp.repository.SignatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {
 @Autowired private LoanRepository loanRepository;
 @Autowired private UserRepository userRepository;
 @Autowired private LoanProductRepository productRepository;
 @Autowired private RepaymentScheduleService scheduleService;
 @Autowired private AuditService auditService;
 @Autowired private GuarantorRepository guarantorRepository;
 @Autowired private SignatureRepository signatureRepository;

 @Transactional
 public Loan createLoan(LoanRequest request,String lenderEmail,Long borrowerId){
  User lender=userRepository.findByEmail(lenderEmail).orElseThrow(()->new RuntimeException("Mkopeshaji hajapatikana"));
  User borrower=userRepository.findById(borrowerId).orElseThrow(()->new RuntimeException("Mkopaji hajapatikana"));
  if(borrower.getRole()!=User.Role.BORROWER) throw new RuntimeException("Mtumiaji si mkopaji");

  LoanProduct product=null;
  if(request.getLoanProductId()!=null){
   product=productRepository.findById(request.getLoanProductId()).orElseThrow(()->new RuntimeException("Loan product haijapatikana"));
   if(!Boolean.TRUE.equals(product.getActive())) throw new RuntimeException("Loan product haifanyi kazi");
   if(request.getAmount().compareTo(product.getMinAmount())<0||request.getAmount().compareTo(product.getMaxAmount())>0) throw new RuntimeException("Kiasi hakipo kwenye kiwango cha loan product");
   if(request.getDurationMonths()<product.getMinDuration()||request.getDurationMonths()>product.getMaxDuration()) throw new RuntimeException("Muda haupo kwenye kiwango cha loan product");
  }

  BigDecimal rate=product!=null?product.getInterestRate():request.getInterestRate();
  BigDecimal processing=product!=null?product.getProcessingFee():(request.getProcessingFee()!=null?request.getProcessingFee():BigDecimal.ZERO);
  BigDecimal lawyer=Boolean.TRUE.equals(request.getLawyerRequired())&&request.getLawyerFee()!=null?request.getLawyerFee():BigDecimal.ZERO;
  BigDecimal total=calculateTotalRepayment(request.getAmount(),rate,request.getDurationMonths(),product).add(processing).add(lawyer).setScale(2,RoundingMode.HALF_UP);

  Loan loan=Loan.builder().lender(lender).borrower(borrower).amount(request.getAmount()).interestRate(rate)
   .durationMonths(request.getDurationMonths()).totalRepayment(total).purpose(request.getPurpose())
   .processingFee(processing).lawyerRequired(Boolean.TRUE.equals(request.getLawyerRequired())).lawyerFee(lawyer)
   .status(Loan.LoanStatus.PENDING).build();
  if(product!=null){loan.setLoanProduct(product);loan.setDurationUnit(product.getDurationUnit());}
  Loan saved=loanRepository.save(loan);
  auditService.log(lenderEmail,"LOAN_CREATED","LOAN",saved.getId(),"Loan created for borrower "+borrower.getId()+(product!=null?" using "+product.getName():""));
  return saved;
 }

 private BigDecimal calculateTotalRepayment(BigDecimal amount,BigDecimal rate,Integer duration,LoanProduct product){
  BigDecimal interest;
  if(product!=null && product.getInterestType()==LoanProduct.InterestType.FLAT){
   interest=amount.multiply(rate).divide(BigDecimal.valueOf(100),2,RoundingMode.HALF_UP);
  } else {
   BigDecimal periodFraction = product != null && product.getDurationUnit() == LoanProduct.DurationUnit.DAYS
    ? BigDecimal.valueOf(duration).divide(BigDecimal.valueOf(365),10,RoundingMode.HALF_UP)
    : BigDecimal.valueOf(duration).divide(BigDecimal.valueOf(12),10,RoundingMode.HALF_UP);
   interest=amount.multiply(rate).divide(BigDecimal.valueOf(100),10,RoundingMode.HALF_UP)
    .multiply(periodFraction).setScale(2,RoundingMode.HALF_UP);
  }
  return amount.add(interest).setScale(2,RoundingMode.HALF_UP);
 }

 public List<Loan> getLoansByLender(String email){User u=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("Mkopeshaji hajapatikana"));return loanRepository.findByLender(u);}
 public List<Loan> getLoansByBorrower(String email){User u=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("Mkopaji hajapatikana"));return loanRepository.findByBorrower(u);}

 @Transactional
 public Loan approveLoan(Long id,String actorEmail){
  Loan loan=getLoanById(id);authorizeLenderOrAdmin(loan,actorEmail);
  if(loan.getStatus()!=Loan.LoanStatus.PENDING) throw new RuntimeException("Mkopo huu hauko kwenye hatua ya kusubiri idhini");
  if(loan.getLoanProduct()!=null && loan.getLoanProduct().getLoanType()==LoanProduct.LoanType.INSTALLMENT && guarantorRepository.findByLoanAndStatus(loan,Guarantor.GuarantorStatus.APPROVED).isEmpty()) throw new RuntimeException("Mdhamini aliyeidhinishwa anahitajika kabla ya approval ya mwisho");
  loan.setStatus(Loan.LoanStatus.APPROVED);
  Loan saved=loanRepository.save(loan);
  if(loan.getLoanProduct()!=null){
   scheduleService.generate(saved,loan.getLoanProduct(),LocalDate.now());
   saved.setNextDueDate(scheduleService.byLoan(saved).get(0).getDueDate());
   saved=loanRepository.save(saved);
  }
  auditService.log(actorEmail,"LOAN_APPROVED","LOAN",id,"Loan approved and repayment schedule prepared");
  return saved;
 }

 @Transactional
 public Loan releaseApprovedLoan(Long id,String actorEmail){
  Loan loan=getLoanById(id); authorizeLenderOrAdmin(loan,actorEmail);
  if(loan.getStatus()!=Loan.LoanStatus.APPROVED) throw new RuntimeException("Mkopo lazima uwe APPROVED");
  if(signatureRepository.findByLoanAndSignatureType(loan,Signature.SignatureType.BORROWER).isEmpty()) throw new RuntimeException("Sahihi ya mkopaji inahitajika");
  if(signatureRepository.findByLoanAndSignatureType(loan,Signature.SignatureType.LENDER).isEmpty()) throw new RuntimeException("Sahihi ya mkopeshaji inahitajika");
  if(loan.getLoanProduct()!=null && loan.getLoanProduct().getLoanType()==LoanProduct.LoanType.INSTALLMENT
      && guarantorRepository.findByLoanAndStatus(loan,Guarantor.GuarantorStatus.APPROVED).stream()
          .anyMatch(g -> signatureRepository.findByLoanAndSignatureType(loan,Signature.SignatureType.GUARANTOR).stream()
              .noneMatch(s -> s.getUser().getId().equals(g.getGuarantor().getId()))))
      throw new RuntimeException("Sahihi ya mdhamini aliyeidhinishwa inahitajika kabla ya disbursement");
  loan.setStatus(Loan.LoanStatus.DISBURSED); loan.setDisbursementDate(LocalDate.now());
  Loan saved=loanRepository.save(loan); auditService.log(actorEmail,"LOAN_DISBURSED","LOAN",id,"Approved loan released after required signatures"); return saved;
 }

 public Loan rejectLoan(Long id,String actorEmail){
  Loan loan=getLoanById(id);authorizeLenderOrAdmin(loan,actorEmail);
  if(loan.getStatus()!=Loan.LoanStatus.PENDING) throw new RuntimeException("Mkopo huu hauko kwenye hatua ya kusubiri idhini");
  loan.setStatus(Loan.LoanStatus.REJECTED);Loan saved=loanRepository.save(loan);
  auditService.log(actorEmail,"LOAN_REJECTED","LOAN",id,"Loan rejected");return saved;
 }

 private void authorizeLenderOrAdmin(Loan loan,String email){
  User actor=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("Mtumiaji hajapatikana"));
  if(actor.getRole()!=User.Role.ADMIN&&!loan.getLender().getId().equals(actor.getId()))throw new RuntimeException("Huna ruhusa ya kubadilisha mkopo huu");
 }

 public Loan getLoanById(Long id){return loanRepository.findById(id).orElseThrow(()->new RuntimeException("Mkopo haujapatikana"));}

 public boolean canView(Loan loan,String email){
  User actor=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("Mtumiaji hajapatikana"));
  return actor.getRole()==User.Role.ADMIN||loan.getBorrower().getId().equals(actor.getId())||loan.getLender().getId().equals(actor.getId());
 }
}