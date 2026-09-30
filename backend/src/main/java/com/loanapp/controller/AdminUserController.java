package com.loanapp.controller;

import com.loanapp.dto.ApiResponse;
import com.loanapp.model.User;
import com.loanapp.repository.UserRepository;
import com.loanapp.service.AuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {
    private final UserRepository users;
    private final AuditService audit;

    public AdminUserController(UserRepository users, AuditService audit){this.users=users;this.audit=audit;}

    @GetMapping
    public ResponseEntity<ApiResponse<List<Map<String,Object>>>> list(){
        var out=users.findAll().stream().map(this::safe).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Users",out));
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<ApiResponse<Map<String,Object>>> changeRole(@PathVariable Long id,@RequestBody Map<String,String> body,org.springframework.security.core.Authentication auth){
        User u=users.findById(id).orElseThrow(()->new RuntimeException("Mtumiaji hajapatikana"));
        if(u.getRole()==User.Role.ADMIN) throw new RuntimeException("Admin role haiwezi kubadilishwa kupitia endpoint hii");
        User.Role role;
        try{role=User.Role.valueOf(String.valueOf(body.getOrDefault("role","")).toUpperCase(Locale.ROOT));}
        catch(Exception e){throw new RuntimeException("Role si sahihi");}
        if(role==User.Role.ADMIN) throw new RuntimeException("Haiwezekani kumpa mtumiaji ADMIN kupitia endpoint hii");
        User.Role old=u.getRole(); u.setRole(role); users.save(u);
        audit.log(auth.getName(),"ROLE_CHANGED","USER",u.getId(),old+" -> "+role);
        return ResponseEntity.ok(ApiResponse.success("Role imebadilishwa",safe(u)));
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<ApiResponse<Map<String,Object>>> setActive(@PathVariable Long id,@RequestBody Map<String,Boolean> body,org.springframework.security.core.Authentication auth){
        User u=users.findById(id).orElseThrow(()->new RuntimeException("Mtumiaji hajapatikana"));
        if(u.getRole()==User.Role.ADMIN) throw new RuntimeException("Admin account haiwezi kuzimwa kupitia endpoint hii");
        boolean active=Boolean.TRUE.equals(body.get("active"));
        u.setActive(active);
        if(!active && u.getStatus()==User.UserStatus.APPROVED) u.setStatus(User.UserStatus.SUSPENDED);
        if(active && u.getStatus()==User.UserStatus.SUSPENDED) u.setStatus(User.UserStatus.APPROVED);
        users.save(u);
        audit.log(auth.getName(),active?"USER_ACTIVATED":"USER_DEACTIVATED","USER",u.getId(),"active="+active);
        return ResponseEntity.ok(ApiResponse.success("Account status imebadilishwa",safe(u)));
    }

    private Map<String,Object> safe(User u){
        Map<String,Object> m=new LinkedHashMap<>();
        m.put("id",u.getId());m.put("fullName",u.getFullName());m.put("email",u.getEmail());
        m.put("phone",u.getPhone());m.put("role",u.getRole());m.put("status",u.getStatus());m.put("active",u.getActive());m.put("createdAt",u.getCreatedAt());
        return m;
    }
}
