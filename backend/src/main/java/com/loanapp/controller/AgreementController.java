package com.loanapp.controller;

import com.loanapp.service.PdfAgreementService;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agreements")
public class AgreementController {
    private final PdfAgreementService service;

    public AgreementController(PdfAgreementService service) {
        this.service = service;
    }

    @GetMapping(value = "/{loanId}.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> pdf(@PathVariable Long loanId, Authentication auth) {
        byte[] bytes = service.generate(loanId, auth.getName());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=loan-" + loanId + "-agreement.pdf"
                )
                .body(bytes);
    }
}
