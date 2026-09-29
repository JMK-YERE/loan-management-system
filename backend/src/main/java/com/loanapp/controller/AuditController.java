package com.loanapp.controller;
import com.loanapp.dto.ApiResponse; import com.loanapp.model.AuditEvent; import com.loanapp.service.AuditService;
import org.springframework.http.ResponseEntity; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/admin/audit")
public class AuditController{private final AuditService service; public AuditController(AuditService service){this.service=service;}
 @GetMapping @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<ApiResponse<List<AuditEvent>>> recent(){return ResponseEntity.ok(ApiResponse.success("Audit trail",service.recent()));}}
