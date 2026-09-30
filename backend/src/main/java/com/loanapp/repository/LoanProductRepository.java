package com.loanapp.repository;
import com.loanapp.model.LoanProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface LoanProductRepository extends JpaRepository<LoanProduct,Long>{
 Optional<LoanProduct> findByNameIgnoreCase(String name);
 List<LoanProduct> findByActiveTrueOrderByNameAsc();
}