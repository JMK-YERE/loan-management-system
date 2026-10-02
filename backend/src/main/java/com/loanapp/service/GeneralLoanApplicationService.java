package com.loanapp.service;

import com.loanapp.dto.LoanApplicationRequest;
import com.loanapp.dto.LoanQuoteRequest;
import com.loanapp.dto.LoanQuoteResponse;
import com.loanapp.model.LoanApplication;
import com.loanapp.model.LoanProduct;
import com.loanapp.model.User;
import com.loanapp.model.Loan;
import com.loanapp.repository.LoanApplicationRepository;
import com.loanapp.repository.LoanProductRepository;
import com.loanapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GeneralLoanApplicationService {
 private final LoanApplicationRepository apps; private final UserRepository users; private final LoanProductRepository products;
 private final LoanQuoteService quotes; private final LoanService loans; private final AuditService audit; private final NotificationService notifications;
 public GeneralLoanApplicationService(LoanApplicationRepository apps,UserRepository users,LoanProductRepository products,LoanQuoteService quotes,LoanService loans,AuditService audit,NotificationService notifications){this.apps=apps;this.users=users;this.products=products;this.quotes=quotes;this.loans=loans;this.audit=audit;this.notifications=notifications;}

 @Transactional public LoanApplication submit(LoanApplicationRequest r,String email){
  User b=users.findByEmail(email).orElseThrow(()->new RuntimeException("Mkopaji hajapatikana"));
  if(b.getRole()!=User.Role.BORROWER||!Boolean.TRUE.equals(b.getActive())||b.getStatus()!=User.UserStatus.APPROVED)throw new RuntimeException("Akaunti ya mkopaji haijakamilika au haijaidhinishwa");
  if(!apps.findByBorrowerOrderByCreatedAtDesc(b).stream().noneMatch(a->a.getStatus()==LoanApplication.Status.SUBMITTED||a.getStatus()==LoanApplication.Status.UNDER_REVIEW))throw new RuntimeException("Una ombi la mkopo ambalo bado linashughulikiwa");
  LoanApplication a=new LoanApplication();a.setBorrower(b);a.setAmount(r.getAmount());a.setDuration(r.getDuration());a.setPurpose(r.getPurpose().trim());a.setMonthlyExpenses(r.getMonthlyExpenses());a.setExistingMonthlyDebt(r.getExistingMonthlyDebt());a.setCollateralDescription(r.getCollateralDescription()==null?null:r.getCollateralDescription().trim());a.setCollateralValue(r.getCollateralValue()==null?BigDecimal.ZERO:r.getCollateralValue());a.setCollateralPhotoData(r.getCollateralPhotoData());
  a.setTermsAccepted(Boolean.TRUE.equals(r.getTermsAccepted()));a.setTermsAcceptedAt(Boolean.TRUE.equals(r.getTermsAccepted())?LocalDateTime.now():null);
  if(r.getProductId()!=null){
   LoanProduct product=products.findById(r.getProductId()).orElseThrow(()->new RuntimeException("Loan product haijapatikana"));
   if(!Boolean.TRUE.equals(product.getActive())) throw new RuntimeException("Loan product haifanyi kazi");
   LoanQuoteRequest qreq=new LoanQuoteRequest();qreq.setProductId(product.getId());qreq.setAmount(r.getAmount());qreq.setDuration(r.getDuration());
   LoanQuoteResponse q=quotes.quote(qreq);
   a.setProduct(product);a.setDuration(qreq.getDuration());a.setDurationUnit(product.getDurationUnit());
   a.setInterestSnapshot(q.interest);a.setProcessingFeeSnapshot(q.processingFee);a.setLateFeeSnapshot(q.lateFee);a.setTotalRepaymentSnapshot(q.totalRepayment);
   a.setInstallmentAmountSnapshot(q.installmentAmount);a.setInstallmentCountSnapshot(q.installmentCount);a.setGracePeriodDaysSnapshot(q.gracePeriodDays);
   a.setInterestTypeSnapshot(q.interestType);a.setRepaymentFrequencySnapshot(q.repaymentFrequency);a.setTermsVersion(q.termsVersion);
  } else {
   a.setDurationUnit(LoanProduct.DurationUnit.DAYS);a.setInterestSnapshot(BigDecimal.ZERO);a.setProcessingFeeSnapshot(BigDecimal.ZERO);a.setLateFeeSnapshot(BigDecimal.ZERO);
   a.setTotalRepaymentSnapshot(r.getAmount());a.setInstallmentAmountSnapshot(r.getAmount());a.setInstallmentCountSnapshot(1);a.setTermsVersion("PENDING_LENDER_OFFER");
  }
  LoanApplication saved=apps.save(a);audit.log(email,"GENERAL_LOAN_REQUEST_SUBMITTED","LOAN_APPLICATION",saved.getId(),"Borrower requested a loan without selecting a visible product");
  notifyLenders("Ombi jipya la mkopo #"+saved.getId(),"Mkopaji "+b.getFullName()+" ameomba mkopo wa TZS "+saved.getAmount()+" kwa siku "+saved.getDuration()+". Fungua General Loan Requests kuanza review.");
  return saved;
 }

 @Transactional public LoanApplication startReview(Long id,String email){
  LoanApplication a=get(id);User actor=users.findByEmail(email).orElseThrow();if(actor.getRole()!=User.Role.LENDER&&actor.getRole()!=User.Role.ADMIN)throw new RuntimeException("Huna ruhusa");if(a.getStatus()!=LoanApplication.Status.SUBMITTED)throw new RuntimeException("Ombi haliko SUBMITTED");a.setStatus(LoanApplication.Status.UNDER_REVIEW);if(actor.getRole()==User.Role.LENDER)a.setLender(actor);audit.log(email,"LOAN_APPLICATION_REVIEW","LOAN_APPLICATION",id,"General loan request moved to review");return apps.save(a);
 }

 @Transactional public LoanApplication assignProduct(Long id,Long productId,String email){
  LoanApplication a=get(id);User actor=users.findByEmail(email).orElseThrow();if(actor.getRole()!=User.Role.LENDER&&actor.getRole()!=User.Role.ADMIN)throw new RuntimeException("Huna ruhusa");if(a.getStatus()!=LoanApplication.Status.UNDER_REVIEW)throw new RuntimeException("Ombi lazima liwe UNDER_REVIEW");
  LoanProduct p=products.findById(productId).orElseThrow(()->new RuntimeException("Loan product haijapatikana"));if(!Boolean.TRUE.equals(p.getActive()))throw new RuntimeException("Loan product haifanyi kazi");
  LoanQuoteRequest qreq=new LoanQuoteRequest();qreq.setProductId(productId);qreq.setAmount(a.getAmount());qreq.setDuration(normalizeRequestedDuration(a.getDuration(),p));LoanQuoteResponse q=quotes.quote(qreq);a.setDuration(qreq.getDuration());
  a.setProduct(p);a.setDurationUnit(p.getDurationUnit());a.setInterestSnapshot(q.interest);a.setProcessingFeeSnapshot(q.processingFee);a.setLateFeeSnapshot(q.lateFee);a.setTotalRepaymentSnapshot(q.totalRepayment);a.setInstallmentAmountSnapshot(q.installmentAmount);a.setInstallmentCountSnapshot(q.installmentCount);a.setGracePeriodDaysSnapshot(q.gracePeriodDays);a.setInterestTypeSnapshot(q.interestType);a.setRepaymentFrequencySnapshot(q.repaymentFrequency);a.setTermsVersion(q.termsVersion);a.setTermsAccepted(false);a.setTermsAcceptedAt(null);
  a.setStatus(LoanApplication.Status.OFFER_READY); audit.log(email,"LOAN_PRODUCT_ASSIGNED","LOAN_APPLICATION",id,"Product assigned and borrower offer generated");
  notifications.sendEmail(a.getBorrower().getEmail(),"JmkLoanApp - Loan Offer yako iko tayari","<p>Habari "+a.getBorrower().getFullName()+",</p><p>Offer ya ombi <b>#"+id+"</b> iko tayari.</p><p>Soma PDF ya offer, kagua riba, ada, jumla ya marejesho na ratiba, kisha ukubali kama masharti ni sahihi.</p>");
  notifications.sendSms(a.getBorrower().getPhone(),"JmkLoanApp: Loan Offer #"+id+" iko tayari. Ingia kwenye mfumo, soma offer kisha ukubali au usikubali.");
  return apps.save(a);
 }

 @Transactional public LoanApplication acceptOffer(Long id,String email){
  LoanApplication a=get(id);User b=users.findByEmail(email).orElseThrow();if(!a.getBorrower().getId().equals(b.getId()))throw new RuntimeException("Huna ruhusa");if(a.getProduct()==null)throw new RuntimeException("Offer bado haijaandaliwa");if(a.getStatus()!=LoanApplication.Status.OFFER_READY)throw new RuntimeException("Offer bado haijawa tayari kwa acceptance");
  a.setTermsAccepted(true);a.setTermsAcceptedAt(LocalDateTime.now());a.setStatus(LoanApplication.Status.OFFER_ACCEPTED);audit.log(email,"LOAN_OFFER_ACCEPTED","LOAN_APPLICATION",id,"Borrower reviewed and accepted offer terms");
  if(a.getLender()!=null){notifications.sendEmail(a.getLender().getEmail(),"JmkLoanApp - Borrower amekubali Offer #"+id,"<p>Borrower "+a.getBorrower().getFullName()+" amekubali offer ya application <b>#"+id+"</b>.</p><p>Endelea na credit assessment/final approval.</p>");notifications.sendSms(a.getLender().getPhone(),"JmkLoanApp: Borrower "+a.getBorrower().getFullName()+" amekubali offer #"+id+". Endelea na hatua inayofuata.");}
  return apps.save(a);
 }

 @Transactional public LoanApplication approve(Long id,String email){
  LoanApplication a=get(id);User actor=users.findByEmail(email).orElseThrow();if(actor.getRole()!=User.Role.LENDER&&actor.getRole()!=User.Role.ADMIN)throw new RuntimeException("Huna ruhusa");if(a.getStatus()!=LoanApplication.Status.OFFER_ACCEPTED)throw new RuntimeException("Borrower lazima akubali offer kwanza");if(a.getProduct()==null)throw new RuntimeException("Chagua loan product kwanza");if(!Boolean.TRUE.equals(a.getTermsAccepted()))throw new RuntimeException("Mkopaji lazima asome na akubali offer");if(a.getLender()==null&&actor.getRole()==User.Role.LENDER)a.setLender(actor);if(a.getLender()==null)throw new RuntimeException("Mkopeshaji hajapangiwa");
  LoanRequestMapper mapper=new LoanRequestMapper();Loan loan=loans.createLoan(mapper.map(a),a.getLender().getEmail(),a.getBorrower().getId());a.setLoan(loan);a.setStatus(LoanApplication.Status.CONVERTED);audit.log(email,"GENERAL_LOAN_APPROVED","LOAN_APPLICATION",id,"General request converted to loan #"+loan.getId());
  notifications.sendEmail(a.getBorrower().getEmail(),"JmkLoanApp - Loan #"+loan.getId()+" imeundwa","<p>Habari "+a.getBorrower().getFullName()+",</p><p>Offer yako imekubaliwa na lender na loan <b>#"+loan.getId()+"</b> imeundwa.</p><p>Fungua Agreement, soma PDF na endelea na hatua za kusaini/disbursement.</p>");
  notifications.sendSms(a.getBorrower().getPhone(),"JmkLoanApp: Loan #"+loan.getId()+" imeundwa. Ingia kwenye mfumo kufungua Agreement na hatua za kusaini.");
  return apps.save(a);
 }

 @Transactional
 public LoanApplication captureCollateralPhoto(Long id,String photoData,String email){
  LoanApplication a=get(id); User actor=users.findByEmail(email).orElseThrow();
  if(actor.getRole()!=User.Role.LENDER&&actor.getRole()!=User.Role.ADMIN) throw new RuntimeException("Huna ruhusa");
  if(a.getStatus()==LoanApplication.Status.CONVERTED) throw new RuntimeException("Application tayari imegeuzwa kuwa loan");
  if(photoData==null||photoData.isBlank()||!photoData.startsWith("data:image/")) throw new RuntimeException("Picha ya dhamana si sahihi");
  if(photoData.length()>5000000) throw new RuntimeException("Picha ni kubwa sana");
  a.setCollateralPhotoData(photoData);
  audit.log(email,"COLLATERAL_PHOTO_CAPTURED","LOAN_APPLICATION",id,"Lender captured collateral evidence");
  return apps.save(a);
 }

 public List<LoanApplication> mine(String email){User b=users.findByEmail(email).orElseThrow();return apps.findByBorrowerOrderByCreatedAtDesc(b);}
 public List<LoanApplication> pending(){
  List<LoanApplication> out=new java.util.ArrayList<>(apps.findByStatusOrderByCreatedAtAsc(LoanApplication.Status.SUBMITTED));
  out.addAll(apps.findByStatusOrderByCreatedAtAsc(LoanApplication.Status.UNDER_REVIEW));
  out.sort(java.util.Comparator.comparing(LoanApplication::getCreatedAt));
  return out;
}
 private int normalizeRequestedDuration(int requestedDays,LoanProduct product){
  if(product.getDurationUnit()==LoanProduct.DurationUnit.DAYS) return requestedDays;
  return Math.max(1,(int)Math.ceil(requestedDays/30.0));
 }
 private LoanApplication get(Long id){return apps.findById(id).orElseThrow(()->new RuntimeException("Ombi halijapatikana"));}
 private void notifyLenders(String subject,String message){
  for(User lender:users.findByRole(User.Role.LENDER)){
   if(!Boolean.TRUE.equals(lender.getActive())||lender.getStatus()!=User.UserStatus.APPROVED) continue;
   notifications.sendEmail(lender.getEmail(),"JmkLoanApp - "+subject,"<p>Habari "+lender.getFullName()+",</p><p>"+message+"</p>");
   notifications.sendSms(lender.getPhone(),"JmkLoanApp: "+message);
  }
 }

 static class LoanRequestMapper{com.loanapp.dto.LoanRequest map(LoanApplication a){com.loanapp.dto.LoanRequest r=new com.loanapp.dto.LoanRequest();r.setLoanProductId(a.getProduct().getId());r.setAmount(a.getAmount());r.setDurationMonths(a.getDuration());r.setPurpose(a.getPurpose());r.setInterestRate(a.getInterestSnapshot());r.setProcessingFee(a.getProcessingFeeSnapshot());r.setLawyerRequired(false);return r;}}
}
