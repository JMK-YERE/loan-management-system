package com.loanapp.repository;
import com.loanapp.model.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface BranchRepository extends JpaRepository<Branch,Long>{List<Branch> findByOrganizationOrderByNameAsc(Organization organization);}
