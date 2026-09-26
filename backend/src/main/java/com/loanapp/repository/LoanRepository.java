package com.loanapp.repository;

import com.loanapp.model.Loan;
import com.loanapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByBorrower(User borrower);

    List<Loan> findByLender(User lender);

    List<Loan> findByStatus(Loan.LoanStatus status);

    List<Loan> findByBorrowerAndStatus(User borrower, Loan.LoanStatus status);
}
