package com.loanapp.controller;

import com.loanapp.dto.ApiResponse; import com.loanapp.model.ComplianceSetting; import com.loanapp.repository.ComplianceSettingRepository;
import org.springframework.http.ResponseEntity; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/admin/compliance")
public class ComplianceController{
 private final ComplianceSettingRepository repo; public ComplianceController(ComplianceSettingRepository repo){this.repo=repo;}
 @GetMapping @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<ApiResponse<ComplianceSetting>> get(){return ResponseEntity.ok(ApiResponse.success("Compliance settings",repo.findById(1L).orElseGet(()->repo.save(ComplianceSetting.builder().id(1L).currency("TZS").build()))));}
 @PutMapping @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<ApiResponse<ComplianceSetting>> update(@RequestBody ComplianceSetting value){value.setId(1L);return ResponseEntity.ok(ApiResponse.success("Compliance settings zimehifadhiwa",repo.save(value)));}
}
