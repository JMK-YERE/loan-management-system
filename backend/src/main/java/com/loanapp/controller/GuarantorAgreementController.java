package com.loanapp.controller;

import com.loanapp.service.GuarantorAgreementPdfService;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loan-agreements")
public class GuarantorAgreementController {
 private final GuarantorAgreementPdfService service;
 public GuarantorAgreementController(GuarantorAgreementPdfService service){this.service=service;}
 @GetMapping("/{id}/pdf") public ResponseEntity<byte[]> pdf(@PathVariable Long id,Authentication auth){return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=loan-"+id+"-final-agreement.pdf").body(service.generate(id,auth.getName()));}
 @GetMapping("/{id}/preview") public ResponseEntity<byte[]> preview(@PathVariable Long id,Authentication auth){return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=loan-"+id+"-final-agreement.pdf").body(service.generate(id,auth.getName()));}
}
