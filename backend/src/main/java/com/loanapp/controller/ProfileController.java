package com.loanapp.controller;

import com.loanapp.dto.ApiResponse;
import com.loanapp.model.User;
import com.loanapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/me")
public class ProfileController {

    @Autowired private UserRepository repo;

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> me(Authentication auth) {
        User u = repo.findByEmail(auth.getName()).orElseThrow(() -> new RuntimeException("Mtumiaji hajapatikana"));
        return ResponseEntity.ok(ApiResponse.success("OK", ApplicantAdminController.toMap(u, true)));
    }
}
