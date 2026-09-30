package com.loanapp.repository;
import com.loanapp.model.LoanApplication;
import com.loanapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface LoanApplicationRepository extends JpaRepository<LoanApplication,Long>{
 List<LoanApplication> findByBorrowerOrderByCreatedAtDesc(User borrower);
 List<LoanApplication> findByStatusOrderByCreatedAtAsc(LoanApplication.Status status);
}
