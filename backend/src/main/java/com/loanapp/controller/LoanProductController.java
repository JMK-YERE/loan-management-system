package com.loanapp.controller;
import com.loanapp.dto.*;import com.loanapp.model.LoanProduct;import com.loanapp.service.LoanProductService;import jakarta.validation.Valid;import org.springframework.security.access.prepost.PreAuthorize;import org.springframework.web.bind.annotation.*;import java.util.List;
@RestController @RequestMapping("/api/loan-products")
public class LoanProductController{
 private final LoanProductService service;
 public LoanProductController(LoanProductService service){this.service=service;}
 @GetMapping public List<LoanProduct> active(){return service.active();}
 @GetMapping("/all") @PreAuthorize("hasRole('ADMIN')") public List<LoanProduct> all(){return service.all();}
 @PostMapping @PreAuthorize("hasRole('ADMIN')") public LoanProduct create(@Valid @RequestBody LoanProductRequest r){return service.create(r);}
 @PatchMapping("/{id}/active") @PreAuthorize("hasRole('ADMIN')") public LoanProduct active(@PathVariable Long id,@RequestParam boolean value){return service.setActive(id,value);}
}