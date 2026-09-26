package com.loanapp.repository;

import com.loanapp.model.Loan;
import com.loanapp.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByLoan(Loan loan);

    List<Payment> findByLoanAndStatus(Loan loan, Payment.PaymentStatus status);

    List<Payment> findByTransactionId(String transactionId);
}
