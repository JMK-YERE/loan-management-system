package com.loanapp.controller;

import com.loanapp.dto.ApiResponse;
import com.loanapp.dto.LoanApplicationRequest;
import com.loanapp.model.LoanApplication;
import com.loanapp.service.GeneralLoanApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/general-loan-applications")
public class GeneralLoanApplicationController {
 private final GeneralLoanApplicationService service;
 public GeneralLoanApplicationController(GeneralLoanApplicationService service){this.service=service;}
 @PostMapping @PreAuthorize("hasRole('BORROWER')")
 public ResponseEntity<ApiResponse<LoanApplication>> submit(@Valid @RequestBody LoanApplicationRequest request,Authentication auth){return ResponseEntity.ok(ApiResponse.success("Ombi la mkopo limetumwa bila kuchagua product",service.submit(request,auth.getName())));}
 @GetMapping("/mine") @PreAuthorize("hasRole('BORROWER')")
 public ResponseEntity<ApiResponse<List<LoanApplication>>> mine(Authentication auth){return ResponseEntity.ok(ApiResponse.success("Maombi yako",service.mine(auth.getName())));}
 @GetMapping("/pending") @PreAuthorize("hasAnyRole('LENDER','ADMIN')")
 public ResponseEntity<ApiResponse<List<LoanApplication>>> pending(){return ResponseEntity.ok(ApiResponse.success("General loan requests",service.pending()));}
 @PutMapping("/{id}/collateral-photo")
 @PreAuthorize("hasAnyRole('LENDER','ADMIN')")
 public ResponseEntity<ApiResponse<LoanApplication>> collateralPhoto(@PathVariable Long id,@RequestBody Map<String,String> body,Authentication auth){
  return ResponseEntity.ok(ApiResponse.success("Picha ya dhamana imehifadhiwa",service.captureCollateralPhoto(id,body.get("photoData"),auth.getName())));
 }
 @PutMapping("/{id}/review") @PreAuthorize("hasAnyRole('LENDER','ADMIN')")
 public ResponseEntity<ApiResponse<LoanApplication>> review(@PathVariable Long id,Authentication auth){return ResponseEntity.ok(ApiResponse.success("Ombi liko review",service.startReview(id,auth.getName())));}
 @PutMapping("/{id}/assign-product/{productId}") @PreAuthorize("hasAnyRole('LENDER','ADMIN')")
 public ResponseEntity<ApiResponse<LoanApplication>> assign(@PathVariable Long id,@PathVariable Long productId,Authentication auth){return ResponseEntity.ok(ApiResponse.success("Offer imeandaliwa",service.assignProduct(id,productId,auth.getName())));}
 @PutMapping("/{id}/accept-offer") @PreAuthorize("hasRole('BORROWER')")
 public ResponseEntity<ApiResponse<LoanApplication>> accept(@PathVariable Long id,Authentication auth){return ResponseEntity.ok(ApiResponse.success("Offer imekubaliwa",service.acceptOffer(id,auth.getName())));}
 @PutMapping("/{id}/approve") @PreAuthorize("hasAnyRole('LENDER','ADMIN')")
 public ResponseEntity<ApiResponse<LoanApplication>> approve(@PathVariable Long id,Authentication auth){return ResponseEntity.ok(ApiResponse.success("Mkopo umeundwa",service.approve(id,auth.getName())));}
}
