package com.loanapp.repository;
import com.loanapp.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PaymentAllocationRepository extends JpaRepository<PaymentAllocation,Long>{
 List<PaymentAllocation> findByPayment(Payment payment);
}
