package com.loanapp.controller;

import com.loanapp.dto.ApiResponse;
import com.loanapp.model.User;
import com.loanapp.repository.UserRepository;
import com.loanapp.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/applicants")
public class ApplicantAdminController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private NotificationService notifications;

    @Value("${app.frontend.url:}")
    private String frontendUrl;

    public static Map<String, Object> toMap(User u, boolean full) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", u.getId());
        m.put("fullName", u.getFullName());
        m.put("email", u.getEmail());
        m.put("phone", u.getPhone());
        m.put("role", u.getRole());
        m.put("status", u.getStatus());
        m.put("active", u.getActive());
        m.put("createdAt", u.getCreatedAt());
        m.put("idType", u.getIdType());
        m.put("idNumber", u.getNidaNumber());
        if (full) {
            m.put("dateOfBirth", u.getDateOfBirth());
            m.put("gender", u.getGender());
            m.put("maritalStatus", u.getMaritalStatus());
            m.put("nationality", u.getNationality());
            m.put("address", u.getAddress());
            m.put("city", u.getCity());
            m.put("country", u.getCountry());
            m.put("employmentStatus", u.getEmploymentStatus());
            m.put("occupation", u.getOccupation());
            m.put("employer", u.getEmployer());
            m.put("monthlyIncome", u.getMonthlyIncome());
            m.put("kinName", u.getKinName());
            m.put("kinPhone", u.getKinPhone());
            m.put("kinRelationship", u.getKinRelationship());
            m.put("rejectionReason", u.getRejectionReason());
            m.put("photo", u.getPhotoData());
        }
        return m;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> list(@RequestParam(required = false) String status) {
        List<Map<String, Object>> out = repo.findApplicantSummaries(User.Role.ADMIN).stream()
                .map(row -> {
                    Map<String,Object> m = new LinkedHashMap<>();
                    m.put("id", row[0]); m.put("fullName", row[1]); m.put("email", row[2]);
                    m.put("phone", row[3]); m.put("role", row[4]); m.put("status", row[5]);
                    m.put("active", row[6]); m.put("createdAt", row[7]); m.put("idType", row[8]); m.put("idNumber", row[9]);
                    return m;
                })
                .filter(m -> status == null || status.isBlank() || String.valueOf(m.get("status")).equalsIgnoreCase(status))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("OK", out));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> detail(@PathVariable Long id) {
        User u = repo.findById(id).orElseThrow(() -> new RuntimeException("Mtumiaji hajapatikana"));
        return ResponseEntity.ok(ApiResponse.success("OK", toMap(u, true)));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<Map<String, Object>>> approve(@PathVariable Long id) {
        User u = repo.findById(id).orElseThrow(() -> new RuntimeException("Mtumiaji hajapatikana"));
        if (u.getRole() == User.Role.ADMIN) throw new RuntimeException("Haiwezekani");
        if (Boolean.TRUE.equals(u.getActive())) throw new RuntimeException("Mtumiaji tayari ana akaunti hai");

        String token = UUID.randomUUID().toString();
        u.setStatus(User.UserStatus.APPROVED);
        u.setRejectionReason(null);
        u.setVerificationToken(token);
        u.setTokenExpiry(LocalDateTime.now().plusDays(2));
        repo.save(u);

        String link = frontendUrl + "/set-password?token=" + token;
        boolean emailSent = false;
        try {
            notifications.sendPasswordSetupEmail(u, link, 48);
            emailSent = true;
        } catch (Exception ignored) {
        }

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("setPasswordLink", link);
        data.put("expiresInHours", 48);
        data.put("emailSent", emailSent);
        return ResponseEntity.ok(ApiResponse.success(
                emailSent ? "Amekubaliwa. Link ya kuweka password imetumwa kwenye email." : "Amekubaliwa. Email haikutumwa; tumia link ya kuweka password.",
                data));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<String>> reject(@PathVariable Long id, @RequestBody(required = false) Map<String, String> body) {
        User u = repo.findById(id).orElseThrow(() -> new RuntimeException("Mtumiaji hajapatikana"));
        if (u.getRole() == User.Role.ADMIN) throw new RuntimeException("Haiwezekani");
        u.setStatus(User.UserStatus.REJECTED);
        u.setActive(false);
        u.setVerificationToken(null);
        u.setTokenExpiry(null);
        u.setRejectionReason(body == null ? null : body.get("reason"));
        repo.save(u);
        return ResponseEntity.ok(ApiResponse.success("Ameakataliwa", "OK"));
    }
}
