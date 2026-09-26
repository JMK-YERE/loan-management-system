package com.loanapp.service;

import com.loanapp.dto.LoanRequest;
import com.loanapp.model.Loan;
import com.loanapp.model.User;
import com.loanapp.repository.LoanRepository;
import com.loanapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    public Loan createLoan(LoanRequest request, String lenderEmail, Long borrowerId) {
        User lender = userRepository.findByEmail(lenderEmail)
                .orElseThrow(() -> new RuntimeException("Mkopeshaji hajapatikana"));
        User borrower = userRepository.findById(borrowerId)
                .orElseThrow(() -> new RuntimeException("Mkopaji hajapatikana"));

        BigDecimal totalRepayment = calculateTotalRepayment(
                request.getAmount(),
                request.getInterestRate(),
                request.getDurationMonths()
        );

        Loan loan = Loan.builder()
                .lender(lender)
                .borrower(borrower)
                .amount(request.getAmount())
                .interestRate(request.getInterestRate())
                .durationMonths(request.getDurationMonths())
                .totalRepayment(totalRepayment)
                .purpose(request.getPurpose())
                .processingFee(request.getProcessingFee() != null ? request.getProcessingFee() : BigDecimal.ZERO)
                .lawyerRequired(request.getLawyerRequired() != null ? request.getLawyerRequired() : false)
                .lawyerFee(request.getLawyerFee() != null ? request.getLawyerFee() : BigDecimal.ZERO)
                .status(Loan.LoanStatus.PENDING)
                .build();

        return loanRepository.save(loan);
    }

    private BigDecimal calculateTotalRepayment(BigDecimal amount, BigDecimal interestRate, Integer months) {
        BigDecimal interest = amount
                .multiply(interestRate)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(months))
                .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);

        return amount.add(interest).setScale(2, RoundingMode.HALF_UP);
    }

    public List<Loan> getLoansByLender(String lenderEmail) {
        User lender = userRepository.findByEmail(lenderEmail)
                .orElseThrow(() -> new RuntimeException("Mkopeshaji hajapatikana"));
        return loanRepository.findByLender(lender);
    }

    public List<Loan> getLoansByBorrower(String borrowerEmail) {
        User borrower = userRepository.findByEmail(borrowerEmail)
                .orElseThrow(() -> new RuntimeException("Mkopaji hajapatikana"));
        return loanRepository.findByBorrower(borrower);
    }

    public Loan approveLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Mkopo haujapatikana"));
        loan.setStatus(Loan.LoanStatus.APPROVED);
        return loanRepository.save(loan);
    }

    public Loan rejectLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Mkopo haujapatikana"));
        loan.setStatus(Loan.LoanStatus.REJECTED);
        return loanRepository.save(loan);
    }

    public Loan getLoanById(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mkopo haujapatikana"));
    }
}
