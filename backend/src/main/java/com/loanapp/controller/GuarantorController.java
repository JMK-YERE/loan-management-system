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
