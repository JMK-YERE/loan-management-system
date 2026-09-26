package com.loanapp.repository;

import com.loanapp.model.Loan;
import com.loanapp.model.Signature;
import com.loanapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignatureRepository extends JpaRepository<Signature, Long> {

    List<Signature> findByLoan(Loan loan);

    List<Signature> findByUser(User user);

    List<Signature> findByLoanAndSignatureType(Loan loan, Signature.SignatureType type);
}
