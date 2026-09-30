package com.loanapp.controller;
import com.loanapp.dto.*;import com.loanapp.model.LoanApplication;import com.loanapp.service.LoanApplicationService;
import jakarta.validation.Valid;import org.springframework.http.ResponseEntity;import org.springframework.security.access.prepost.PreAuthorize;import org.springframework.security.core.Authentication;import org.springframework.web.bind.annotation.*;import java.util.*;
@RestController @RequestMapping("/api/loan-applications")
public class LoanApplicationController{
 private final LoanApplicationService s; public LoanApplicationController(LoanApplicationService s){this.s=s;}
 @PostMapping @PreAuthorize("hasRole('BORROWER')") public ResponseEntity<ApiResponse<LoanApplication>> submit(@Valid @RequestBody LoanApplicationRequest r,Authentication a){return ResponseEntity.ok(ApiResponse.success("Ombi la mkopo limetumwa",s.submit(r,a.getName())));}
 @GetMapping("/mine") @PreAuthorize("hasRole('BORROWER')") public ResponseEntity<ApiResponse<List<LoanApplication>>> mine(Authentication a){return ResponseEntity.ok(ApiResponse.success("Maombi yako",s.mine(a.getName())));}
 @GetMapping("/pending") @PreAuthorize("hasAnyRole('LENDER','ADMIN')") public ResponseEntity<ApiResponse<List<LoanApplication>>> pending(){return ResponseEntity.ok(ApiResponse.success("Maombi yanayosubiri",s.pending()));}
 @PutMapping("/{id}/review") @PreAuthorize("hasAnyRole('LENDER','ADMIN')") public ResponseEntity<ApiResponse<LoanApplication>> review(@PathVariable Long id,Authentication a){return ResponseEntity.ok(ApiResponse.success("Ombi limeanza kukaguliwa",s.startReview(id,a.getName())));}
 @PutMapping("/{id}/approve") @PreAuthorize("hasAnyRole('LENDER','ADMIN')") public ResponseEntity<ApiResponse<LoanApplication>> approve(@PathVariable Long id,Authentication a){return ResponseEntity.ok(ApiResponse.success("Ombi limeidhinishwa na kuwa mkopo",s.approve(id,a.getName())));}
 @PutMapping("/{id}/reject") @PreAuthorize("hasAnyRole('LENDER','ADMIN') public ResponseEntity<ApiResponse<LoanApplication>> reject(@PathVariable Long id,@RequestBody(required=false) Map<String,String> b,Authentication a){return ResponseEntity.ok(ApiResponse.success("Ombi limekataliwa",s.reject(id,a.getName(),b==null?null:b.get("reason"))));}
}
