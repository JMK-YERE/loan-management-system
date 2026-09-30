package com.loanapp.repository;

import com.loanapp.model.LoanApplication;
import com.loanapp.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication,Long>{

    @EntityGraph(attributePaths = {"borrower", "product", "lender"})
    List<LoanApplication> findByBorrowerOrderByCreatedAtDesc(User borrower);

    @EntityGraph(attributePaths = {"borrower", "product", "lender"})
    List<LoanApplication> findByStatusOrderByCreatedAtAsc(LoanApplication.Status status);
}
