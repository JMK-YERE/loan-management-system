package com.loanapp.controller;

import com.loanapp.dto.LoanQuoteRequest;
import com.loanapp.service.PreAgreementPdfService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pre-agreements")
public class PreAgreementController {
    private final PreAgreementPdfService service;
    public PreAgreementController(PreAgreementPdfService service){this.service=service;}

    @PostMapping(value="/pdf",produces=MediaType.APPLICATION_PDF_VALUE)
    @PreAuthorize("hasRole('BORROWER')")
    public ResponseEntity<byte[]> pdf(@Valid @RequestBody LoanQuoteRequest request,
                                      @RequestParam(defaultValue="Loan application") String purpose,
                                      Authentication auth){
        byte[] bytes=service.generate(request,auth.getName(),purpose);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=pre-loan-offer.pdf").body(bytes);
    }
}
