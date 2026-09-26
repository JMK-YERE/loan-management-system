package com.loanapp.controller;

import com.loanapp.dto.ApiResponse;
import com.loanapp.dto.SignatureRequest;
import com.loanapp.model.Signature;
import com.loanapp.service.SignatureService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/signatures")
public class SignatureController {

    @Autowired
    private SignatureService signatureService;

    @PostMapping
    public ResponseEntity<ApiResponse<Signature>> signLoan(
            @Valid @RequestBody SignatureRequest request,
            Authentication authentication) {
        Signature signature = signatureService.signLoan(request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Sahihi imehifadhiwa", signature));
    }

    @GetMapping("/loan/{loanId}")
    public ResponseEntity<ApiResponse<List<Signature>>> getSignaturesByLoan(@PathVariable Long loanId) {
        List<Signature> list = signatureService.getSignaturesByLoan(loanId);
        return ResponseEntity.ok(ApiResponse.success("Sahihi zote", list));
    }
}
