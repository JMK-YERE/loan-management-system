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

    @Autowired
    private LoanService loanService;

    @PostMapping
    @PreAuthorize("hasAnyRole('LENDER','ADMIN')")
    public ResponseEntity<ApiResponse<Loan>> createLoan(
            @Valid @RequestBody LoanRequest request,
            @RequestParam Long borrowerId,
            Authentication authentication) {
        String lenderEmail = authentication.getName();
        Loan loan = loanService.createLoan(request, lenderEmail, borrowerId);
        return ResponseEntity.ok(ApiResponse.success("Mkopo umetengenezwa", loan));
    }

    @GetMapping("/lender")
    @PreAuthorize("hasAnyRole('LENDER','ADMIN')")
    public ResponseEntity<ApiResponse<List<Loan>>> getLoansByLender(Authentication authentication) {
        List<Loan> loans = loanService.getLoansByLender(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Mikopo yote", loans));
    }

    @GetMapping("/borrower")
    @PreAuthorize("hasRole('BORROWER')")
    public ResponseEntity<ApiResponse<List<Loan>>> getLoansByBorrower(Authentication authentication) {
        List<Loan> loans = loanService.getLoansByBorrower(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Mikopo yako", loans));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Loan>> getLoanById(@PathVariable Long id) {
        Loan loan = loanService.getLoanById(id);
        return ResponseEntity.ok(ApiResponse.success("Mkopo umepatikana", loan));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('LENDER','ADMIN')")
    public ResponseEntity<ApiResponse<Loan>> approveLoan(@PathVariable Long id) {
        Loan loan = loanService.approveLoan(id);
        return ResponseEntity.ok(ApiResponse.success("Mkopo umeidhinishwa", loan));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('LENDER','ADMIN')")
    public ResponseEntity<ApiResponse<Loan>> rejectLoan(@PathVariable Long id) {
        Loan loan = loanService.rejectLoan(id);
        return ResponseEntity.ok(ApiResponse.success("Mkopo umekataliwa", loan));
    }
}
