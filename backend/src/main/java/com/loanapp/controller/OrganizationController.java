package com.loanapp.controller;
import com.loanapp.model.*; import com.loanapp.repository.*; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/organizations") @PreAuthorize("hasRole('ADMIN')")
public class OrganizationController {
 private final OrganizationRepository organizations; private final BranchRepository branches;
 public OrganizationController(OrganizationRepository o,BranchRepository b){organizations=o;branches=b;}
 @GetMapping public List<Organization> all(){return organizations.findAll();}
 @PostMapping public Organization create(@RequestBody Map<String,String> r){String code=r.getOrDefault("code","ORG-"+System.currentTimeMillis());if(organizations.findByCode(code).isPresent())throw new RuntimeException("Organization code already exists");return organizations.save(new Organization(r.get("name"),code,r.get("licenseNumber")));}
 @GetMapping("/{id}/branches") public List<Branch> branches(@PathVariable Long id){return branches.findByOrganizationOrderByNameAsc(organizations.findById(id).orElseThrow());}
 @PostMapping("/{id}/branches") public Branch createBranch(@PathVariable Long id,@RequestBody Map<String,String> r){Organization o=organizations.findById(id).orElseThrow();return branches.save(new Branch(o,r.get("name"),r.get("code"),r.get("city"),r.get("address")));}
}
