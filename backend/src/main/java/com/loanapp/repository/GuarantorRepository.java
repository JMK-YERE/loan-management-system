package com.loanapp.repository;

import com.loanapp.model.Guarantor;
import com.loanapp.model.Loan;
import com.loanapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuarantorRepository extends JpaRepository<Guarantor, Long> {

    List<Guarantor> findByLoan(Loan loan);

    List<Guarantor> findByGuarantor(User guarantor);

    List<Guarantor> findByLoanAndStatus(Loan loan, Guarantor.GuarantorStatus status);

    java.util.Optional<Guarantor> findByRemoteTokenHash(String remoteTokenHash);
}
