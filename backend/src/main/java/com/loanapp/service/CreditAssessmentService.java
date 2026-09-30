package com.loanapp.service;
import com.loanapp.dto.CreditAssessmentRequest;
import com.loanapp.model.*;
import com.loanapp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
@Service
public class CreditAssessmentService{
 private final CreditAssessmentRepository assessments; private final LoanApplicationRepository applications; private final LoanRepository loans; private final UserRepository users; private final AuditService audit;
 public CreditAssessmentService(CreditAssessmentRepository a,LoanApplicationRepository ap,LoanRepository l,UserRepository u,AuditService au){assessments=a;applications=ap;loans=l;users=u;audit=au;}
 @Transactional public CreditAssessment assess(Long applicationId,CreditAssessmentRequest request,String actorEmail){
  User actor=users.findByEmail(actorEmail).orElseThrow(()->new RuntimeException("Mtumiaji hajapatikana"));
  if(actor.getRole()!=User.Role.ADMIN&&actor.getRole()!=User.Role.LENDER)throw new RuntimeException("Huna ruhusa ya kufanya credit assessment");
  LoanApplication app=applications.findById(applicationId).orElseThrow(()->new RuntimeException("Loan application haijapatikana"));
  BigDecimal income=app.getBorrower().getMonthlyIncome()==null?BigDecimal.ZERO:app.getBorrower().getMonthlyIncome();
  BigDecimal expenses=request.getMonthlyExpenses()==null?BigDecimal.ZERO:request.getMonthlyExpenses();
  BigDecimal debt=request.getExistingMonthlyDebt()==null?BigDecimal.ZERO:request.getExistingMonthlyDebt();
  if(income.signum()<0||expenses.signum()<0||debt.signum()<0)throw new RuntimeException("Taarifa za kifedha si sahihi");
  BigDecimal surplus=income.subtract(expenses).subtract(debt).max(BigDecimal.ZERO);
  BigDecimal installment=app.getAmount().divide(BigDecimal.valueOf(Math.max(1,app.getDurationMonths())),2,RoundingMode.HALF_UP);
  int score=0;if(app.getBorrower().getNidaNumber()!=null&&!app.getBorrower().getNidaNumber().isBlank())score+=15;if(income.signum()>0)score+=20;if(surplus.signum()>0)score+=20;
  boolean affordable=surplus.signum()>0&&installment.compareTo(surplus.multiply(new BigDecimal("0.50")))<=0;
  boolean borderline=surplus.signum()>0&&installment.compareTo(surplus.multiply(new BigDecimal("0.75")))<=0;
  if(affordable)score+=25;else if(borderline)score+=15;
  List<Loan> history=loans.findByBorrower(app.getBorrower());long defaults=history.stream().filter(l->l.getStatus()==Loan.LoanStatus.DEFAULTED).count();if(defaults==0)score+=20;else if(defaults==1)score+=8;
  CreditAssessment.Affordability affordability=affordable?CreditAssessment.Affordability.PASS:(borderline?CreditAssessment.Affordability.BORDERLINE:CreditAssessment.Affordability.FAIL);
  CreditAssessment.RiskLevel risk;if(affordability==CreditAssessment.Affordability.FAIL||defaults>=2)risk=CreditAssessment.RiskLevel.HIGH;else if(score>=75)risk=CreditAssessment.RiskLevel.LOW;else if(score>=55)risk=CreditAssessment.RiskLevel.MEDIUM;else risk=CreditAssessment.RiskLevel.REVIEW_REQUIRED;
  BigDecimal recommended=surplus.multiply(new BigDecimal("0.50")).multiply(BigDecimal.valueOf(Math.max(1,app.getDurationMonths()))).setScale(2,RoundingMode.HALF_UP);if(recommended.compareTo(app.getAmount())>0)recommended=app.getAmount();
  CreditAssessment a=assessments.findByApplication(app).orElseGet(CreditAssessment::new);a.setApplication(app);a.setMonthlyIncome(income);a.setMonthlyExpenses(expenses);a.setExistingMonthlyDebt(debt);a.setMonthlySurplus(surplus);a.setEstimatedInstallment(installment);a.setRecommendedAmount(recommended);a.setScore(score);a.setRiskLevel(risk);a.setAffordability(affordability);a.setAssessmentSummary("Internal assessment: income="+income+", expenses="+expenses+", existing debt="+debt+", prior defaults="+defaults+". Decision support only; not automatic approval.");
  CreditAssessment saved=assessments.save(a);audit.log(actorEmail,"CREDIT_ASSESSMENT_CREATED","LOAN_APPLICATION",applicationId,"score="+score+", risk="+risk+", affordability="+affordability);return saved;
 }
 @Transactional(readOnly=true) public CreditAssessment get(Long applicationId,String actorEmail){
  User actor=users.findByEmail(actorEmail).orElseThrow(()->new RuntimeException("Mtumiaji hajapatikana"));if(actor.getRole()!=User.Role.ADMIN&&actor.getRole()!=User.Role.LENDER)throw new RuntimeException("Huna ruhusa ya kuona credit assessment");
  LoanApplication app=applications.findById(applicationId).orElseThrow(()->new RuntimeException("Loan application haijapatikana"));return assessments.findByApplication(app).orElseThrow(()->new RuntimeException("Credit assessment haijafanyika bado"));
 }
}