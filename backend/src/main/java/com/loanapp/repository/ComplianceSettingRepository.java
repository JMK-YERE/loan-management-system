package com.loanapp.repository;
import com.loanapp.model.ComplianceSetting; import org.springframework.data.jpa.repository.JpaRepository;
public interface ComplianceSettingRepository extends JpaRepository<ComplianceSetting,Long>{}
