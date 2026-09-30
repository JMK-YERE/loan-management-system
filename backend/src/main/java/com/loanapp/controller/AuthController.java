package com.loanapp.controller;

import com.loanapp.dto.ApiResponse;
import com.loanapp.dto.AuthResponse;
import com.loanapp.dto.LoginRequest;
import com.loanapp.dto.RegisterRequest;
import com.loanapp.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/set-password")
    public ResponseEntity<ApiResponse<String>> setPassword(@RequestBody Map<String, String> body) {
        authService.setPassword(body.get("token"), body.get("password"));
        return ResponseEntity.ok(ApiResponse.success("Password imewekwa. Sasa unaweza kuingia.", "OK"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody Map<String, String> body) {
        // Intentionally generic: never reveal whether an email exists.
        authService.requestPasswordReset(body.get("email"));
        return ResponseEntity.ok(ApiResponse.success(
                "Kama email ipo kwenye mfumo na akaunti iko hai, utapokea link ya kubadilisha password.",
                "EMAIL_IF_ELIGIBLE"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody Map<String, String> body) {
        authService.resetPassword(body.get("token"), body.get("password"));
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
