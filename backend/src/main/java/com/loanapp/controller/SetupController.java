package com.loanapp.controller;

import com.loanapp.dto.ApiResponse;
import com.loanapp.model.User;
import com.loanapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/setup")
public class SetupController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Unda admin mpya
    @PostMapping("/create-admin")
    public ResponseEntity<ApiResponse<String>> createAdmin(@RequestBody Map<String, String> body) {
        String email = body.getOrDefault("email", "joseph@jmkloanapp.co.tz");
        String password = body.getOrDefault("password", "Joseph@2026");

        userRepository.findByEmail(email).ifPresent(userRepository::delete);

        User admin = User.builder()
                .fullName("Joseph")
                .email(email)
                .phone("+255700000000")
                .nidaNumber("00000000000000000000")
                .password(passwordEncoder.encode(password))
                .role(User.Role.ADMIN)
                .status(User.UserStatus.APPROVED)
                .active(true)
                .build();

        userRepository.save(admin);

        return ResponseEntity.ok(ApiResponse.success(
            "Admin ameundwa: " + email + " / " + password, "OK"));
    }

    // Badilisha password
    @PostMapping("/set-password")
    public ResponseEntity<ApiResponse<String>> setPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String newPassword = body.get("password");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Mtumiaji hajapatikana"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setActive(true);
        user.setStatus(User.UserStatus.APPROVED);
        userRepository.save(user);

        return ResponseEntity.ok(ApiResponse.success(
            "Password imewekwa kwa " + email, "OK"));
    }
}
