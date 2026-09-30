package com.loanapp.controller;
import com.loanapp.service.CollectionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/collections")
@PreAuthorize("hasAnyRole('ADMIN','LENDER')")
public class CollectionController {
 private final CollectionService service;
 public CollectionController(CollectionService s){service=s;}
 @GetMapping public List<?> open(){return service.open();}
 @PostMapping("/loan/{loanId}") public Object create(@PathVariable Long loanId,@RequestParam(required=false) String assignee,@RequestParam(required=false) String notes,Authentication a){return service.create(loanId,assignee,notes,a.getName());}
 @PostMapping("/{id}/actions") public Object action(@PathVariable Long id,@RequestParam String type,@RequestParam(required=false) String notes,Authentication a){return service.action(id,type,notes,a.getName());}
 @GetMapping("/{id}/history") public List<?> history(@PathVariable Long id){return service.history(id);}
}
