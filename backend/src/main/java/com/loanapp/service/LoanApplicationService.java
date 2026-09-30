package com.loanapp.service;

import com.loanapp.dto.LoanApplicationRequest;
import com.loanapp.model.*;
import com.loanapp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class LoanApplicationService {
 private final LoanApplicationRepository apps; private final UserRepository users; private final LoanProductRepository products;
 private final LoanService loans; private final AuditService audit;
 public LoanApplicationService(LoanApplicationRepository apps,UserRepository users,LoanProductRepository products,LoanService loans,AuditService audit){this.apps=apps;this.users=users;this.products=products;this.loans=loans;this.audit=audit;}

 @Transactional
 public LoanApplication submit(LoanApplicationRequest r,String email){
  User borrower=users.findByEmail(email).orElseThrow(()->new RuntimeException("Mkopaji hajapatikana"));
  if(borrower.getRole()!=User.Role.BORROWER||!Boolean.TRUE.equals(borrower.getActive())||borrower.getStatus()!=User.UserStatus.APPROVED) throw new RuntimeException("Akaunti ya mkopaji haijakamilika au haijaidhinishwa");
  LoanProduct p=products.findById(r.getProductId()).orElseThrow(()->new RuntimeException("Loan product haijapatikana"));
  if(!Boolean.TRUE.equals(p.getActive())) throw new RuntimeException("Loan product haifanyi kazi");
  if(r.getAmount().compareTo(p.getMinAmount())<0||r.getAmount().compareTo(p.getMaxAmount())>0) throw new RuntimeException("Kiasi lazima kiwe kati ya "+p.getMinAmount()+" na "+p.getMaxAmount());
  if(r.getDurationMonths()<p.getMinDuration()||r.getDurationMonths()>p.getMaxDuration()) throw new RuntimeException("Muda haupo kwenye kiwango cha bidhaa");
  if(!apps.findByBorrowerOrderByCreatedAtDesc(borrower).stream().noneMatch(a->a.getStatus()==LoanApplication.Status.SUBMITTED||a.getStatus()==LoanApplication.Status.UNDER_REVIEW)) throw new RuntimeException("Una ombi la mkopo ambalo bado linashughulikiwa");
  LoanApplication a=new LoanApplication(); a.setBorrower(borrower);a.setProduct(p);a.setAmount(r.getAmount());a.setDurationMonths(r.getDurationMonths());a.setPurpose(r.getPurpose().trim());
  LoanApplication saved=apps.save(a); audit.log(email,"LOAN_APPLICATION_SUBMITTED","LOAN_APPLICATION",saved.getId(),"Borrower submitted loan application"); return saved;
 }
 public List<LoanApplication> mine(String email){User u=users.findByEmail(email).orElseThrow();return apps.findByBorrowerOrderByCreatedAtDesc(u);}
 public List<LoanApplication> pending(){return apps.findByStatusOrderByCreatedAtAsc(LoanApplication.Status.SUBMITTED);}
 @Transactional public LoanApplication startReview(Long id,String email){LoanApplication a=get(id);User l=users.findByEmail(email).orElseThrow();if(l.getRole()!=User.Role.LENDER&&l.getRole()!=User.Role.ADMIN)throw new RuntimeException("Huna ruhusa");if(a.getStatus()!=LoanApplication.Status.SUBMITTED)throw new RuntimeException("Ombi haliko SUBMITTED");a.setStatus(LoanApplication.Status.UNDER_REVIEW);if(l.getRole()==User.Role.LENDER)a.setLender(l);audit.log(email,"LOAN_APPLICATION_REVIEW","LOAN_APPLICATION",id,"Application moved to review");return apps.save(a);}
 @Transactional public LoanApplication approve(Long id,String email){LoanApplication a=get(id);User l=users.findByEmail(email).orElseThrow();if(l.getRole()!=User.Role.LENDER&&l.getRole()!=User.Role.ADMIN)throw new RuntimeException("Huna ruhusa");if(a.getStatus()!=LoanApplication.Status.UNDER_REVIEW)throw new RuntimeException("Ombi lazima liwe kwenye review");if(a.getLender()==null&&l.getRole()==User.Role.LENDER)a.setLender(l);if(a.getLender()==null)throw new RuntimeException("Mkopeshaji hajapangiwa");
  LoanRequestMapper m=new LoanRequestMapper(); Loan loan=loans.createLoan(m.map(a),a.getLender().getEmail(),a.getBorrower().getId()); a.setLoan(loan);a.setStatus(LoanApplication.Status.CONVERTED);audit.log(email,"LOAN_APPLICATION_APPROVED","LOAN_APPLICATION",id,"Application converted to loan #"+loan.getId());return apps.save(a);}
 @Transactional public LoanApplication reject(Long id,String email,String reason){LoanApplication a=get(id);User l=users.findByEmail(email).orElseThrow();if(l.getRole()!=User.Role.LENDER&&l.getRole()!=User.Role.ADMIN)throw new RuntimeException("Huna ruhusa");if(a.getStatus()!=LoanApplication.Status.SUBMITTED&&a.getStatus()!=LoanApplication.Status.UNDER_REVIEW)throw new RuntimeException("Ombi haliwezi kukataliwa");a.setStatus(LoanApplication.Status.REJECTED);a.setRejectionReason(reason);audit.log(email,"LOAN_APPLICATION_REJECTED","LOAN_APPLICATION",id,"Application rejected");return apps.save(a);}
 private LoanApplication get(Long id){return apps.findById(id).orElseThrow(()->new RuntimeException("Ombi halijapatikana"));}
 static class LoanRequestMapper{
  LoanRequest map(LoanApplication a){LoanRequest r=new LoanRequest();r.setLoanProductId(a.getProduct().getId());r.setAmount(a.getAmount());r.setDurationMonths(a.getDurationMonths());r.setPurpose(a.getPurpose());r.setInterestRate(a.getProduct().getInterestRate());r.setProcessingFee(a.getProduct().getProcessingFee());r.setLawyerRequired(false);return r;}
 }
}
