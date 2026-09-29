package com.loanapp.repository;
import com.loanapp.model.AuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AuditEventRepository extends JpaRepository<AuditEvent,Long>{List<AuditEvent> findTop200ByOrderByCreatedAtDesc();}
