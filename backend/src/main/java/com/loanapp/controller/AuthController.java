package com.loanapp.controller;

import com.loanapp.dto.ApiResponse;
import com.loanapp.dto.AuthResponse;
import com.loanapp.dto.LoginRequest;
import com.loanapp.dto.RegisterRequest;
import com.loanapp.dto.PasswordActionRequest;
import com.loanapp.dto.ForgotPasswordRequest;
import com.loanapp.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(ApiResponse.success(authService.register(request), "OK"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Kuingia kumefanikiwa", authService.login(request)));
    }

    @PostMapping("/google")
    public ResponseEntity<ApiResponse<AuthResponse>> google(@RequestBody Map<String,String> body) {
        String credential=body==null?null:body.get("credential");
        if(credential==null || credential.isBlank()) throw new RuntimeException("Google credential inahitajika");
        return ResponseEntity.ok(ApiResponse.success("Google sign-in imefanikiwa", authService.loginWithGoogle(credential)));
    }

    @PostMapping("/set-password")
    public ResponseEntity<ApiResponse<String>> setPassword(@Valid @RequestBody PasswordActionRequest body) {
        authService.setPassword(body.getToken(), body.getPassword());
        return ResponseEntity.ok(ApiResponse.success("Password imewekwa. Sasa unaweza kuingia.", "OK"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest body) {
        // Intentionally generic: never reveal whether an email exists.
        authService.requestPasswordReset(body.getEmail());
        return ResponseEntity.ok(ApiResponse.success(
                "Kama email ipo kwenye mfumo na akaunti iko hai, utapokea link ya kubadilisha password.",
                "EMAIL_IF_ELIGIBLE"));
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @RequestBody Map<String, String> body, Authentication authentication) {
        authService.changePassword(authentication.getName(),
                body == null ? null : body.get("currentPassword"),
                body == null ? null : body.get("newPassword"));
        return ResponseEntity.ok(ApiResponse.success("Password imebadilishwa.", "OK"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody PasswordActionRequest body) {
        authService.resetPassword(body.getToken(), body.getPassword());
        return ResponseEntity.ok(ApiResponse.success("Password imebadilishwa. Sasa unaweza kuingia.", "OK"));
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<String>> root() {
        return ResponseEntity.ok(ApiResponse.success("JmkLoanApp API ipo hai", "Karibu JmkLoanApp Backend"));
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("API ipo hai", "OK"));
    }
}
