package com.loanapp.controller;

import com.loanapp.dto.AnnouncementRequest;
import com.loanapp.dto.ApiResponse;
import com.loanapp.model.Announcement;
import com.loanapp.service.AnnouncementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    // PUBLIC - wote wanaweza kuona
    @GetMapping("/public")
    public ResponseEntity<ApiResponse<List<Announcement>>> getPublic() {
        List<Announcement> list = announcementService.getPublicAnnouncements();
        return ResponseEntity.ok(ApiResponse.success("Matangazo", list));
    }

    // ADMIN - kuona yote
    @GetMapping
    public ResponseEntity<ApiResponse<List<Announcement>>> getAll() {
        List<Announcement> list = announcementService.getAllAnnouncements();
        return ResponseEntity.ok(ApiResponse.success("Matangazo yote", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Announcement>> getById(@PathVariable Long id) {
        Announcement a = announcementService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Tangazo", a));
    }

    // ADMIN - kuunda
    @PostMapping
    public ResponseEntity<ApiResponse<Announcement>> create(
            @Valid @RequestBody AnnouncementRequest request,
            Authentication authentication) {
        Announcement a = announcementService.create(request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Tangazo limeundwa", a));
    }

    // ADMIN - kubadilisha
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Announcement>> update(
            @PathVariable Long id,
            @Valid @RequestBody AnnouncementRequest request) {
        Announcement a = announcementService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Tangazo limebadilishwa", a));
    }

    // ADMIN - kufuta
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        announcementService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Tangazo limefutwa", "OK"));
    }
}
