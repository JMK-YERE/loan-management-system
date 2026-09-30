package com.loanapp.controller;

import com.loanapp.service.ApplicationAgreementPdfService;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agreements/application")
public class ApplicationAgreementController {
 private final ApplicationAgreementPdfService service;
 public ApplicationAgreementController(ApplicationAgreementPdfService service){this.service=service;}
 @GetMapping("/{id}/pdf") public ResponseEntity<byte[]> pdf(@PathVariable Long id,Authentication auth){return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=application-"+id+"-offer.pdf").body(service.generate(id,auth.getName()));}
 @GetMapping("/{id}/preview") public ResponseEntity<byte[]> preview(@PathVariable Long id,Authentication auth){return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=application-"+id+"-offer.pdf").body(service.generate(id,auth.getName()));}
}
