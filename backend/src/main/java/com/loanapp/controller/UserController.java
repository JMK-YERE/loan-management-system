package com.loanapp.controller;

import com.loanapp.dto.ApiResponse;
import com.loanapp.model.User;
import com.loanapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired private UserRepository userRepository;

    @GetMapping("/borrowers")
    @PreAuthorize("hasAnyRole('LENDER','ADMIN')")
    public ResponseEntity<ApiResponse<List<Map<String,Object>>>> borrowers() {
        List<Map<String,Object>> data = userRepository.findByRole(User.Role.BORROWER).stream()
                .filter(u -> Boolean.TRUE.equals(u.getActive()))
                .map(u -> Map.<String,Object>of("id",u.getId(),"fullName",u.getFullName(),"email",u.getEmail(),"phone",u.getPhone(),"city",u.getCity()))
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Wakopaji waliopo", data));
    }
}
