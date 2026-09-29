package com.loanapp.controller;

import com.loanapp.dto.ApiResponse;
import com.loanapp.dto.LoanRequest;
import com.loanapp.model.Loan;
import com.loanapp.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    @Autowired private LoanService loanService;

    @PostMapping @PreAuthorize("hasAnyRole('LENDER','ADMIN')")
    public ResponseEntity<ApiResponse<Loan>> createLoan(@Valid @RequestBody LoanRequest request,@RequestParam Long borrowerId,Authentication auth){return ResponseEntity.ok(ApiResponse.success("Mkopo umetengenezwa",loanService.createLoan(request,auth.getName(),borrowerId)));}

    @GetMapping("/lender") @PreAuthorize("hasAnyRole('LENDER','ADMIN')")
    public ResponseEntity<ApiResponse<List<Loan>>> getLoansByLender(Authentication auth){return ResponseEntity.ok(ApiResponse.success("Mikopo yote",loanService.getLoansByLender(auth.getName())));}

    @GetMapping("/borrower") @PreAuthorize("hasRole('BORROWER')")
    public ResponseEntity<ApiResponse<List<Loan>>> getLoansByBorrower(Authentication auth){return ResponseEntity.ok(ApiResponse.success("Mikopo yako",loanService.getLoansByBorrower(auth.getName())));}

    @GetMapping("/{id}") @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Loan>> getLoanById(@PathVariable Long id){return ResponseEntity.ok(ApiResponse.success("Mkopo umepatikana",loanService.getLoanById(id)));}

    @PutMapping("/{id}/approve") @PreAuthorize("hasAnyRole('LENDER','ADMIN')")
    public ResponseEntity<ApiResponse<Loan>> approveLoan(@PathVariable Long id,Authentication auth){return ResponseEntity.ok(ApiResponse.success("Mkopo umeidhinishwa",loanService.approveLoan(id,auth.getName())));}

    @PutMapping("/{id}/reject") @PreAuthorize("hasAnyRole('LENDER','ADMIN')")
    public ResponseEntity<ApiResponse<Loan>> rejectLoan(@PathVariable Long id,Authentication auth){return ResponseEntity.ok(ApiResponse.success("Mkopo umekataliwa",loanService.rejectLoan(id,auth.getName())));}
}
