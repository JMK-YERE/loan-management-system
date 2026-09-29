package com.loanapp.service;

import com.loanapp.dto.SignatureRequest;
import com.loanapp.model.Loan;
import com.loanapp.model.Signature;
import com.loanapp.model.User;
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

    public Signature signLoan(SignatureRequest request,String email){
        Loan loan=loanRepository.findById(request.getLoanId()).orElseThrow(()->new RuntimeException("Mkopo haujapatikana"));
        User user=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("Mtumiaji hajapatikana"));
        if(!isParticipant(loan,user)) throw new RuntimeException("Huna ruhusa ya kusaini mkopo huu");
        return signatureRepository.save(Signature.builder().loan(loan).user(user).signatureData(request.getSignatureData()).signatureType(request.getSignatureType()).deviceInfo(request.getDeviceInfo()).isValid(true).build());
    }
    private boolean isParticipant(Loan loan,User user){return user.getRole()==User.Role.ADMIN||loan.getBorrower().getId().equals(user.getId())||loan.getLender().getId().equals(user.getId())||signatureRepository.findByLoan(loan).stream().anyMatch(s->s.getUser().getId().equals(user.getId()));}
    public List<Signature> getSignaturesByLoan(Long loanId,String email){
        Loan loan=loanRepository.findById(loanId).orElseThrow(()->new RuntimeException("Mkopo haujapatikana"));
        User user=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("Mtumiaji hajapatikana"));
        if(!isParticipant(loan,user)) throw new RuntimeException("Huna ruhusa ya kuona sahihi za mkopo huu");
        return signatureRepository.findByLoan(loan);
    }
}
