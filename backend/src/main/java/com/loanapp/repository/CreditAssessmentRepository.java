package com.loanapp.repository;

import com.loanapp.model.CreditAssessment;
import com.loanapp.model.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CreditAssessmentRepository extends JpaRepository<CreditAssessment, Long> {
    Optional<CreditAssessment> findByApplication(LoanApplication application);
}