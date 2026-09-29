package com.loanapp.service;
import com.loanapp.model.AuditEvent;
import com.loanapp.model.User;
import com.loanapp.repository.AuditEventRepository;
import com.loanapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class AuditService{
 private final AuditEventRepository repo; private final UserRepository users;
 public AuditService(AuditEventRepository repo,UserRepository users){this.repo=repo;this.users=users;}
 public void log(String email,String action,String entityType,Long entityId,String details){
  User u=email==null?null:users.findByEmail(email).orElse(null);
  repo.save(AuditEvent.builder().actorEmail(email).actorRole(u==null?null:u.getRole().name()).action(action).entityType(entityType).entityId(entityId).details(details).build());
 }
 public List<AuditEvent> recent(){return repo.findTop200ByOrderByCreatedAtDesc();}
}
