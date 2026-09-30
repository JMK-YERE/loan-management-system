package com.loanapp.service;

import com.loanapp.dto.GuarantorRequest;
import com.loanapp.model.Guarantor;
import com.loanapp.model.Loan;
import com.loanapp.model.User;
import com.loanapp.repository.GuarantorRepository;
import com.loanapp.repository.LoanRepository;
import com.loanapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GuarantorService {

    @Autowired
    private GuarantorRepository guarantorRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditService auditService;

    public Guarantor addGuarantor(Long loanId, GuarantorRequest request, String lenderEmail) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Mkopo haujapatikana"));
        User lender = userRepository.findByEmail(lenderEmail).orElseThrow(() -> new RuntimeException("Mkopeshaji hajapatikana"));
        if (!loan.getLender().getId().equals(lender.getId()) && lender.getRole() != User.Role.ADMIN) throw new RuntimeException("Huna ruhusa ya kuongeza mdhamini kwenye mkopo huu");
        User guarantor = userRepository.findById(request.getGuarantorId())
                .orElseThrow(() -> new RuntimeException("Mdhamini hajapatikana"));

        Guarantor g = Guarantor.builder()
                .loan(loan)
                .guarantor(guarantor)
                .guaranteedAmount(request.getGuaranteedAmount())
                .relationship(request.getRelationship())
                .status(Guarantor.GuarantorStatus.PENDING)
                .build();

        return guarantorRepository.save(g);
    }

    public Guarantor approveGuarantor(Long id, String email) {
        Guarantor g = getForAction(id, email);
        g.setStatus(Guarantor.GuarantorStatus.APPROVED);
        g.setApprovedAt(LocalDateTime.now());
        return guarantorRepository.save(g);
    }

    public Guarantor rejectGuarantor(Long id, String email) {
        Guarantor g = getForAction(id, email);
        g.setStatus(Guarantor.GuarantorStatus.REJECTED);
        return guarantorRepository.save(g);
    }

    private Guarantor getForAction(Long id, String email) {
        Guarantor g = guarantorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mdhamini hajapatikana"));
        if (!g.getGuarantor().getEmail().equalsIgnoreCase(email)) {
            User actor = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Mtumiaji hajapatikana"));
            if (actor.getRole() != User.Role.ADMIN) {
                throw new RuntimeException("Huna ruhusa ya kufanya kitendo hiki");
            }
        }
        return g;
    }

    public List<Guarantor> getGuarantorsByLoan(Long loanId, String email) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Mkopo haujapatikana"));
        User actor=userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Mtumiaji hajapatikana"));
        boolean participant=actor.getRole()==User.Role.ADMIN
                || loan.getLender().getId().equals(actor.getId())
                || loan.getBorrower().getId().equals(actor.getId())
                || guarantorRepository.findByLoan(loan).stream().anyMatch(g->g.getGuarantor().getId().equals(actor.getId()));
        if(!participant) throw new RuntimeException("Huna ruhusa kuona taarifa za wadhamini wa mkopo huu");
        return guarantorRepository.findByLoan(loan);
    }

    public Guarantor updateGuarantor(Long id, GuarantorRequest request, String email) {
        Guarantor current=guarantorRepository.findById(id).orElseThrow(() -> new RuntimeException("Mdhamini hajapatikana"));
        User borrower=userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Mkopaji hajapatikana"));
        if(!current.getLoan().getBorrower().getId().equals(borrower.getId())) throw new RuntimeException("Huna ruhusa");
        if(current.getLoan().getStatus()!=Loan.LoanStatus.PENDING) throw new RuntimeException("Mdhamini anaweza kubadilishwa kabla ya loan approval tu");
        User replacement=userRepository.findById(request.getGuarantorId()).orElseThrow(() -> new RuntimeException("Mdhamini mpya hajapatikana"));
        if(replacement.getRole()!=User.Role.GUARANTOR || !Boolean.TRUE.equals(replacement.getActive()) || replacement.getStatus()!=User.UserStatus.APPROVED)
            throw new RuntimeException("Mtumiaji aliyechaguliwa si mdhamini aliyeidhinishwa");
        if(replacement.getId().equals(borrower.getId())) throw new RuntimeException("Mkopaji hawezi kuwa mdhamini wake");
        current.setGuarantor(replacement);
        current.setGuaranteedAmount(request.getGuaranteedAmount());
        current.setRelationship(request.getRelationship());
        current.setStatus(Guarantor.GuarantorStatus.PENDING);
        current.setApprovedAt(null);
        Guarantor saved=guarantorRepository.save(current);
        auditService.log(email,"GUARANTOR_UPDATED","GUARANTOR",id,"Borrower updated guarantor and guarantee amount");
        return saved;
    }

    public List<Guarantor> getGuarantorsByUser(String email) {
        User guarantor = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Mdhamini hajapatikana"));
        return guarantorRepository.findByGuarantor(guarantor);
    }
}
