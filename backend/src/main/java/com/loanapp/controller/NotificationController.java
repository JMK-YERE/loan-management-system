package com.loanapp.controller;
import com.loanapp.dto.ApiResponse; import com.loanapp.service.NotificationService; import com.loanapp.repository.LoanRepository; import com.loanapp.model.Loan; import com.loanapp.model.User; import com.loanapp.repository.UserRepository; import org.springframework.http.ResponseEntity; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequestMapping("/api/notifications")
public class NotificationController{private final NotificationService service; private final LoanRepository loans; private final UserRepository users; public NotificationController(NotificationService service,LoanRepository loans,UserRepository users){this.service=service;this.loans=loans;this.users=users;}
 @PostMapping("/loan/{loanId}/remind") @PreAuthorize("hasAnyRole('LENDER','BURSER','ADMIN')")
 public ResponseEntity<ApiResponse<String>> remind(@PathVariable Long loanId,org.springframework.security.core.Authentication auth){
  Loan loan=loans.findById(loanId).orElseThrow(()->new RuntimeException("Mkopo haujapatikana")); User actor=users.findByEmail(auth.getName()).orElseThrow(()->new RuntimeException("Mtumiaji hajapatikana"));
  if(actor.getRole()!=User.Role.ADMIN && actor.getRole()!=User.Role.BURSER && (loan.getLender()==null || !loan.getLender().getId().equals(actor.getId()))) throw new RuntimeException("Huna ruhusa ya loan hii");
  String message="JmkLoanApp: Kumbusho la Loan #"+loan.getId()+". Salio la jumla ni TZS "+loan.getTotalRepayment()+". Tafadhali angalia ratiba na fanya malipo kwa wakati.";
  boolean sms=service.sendSms(loan.getBorrower().getPhone(),message); boolean wa=service.sendWhatsApp(loan.getBorrower().getPhone(),message);
  boolean email=service.sendEmail(loan.getBorrower().getEmail(),"JmkLoanApp - Kumbusho la Loan #"+loan.getId(),"<p>Habari "+loan.getBorrower().getFullName()+",</p><p>"+message+"</p>");
  return ResponseEntity.ok(ApiResponse.success("Reminder imetumwa", "SMS="+sms+", WhatsApp="+wa+", EMAIL="+email));
 }
 @PostMapping("/test-sms") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<ApiResponse<String>> sms(@RequestBody Map<String,String> body){boolean ok=service.sendSms(body.get("to"),body.get("message"));return ResponseEntity.ok(ApiResponse.success(ok?"SMS imetumwa":"SMS provider haija-configure au imeshindwa",ok?"SENT":"NOT_SENT"));}
}
