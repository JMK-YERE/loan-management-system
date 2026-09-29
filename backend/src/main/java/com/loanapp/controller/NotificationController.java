package com.loanapp.controller;
import com.loanapp.dto.ApiResponse; import com.loanapp.service.NotificationService; import org.springframework.http.ResponseEntity; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequestMapping("/api/notifications")
public class NotificationController{private final NotificationService service;public NotificationController(NotificationService service){this.service=service;}
 @PostMapping("/test-sms") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<ApiResponse<String>> sms(@RequestBody Map<String,String> body){boolean ok=service.sendSms(body.get("to"),body.get("message"));return ResponseEntity.ok(ApiResponse.success(ok?"SMS imetumwa":"SMS provider haija-configure au imeshindwa",ok?"SENT":"NOT_SENT"));}
}
