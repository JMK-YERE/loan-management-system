package com.loanapp.repository;

import com.loanapp.model.Loan;
import com.loanapp.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    @org.springframework.data.jpa.repository.Query("select coalesce(sum(l.amount),0) from Loan l")
    java.math.BigDecimal sumAmount();
    long countByStatus(Loan.LoanStatus status);
    long countByStatusIn(java.util.Collection<Loan.LoanStatus> statuses);

    @EntityGraph(attributePaths = {"borrower", "lender", "loanProduct"})
    List<Loan> findByBorrower(User borrower);

    @EntityGraph(attributePaths = {"borrower", "lender", "loanProduct"})
    List<Loan> findByLender(User lender);

    @EntityGraph(attributePaths = {"borrower", "lender", "loanProduct"})
    List<Loan> findByStatus(Loan.LoanStatus status);

    @EntityGraph(attributePaths = {"borrower", "lender", "loanProduct"})
    List<Loan> findByBorrowerAndStatus(User borrower, Loan.LoanStatus status);
}
