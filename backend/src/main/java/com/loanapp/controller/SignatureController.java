package com.loanapp.controller;

import com.loanapp.dto.ApiResponse;
import com.loanapp.dto.SignatureRequest;
import com.loanapp.model.Signature;
import com.loanapp.service.SignatureService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/signatures")
public class SignatureController {
    @Autowired private SignatureService signatureService;
    @PostMapping @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Signature>> sign(@Valid @RequestBody SignatureRequest request,Authentication auth){return ResponseEntity.ok(ApiResponse.success("Mkataba umesainiwa",signatureService.signLoan(request,auth.getName())));}
    @GetMapping("/loan/{loanId}") @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<Signature>>> getByLoan(@PathVariable Long loanId,Authentication auth){return ResponseEntity.ok(ApiResponse.success("Sahihi za mkopo",signatureService.getSignaturesByLoan(loanId,auth.getName())));}
}
