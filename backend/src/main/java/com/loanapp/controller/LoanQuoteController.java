package com.loanapp.controller;

import com.loanapp.dto.*;
import com.loanapp.service.LoanQuoteService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loan-quotes")
public class LoanQuoteController {
    private final LoanQuoteService service;
    public LoanQuoteController(LoanQuoteService service){this.service=service;}

    @PostMapping
    @PreAuthorize("hasRole('BORROWER')")
    public LoanQuoteResponse quote(@Valid @RequestBody LoanQuoteRequest request){
        return service.quote(request);
    }
}
