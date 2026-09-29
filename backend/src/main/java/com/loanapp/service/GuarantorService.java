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

    public Guarantor addGuarantor(Long loanId, GuarantorRequest request) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Mkopo haujapatikana"));
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

    public List<Guarantor> getGuarantorsByLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Mkopo haujapatikana"));
        return guarantorRepository.findByLoan(loan);
    }

    public List<Guarantor> getGuarantorsByUser(String email) {
        User guarantor = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Mdhamini hajapatikana"));
        return guarantorRepository.findByGuarantor(guarantor);
    }
}
