package com.loanapp.controller;

import com.loanapp.dto.ApiResponse; import com.loanapp.service.KycService;
import org.springframework.http.ResponseEntity; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequestMapping("/api/admin/kyc")
public class KycController{private final KycService service;public KycController(KycService service){this.service=service;}
 @PostMapping("/verify/{userId}") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<ApiResponse<Map<String,Object>>> verify(@PathVariable Long userId){return ResponseEntity.ok(ApiResponse.success("KYC verification",service.verify(userId)));}}
