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

    @Autowired
    private SignatureRepository signatureRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    public Signature signLoan(SignatureRequest request, String userEmail) {
        Loan loan = loanRepository.findById(request.getLoanId())
                .orElseThrow(() -> new RuntimeException("Mkopo haujapatikana"));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Mtumiaji hajapatikana"));

        Signature signature = Signature.builder()
                .loan(loan)
                .user(user)
                .signatureData(request.getSignatureData())
                .signatureType(request.getSignatureType())
                .deviceInfo(request.getDeviceInfo())
                .isValid(true)
                .build();

        return signatureRepository.save(signature);
    }

    public List<Signature> getSignaturesByLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Mkopo haujapatikana"));
        return signatureRepository.findByLoan(loan);
    }
}
