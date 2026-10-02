package com.loanapp.controller;

import com.loanapp.dto.ApiResponse;
import com.loanapp.dto.SignatureRequest;
import com.loanapp.model.Signature;
import com.loanapp.service.SignatureService;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/signatures")
public class SignatureController {
    private final SignatureService signatureService;
    public SignatureController(SignatureService signatureService){this.signatureService=signatureService;}

    @PostMapping @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Signature>> sign(@Valid @RequestBody SignatureRequest request,Authentication auth,HttpServletRequest http){
        String forwarded=http.getHeader("X-Forwarded-For");
        String ip=(forwarded!=null&&!forwarded.isBlank())?forwarded.split(",")[0].trim():http.getRemoteAddr();
        return ResponseEntity.ok(ApiResponse.success("Mkataba umesainiwa kwa uthibitisho wa kidigitali",signatureService.signLoan(request,auth.getName(),ip)));
    }

    @GetMapping("/loan/{loanId}") @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<Signature>>> getByLoan(@PathVariable Long loanId,Authentication auth){
        return ResponseEntity.ok(ApiResponse.success("Sahihi za mkopo",signatureService.getSignaturesByLoan(loanId,auth.getName())));
    }

    @GetMapping("/loan/{loanId}/status") @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Map<String,Object>>> status(@PathVariable Long loanId,Authentication auth){
        return ResponseEntity.ok(ApiResponse.success("Hali ya sahihi za mkataba",signatureService.getStatus(loanId,auth.getName())));
    }
}