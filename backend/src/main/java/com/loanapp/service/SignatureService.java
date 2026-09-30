package com.loanapp.service;

import com.loanapp.dto.SignatureRequest;
import com.loanapp.model.Guarantor;
import com.loanapp.model.Loan;
import com.loanapp.model.Signature;
import com.loanapp.model.User;
import com.loanapp.repository.GuarantorRepository;
import com.loanapp.repository.LoanRepository;
import com.loanapp.repository.SignatureRepository;
import com.loanapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SignatureService {
    @Autowired private SignatureRepository signatureRepository;
    @Autowired private LoanRepository loanRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private GuarantorRepository guarantorRepository;
    @Autowired private AuditService auditService;

    public Signature signLoan(SignatureRequest request, String email) {
        Loan loan = loanRepository.findById(request.getLoanId()).orElseThrow(() -> new RuntimeException("Mkopo haujapatikana"));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Mtumiaji hajapatikana"));
        if (!isParticipant(loan, user)) throw new RuntimeException("Huna ruhusa ya kusaini mkopo huu");
        if (loan.getStatus() != Loan.LoanStatus.APPROVED) throw new RuntimeException("Mkataba unaweza kusainiwa baada ya loan approval");

        Signature.SignatureType requested = request.getSignatureType();
        Signature.SignatureType allowed = allowedType(loan, user);
        if (requested != allowed) throw new RuntimeException("Akaunti yako inaruhusiwa kusaini kama " + allowed);
        if (!signatureRepository.findByLoanAndSignatureType(loan, requested).isEmpty()) throw new RuntimeException("Sahihi ya " + requested + " tayari ipo kwenye mkopo huu");

        Signature saved = signatureRepository.save(Signature.builder().loan(loan).user(user).signatureData(request.getSignatureData()).signatureType(requested).deviceInfo(request.getDeviceInfo()).isValid(true).build());
        auditService.log(email, "DOCUMENT_SIGNED", "LOAN", loan.getId(), "Digital signature type " + requested);
        return saved;
    }

    private Signature.SignatureType allowedType(Loan loan, User user) {
        if (user.getRole() == User.Role.ADMIN) return Signature.SignatureType.LENDER;
        if (loan.getBorrower().getId().equals(user.getId())) return Signature.SignatureType.BORROWER;
        if (loan.getLender().getId().equals(user.getId())) return Signature.SignatureType.LENDER;
        boolean approved = guarantorRepository.findByLoan(loan).stream().anyMatch(g -> g.getGuarantor().getId().equals(user.getId()) && g.getStatus() == Guarantor.GuarantorStatus.APPROVED);
        if (approved) return Signature.SignatureType.GUARANTOR;
        throw new RuntimeException("Huna nafasi ya kusaini mkopo huu");
    }

    private boolean isParticipant(Loan loan, User user) {
        if (user.getRole() == User.Role.ADMIN) return true;
        if (loan.getBorrower().getId().equals(user.getId()) || loan.getLender().getId().equals(user.getId())) return true;
        return guarantorRepository.findByLoan(loan).stream().anyMatch(g -> g.getGuarantor().getId().equals(user.getId()));
    }

    public List<Signature> getSignaturesByLoan(Long loanId, String email) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new RuntimeException("Mkopo haujapatikana"));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Mtumiaji hajapatikana"));
        if (!isParticipant(loan, user)) throw new RuntimeException("Huna ruhusa ya kuona sahihi za mkopo huu");
        return signatureRepository.findByLoan(loan);
    }
}