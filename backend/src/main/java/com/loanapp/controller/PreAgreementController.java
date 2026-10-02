package com.loanapp.controller;

import com.loanapp.dto.LoanQuoteRequest;
import com.loanapp.service.PreAgreementPdfService;
import com.loanapp.model.User;
import com.loanapp.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pre-agreements")
public class PreAgreementController {
    private final PreAgreementPdfService service;
    private final UserRepository users;
    public PreAgreementController(PreAgreementPdfService service, UserRepository users){this.service=service;this.users=users;}

    @PostMapping(value="/pdf",produces=MediaType.APPLICATION_PDF_VALUE)
    @PreAuthorize("hasRole('BORROWER')")
    public ResponseEntity<byte[]> pdf(@Valid @RequestBody LoanQuoteRequest request,
                                      @RequestParam(defaultValue="Loan application") String purpose,
                                      Authentication auth){
        User borrower=users.findByEmail(auth.getName()).orElseThrow(()->new RuntimeException("Mkopaji hajapatikana"));
        byte[] bytes=service.generate(request,borrower,purpose);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=pre-loan-offer.pdf").body(bytes);
    }
}
