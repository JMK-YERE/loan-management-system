package com.loanapp.controller;
import com.loanapp.dto.ApiResponse;
import com.loanapp.dto.CreditAssessmentRequest;
import com.loanapp.model.CreditAssessment;
import com.loanapp.service.CreditAssessmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/credit-assessments")
public class CreditAssessmentController{
 private final CreditAssessmentService service; public CreditAssessmentController(CreditAssessmentService service){this.service=service;}
 @PostMapping("/{applicationId}") public ResponseEntity<ApiResponse<CreditAssessment>> assess(@PathVariable Long applicationId,@Valid @RequestBody CreditAssessmentRequest request,Authentication auth){return ResponseEntity.ok(ApiResponse.success("Credit assessment imekamilika",service.assess(applicationId,request,auth.getName())));}
 @GetMapping("/{applicationId}") public ResponseEntity<ApiResponse<CreditAssessment>> get(@PathVariable Long applicationId,Authentication auth){return ResponseEntity.ok(ApiResponse.success("Credit assessment",service.get(applicationId,auth.getName())));}
}