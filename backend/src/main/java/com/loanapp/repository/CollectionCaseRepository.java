package com.loanapp.repository;
import com.loanapp.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface CollectionCaseRepository extends JpaRepository<CollectionCase,Long>{List<CollectionCase> findByStatusOrderByCreatedAtDesc(CollectionCase.Status status);Optional<CollectionCase> findFirstByLoanOrderByCreatedAtDesc(Loan loan);}
