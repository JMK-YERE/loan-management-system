package com.loanapp.repository;

import com.loanapp.model.Loan;
import com.loanapp.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @org.springframework.data.jpa.repository.Query("select coalesce(sum(p.amount),0) from Payment p where p.status = ?1")
    java.math.BigDecimal sumAmountByStatus(Payment.PaymentStatus status);

    List<Payment> findByLoan(Loan loan);

    List<Payment> findByLoanAndStatus(Loan loan, Payment.PaymentStatus status);

    Optional<Payment> findByTransactionId(String transactionId);

    Optional<Payment> findFirstByTransactionId(String transactionId);
}
