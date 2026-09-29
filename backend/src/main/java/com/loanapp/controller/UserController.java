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
                .map(u -> { Map<String,Object> m = new java.util.LinkedHashMap<>(); m.put("id",u.getId()); m.put("fullName",u.getFullName()); m.put("email",u.getEmail()); m.put("phone",u.getPhone()); m.put("city",u.getCity()); return m; })
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Wakopaji waliopo", data));
    }
}
