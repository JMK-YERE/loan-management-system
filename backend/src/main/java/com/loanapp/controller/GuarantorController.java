package com.loanapp.controller;

import com.loanapp.dto.ApiResponse;
import com.loanapp.dto.GuarantorRequest;
import com.loanapp.dto.OnsiteGuarantorRequest;
import com.loanapp.model.Guarantor;
import com.loanapp.service.GuarantorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/guarantors")
public class GuarantorController {

    @Autowired
    private GuarantorService guarantorService;

    @PostMapping("/loan/{loanId}")
    @PreAuthorize("hasAnyRole('LENDER','ADMIN')")
    public ResponseEntity<ApiResponse<Guarantor>> addGuarantor(
            @PathVariable Long loanId,
            @Valid @RequestBody GuarantorRequest request, Authentication authentication) {
        Guarantor g = guarantorService.addGuarantor(loanId, request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Mdhamini ameongezwa", g));
    }

    @PostMapping("/loan/{loanId}/onsite")
    @PreAuthorize("hasAnyRole('LENDER','ADMIN')")
    public ResponseEntity<ApiResponse<Guarantor>> addOnsite(
            @PathVariable Long loanId,
            @Valid @RequestBody OnsiteGuarantorRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.success("Mdhamini wa onsite amerekodiwa na kusaini",
                guarantorService.addOnsiteGuarantor(loanId, request, authentication.getName())));
    }

    @PostMapping("/loan/{loanId}/remote-invite")
    @PreAuthorize("hasAnyRole('LENDER','ADMIN')")
    public ResponseEntity<ApiResponse<Map<String,Object>>> remoteInvite(@PathVariable Long loanId,@RequestBody Map<String,Object> body,Authentication authentication){
        java.math.BigDecimal amount=new java.math.BigDecimal(String.valueOf(body.getOrDefault("guaranteedAmount","0")));
        Map<String,Object> result=guarantorService.createRemoteInvite(loanId,String.valueOf(body.getOrDefault("name","")),String.valueOf(body.getOrDefault("phone","")),String.valueOf(body.getOrDefault("idNumber","")),String.valueOf(body.getOrDefault("relationship","")),amount,authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Remote guarantor link imetengenezwa",result));
    }

    @GetMapping("/remote/{token}")
    public ResponseEntity<ApiResponse<Map<String,Object>>> remoteDetails(@PathVariable String token){
        return ResponseEntity.ok(ApiResponse.success("Taarifa za signing",guarantorService.getRemoteInvite(token)));
    }

    @PostMapping("/remote/{token}/sign")
    public ResponseEntity<ApiResponse<Map<String,Object>>> remoteSign(@PathVariable String token,@RequestBody Map<String,String> body){
        return ResponseEntity.ok(ApiResponse.success("Mdhamini amesaini",guarantorService.signRemote(token,body.get("signatureData"),body.get("deviceInfo"))));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('GUARANTOR','ADMIN')")
    public ResponseEntity<ApiResponse<Guarantor>> approveGuarantor(
            @PathVariable Long id,
            Authentication authentication) {
        Guarantor g = guarantorService.approveGuarantor(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Mdhamini ameidhinisha", g));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('GUARANTOR','ADMIN')")
    public ResponseEntity<ApiResponse<Guarantor>> rejectGuarantor(
            @PathVariable Long id,
            Authentication authentication) {
        Guarantor g = guarantorService.rejectGuarantor(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Mdhamini amekataa", g));
    }

    @GetMapping("/loan/{loanId}")
    public ResponseEntity<ApiResponse<List<Guarantor>>> getGuarantorsByLoan(@PathVariable Long loanId, Authentication authentication) {
        List<Guarantor> list = guarantorService.getGuarantorsByLoan(loanId, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Wadhamini wote", list));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('BORROWER')")
    public ResponseEntity<ApiResponse<Guarantor>> updateGuarantor(
            @PathVariable Long id, @Valid @RequestBody GuarantorRequest request, Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.success("Mdhamini amesasishwa",
                guarantorService.updateGuarantor(id, request, authentication.getName())));
    }

    @GetMapping("/mine")
    @PreAuthorize("hasRole('GUARANTOR')")
    public ResponseEntity<ApiResponse<List<Guarantor>>> getMine(Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.success("Maombi yako ya udhamini",
                guarantorService.getGuarantorsByUser(authentication.getName())));
    }
}
