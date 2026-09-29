package com.loanapp.service;

import com.loanapp.dto.LoanRequest;
import com.loanapp.model.Loan;
import com.loanapp.model.User;
import com.loanapp.repository.LoanRepository;
import com.loanapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class LoanService {
    @Autowired private LoanRepository loanRepository;
    @Autowired private UserRepository userRepository;

    @Transactional
    public Loan createLoan(LoanRequest request, String lenderEmail, Long borrowerId) {
        User lender=userRepository.findByEmail(lenderEmail).orElseThrow(()->new RuntimeException("Mkopeshaji hajapatikana"));
        User borrower=userRepository.findById(borrowerId).orElseThrow(()->new RuntimeException("Mkopaji hajapatikana"));
        if(borrower.getRole()!=User.Role.BORROWER) throw new RuntimeException("Mtumiaji si mkopaji");
        BigDecimal processing=request.getProcessingFee()!=null?request.getProcessingFee():BigDecimal.ZERO;
        BigDecimal lawyer=request.getLawyerRequired()!=null&&request.getLawyerRequired()&&request.getLawyerFee()!=null?request.getLawyerFee():BigDecimal.ZERO;
        BigDecimal total=calculateTotalRepayment(request.getAmount(),request.getInterestRate(),request.getDurationMonths()).add(processing).add(lawyer).setScale(2,RoundingMode.HALF_UP);
        Loan loan=Loan.builder().lender(lender).borrower(borrower).amount(request.getAmount()).interestRate(request.getInterestRate()).durationMonths(request.getDurationMonths()).totalRepayment(total).purpose(request.getPurpose()).processingFee(processing).lawyerRequired(Boolean.TRUE.equals(request.getLawyerRequired())).lawyerFee(lawyer).status(Loan.LoanStatus.PENDING).build();
        return loanRepository.save(loan);
    }

    private BigDecimal calculateTotalRepayment(BigDecimal amount,BigDecimal rate,Integer months){
        BigDecimal interest=amount.multiply(rate).divide(BigDecimal.valueOf(100),2,RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(months)).divide(BigDecimal.valueOf(12),2,RoundingMode.HALF_UP);
        return amount.add(interest).setScale(2,RoundingMode.HALF_UP);
    }

    public List<Loan> getLoansByLender(String email){User u=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("Mkopeshaji hajapatikana"));return loanRepository.findByLender(u);}
    public List<Loan> getLoansByBorrower(String email){User u=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("Mkopaji hajapatikana"));return loanRepository.findByBorrower(u);}

    public Loan approveLoan(Long id,String actorEmail){
        Loan loan=getLoanById(id); authorizeLenderOrAdmin(loan,actorEmail);
        if(loan.getStatus()!=Loan.LoanStatus.PENDING) throw new RuntimeException("Mkopo huu hauko kwenye hatua ya kusubiri idhini");
        loan.setStatus(Loan.LoanStatus.APPROVED); return loanRepository.save(loan);
    }
    public Loan rejectLoan(Long id,String actorEmail){
        Loan loan=getLoanById(id); authorizeLenderOrAdmin(loan,actorEmail);
        if(loan.getStatus()!=Loan.LoanStatus.PENDING) throw new RuntimeException("Mkopo huu hauko kwenye hatua ya kusubiri idhini");
        loan.setStatus(Loan.LoanStatus.REJECTED); return loanRepository.save(loan);
    }
    private void authorizeLenderOrAdmin(Loan loan,String email){
        User actor=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("Mtumiaji hajapatikana"));
        if(actor.getRole()!=User.Role.ADMIN && !loan.getLender().getId().equals(actor.getId())) throw new RuntimeException("Huna ruhusa ya kubadilisha mkopo huu");
    }
    public Loan getLoanById(Long id){return loanRepository.findById(id).orElseThrow(()->new RuntimeException("Mkopo haujapatikana"));}
}
