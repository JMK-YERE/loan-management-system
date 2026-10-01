package com.loanapp.repository;

import com.loanapp.model.Loan;
import com.loanapp.model.LoanCollateral;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanCollateralRepository extends JpaRepository<LoanCollateral,Long> {
    List<LoanCollateral> findByLoanOrderByIdAsc(Loan loan);
}