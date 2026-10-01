package com.loanapp.controller;

import com.loanapp.model.Loan;
import com.loanapp.model.LoanCollateral;
import com.loanapp.repository.LoanCollateralRepository;
import com.loanapp.repository.LoanRepository;
import com.loanapp.repository.UserRepository;
import com.loanapp.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/loans/{loanId}/collaterals")
public class LoanCollateralController {
    private final LoanRepository loans;
    private final LoanCollateralRepository collaterals;
    private final UserRepository users;

    public LoanCollateralController(LoanRepository loans, LoanCollateralRepository collaterals, UserRepository users){
        this.loans=loans; this.collaterals=collaterals; this.users=users;
    }

    private Loan loan(Long id){return loans.findById(id).orElseThrow(()->new RuntimeException("Mkopo haujapatikana"));}

    private boolean canView(Loan l, String email){
        var u=users.findByEmail(email).orElseThrow(()->new RuntimeException("Mtumiaji hajapatikana"));
        return u.getRole()==com.loanapp.model.User.Role.ADMIN
            || l.getBorrower().getId().equals(u.getId())
            || l.getLender().getId().equals(u.getId());
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<LoanCollateral>>> list(@PathVariable Long loanId, Authentication auth){
        Loan l=loan(loanId);
        if(!canView(l,auth.getName())) throw new RuntimeException("Huna ruhusa kuona dhamana za mkopo huu");
        return ResponseEntity.ok(ApiResponse.success("Dhamana za mkopo",collaterals.findByLoanOrderByIdAsc(l)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('LENDER','ADMIN')")
    public ResponseEntity<ApiResponse<LoanCollateral>> add(@PathVariable Long loanId,@RequestBody Map<String,Object> body,Authentication auth){
        Loan l=loan(loanId);
        var actor=users.findByEmail(auth.getName()).orElseThrow(()->new RuntimeException("Mtumiaji hajapatikana"));
        if(actor.getRole()!=com.loanapp.model.User.Role.ADMIN && !l.getLender().getId().equals(actor.getId()))
            throw new RuntimeException("Huna ruhusa ya kuongeza dhamana kwenye mkopo huu");
        String type=String.valueOf(body.getOrDefault("type","OTHER")).trim();
        String description=String.valueOf(body.getOrDefault("description","")).trim();
        if(description.isBlank()) throw new RuntimeException("Maelezo ya dhamana yanahitajika");
        BigDecimal value=new BigDecimal(String.valueOf(body.getOrDefault("value","0")));
        Object photosObj=body.get("photos");
        List<String> photos=new ArrayList<>();
        if(photosObj instanceof List<?> list) for(Object p:list) {
            String s=String.valueOf(p);
            if(!s.startsWith("data:image/")) throw new RuntimeException("Picha ya dhamana si sahihi");
            if(s.length()>5000000) throw new RuntimeException("Picha ya dhamana ni kubwa sana");
            photos.add(s);
        }
        if(photos.size()>10) throw new RuntimeException("Dhamana moja inaweza kuwa na picha 10 kwa sasa");
        Object docsObj=body.get("documents");
        List<Map<String,String>> documents=new ArrayList<>();
        if(docsObj instanceof List<?> list) for(Object item:list) {
            if(!(item instanceof Map<?,?> raw)) continue;
            String name=String.valueOf(raw.getOrDefault("name","document"));
            String data=String.valueOf(raw.getOrDefault("data",""));
            if(data.isBlank() || !data.startsWith("data:")) throw new RuntimeException("Nyaraka ya dhamana si sahihi");
            if(data.length()>7000000) throw new RuntimeException("Nyaraka ya dhamana ni kubwa sana");
            documents.add(Map.of("name",name,"data",data));
        }
        if(documents.size()>10) throw new RuntimeException("Dhamana moja inaweza kuwa na nyaraka 10 kwa sasa");
        LoanCollateral c=new LoanCollateral();
        c.setLoan(l); c.setType(type); c.setDescription(description); c.setValue(value);
        try {
            var mapper=new com.fasterxml.jackson.databind.ObjectMapper();
            c.setPhotoDataJson(mapper.writeValueAsString(photos));
            c.setDocumentDataJson(mapper.writeValueAsString(documents));
        } catch (Exception e) { throw new RuntimeException("Evidence za dhamana hazikuandaliwa"); }
        c.setDocumentReference(documents.stream().map(x->x.get("name")).reduce((a,b)->a+", "+b).orElse(null));
        c.setCapturedBy(auth.getName()); c.setVerificationStatus("PENDING"); c.setCapturedAt(LocalDateTime.now());
        try { return ResponseEntity.ok(ApiResponse.success("Dhamana imehifadhiwa",collaterals.save(c))); }
        catch(Exception e){throw new RuntimeException("Dhamana haijahifadhiwa",e);}
    }

    @PutMapping("/{collateralId}/verify")
    @PreAuthorize("hasAnyRole('LENDER','ADMIN')")
    public ResponseEntity<ApiResponse<LoanCollateral>> verify(@PathVariable Long loanId,@PathVariable Long collateralId,Authentication auth){
        Loan l=loan(loanId);
        var actor=users.findByEmail(auth.getName()).orElseThrow(()->new RuntimeException("Mtumiaji hajapatikana"));
        LoanCollateral c=collaterals.findById(collateralId).orElseThrow(()->new RuntimeException("Dhamana haijapatikana"));
        if(!c.getLoan().getId().equals(l.getId())) throw new RuntimeException("Dhamana si ya mkopo huu");
        if(actor.getRole()!=com.loanapp.model.User.Role.ADMIN && !l.getLender().getId().equals(actor.getId())) throw new RuntimeException("Huna ruhusa");
        c.setVerificationStatus("VERIFIED"); c.setVerifiedAt(LocalDateTime.now());
        return ResponseEntity.ok(ApiResponse.success("Dhamana imethibitishwa",collaterals.save(c)));
    }
}