package com.loanapp.repository;
import com.loanapp.model.Organization; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface OrganizationRepository extends JpaRepository<Organization,Long>{Optional<Organization> findByCode(String code);}
