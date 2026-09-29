package com.loanapp.service;

import com.loanapp.dto.PaymentRequest;
import com.loanapp.model.Loan;
import com.loanapp.model.Payment;
import com.loanapp.model.User;
import com.loanapp.repository.LoanRepository;
import com.loanapp.repository.PaymentRepository;
import com.loanapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {
    @Autowired private PaymentRepository paymentRepository;
    @Autowired private LoanRepository loanRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private AuditService auditService;

    @Transactional
    public Payment createPayment(PaymentRequest request,String email){
        Loan loan=loanRepository.findById(request.getLoanId()).orElseThrow(()->new RuntimeException("Mkopo haujapatikana"));
        User borrower=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("Mkopaji hajapatikana"));
        if(!loan.getBorrower().getId().equals(borrower.getId())) throw new RuntimeException("Huna ruhusa ya kulipia mkopo huu");
        if(loan.getStatus()==Loan.LoanStatus.REJECTED||loan.getStatus()==Loan.LoanStatus.PAID) throw new RuntimeException("Mkopo huu haupokei malipo kwa sasa");
        if(request.getAmount()==null||request.getAmount().compareTo(BigDecimal.ZERO)<=0) throw new RuntimeException("Kiasi cha malipo lazima kiwe zaidi ya sifuri");
        Payment.PaymentMethod method;
        try{method=Payment.PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase());}catch(Exception e){throw new RuntimeException("Njia ya malipo si sahihi");}
        BigDecimal paid=paymentRepository.findByLoanAndStatus(loan,Payment.PaymentStatus.SUCCESS).stream().map(Payment::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal pending=paymentRepository.findByLoanAndStatus(loan,Payment.PaymentStatus.PENDING).stream().map(Payment::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal remaining=loan.getTotalRepayment().subtract(paid).subtract(pending);
        if(request.getAmount().compareTo(remaining)>0) throw new RuntimeException("Malipo yanazidi salio la mkopo: "+remaining);
        Payment saved=paymentRepository.save(Payment.builder().loan(loan).amount(request.getAmount()).paymentMethod(method).transactionId("TXN-"+UUID.randomUUID().toString().substring(0,8).toUpperCase()).status(Payment.PaymentStatus.PENDING).build()); auditService.log(email,"PAYMENT_CREATED","PAYMENT",saved.getId(),"Payment initiated"); return saved;
    }

    @Transactional
    public Payment confirmPayment(Long id,String transactionId,String actorEmail){
        Payment payment=paymentRepository.findById(id).orElseThrow(()->new RuntimeException("Malipo hayajapatikana"));
        User actor=userRepository.findByEmail(actorEmail).orElseThrow(()->new RuntimeException("Mtumiaji hajapatikana"));
        if(actor.getRole()!=User.Role.ADMIN&&!payment.getLoan().getLender().getId().equals(actor.getId())) throw new RuntimeException("Huna ruhusa ya kuthibitisha malipo haya");
        if(payment.getStatus()==Payment.PaymentStatus.SUCCESS) return payment;
        payment.setStatus(Payment.PaymentStatus.SUCCESS);
        if(transactionId!=null&&!transactionId.isBlank()) payment.setTransactionId(transactionId.trim());
        payment.setPaidAt(LocalDateTime.now());
        Payment saved=paymentRepository.save(payment); Loan loan=saved.getLoan();
        BigDecimal paid=paymentRepository.findByLoanAndStatus(loan,Payment.PaymentStatus.SUCCESS).stream().map(Payment::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        if(paid.compareTo(loan.getTotalRepayment())>=0) loan.setStatus(Loan.LoanStatus.PAID);
        else if(loan.getStatus()==Loan.LoanStatus.APPROVED) loan.setStatus(Loan.LoanStatus.DISBURSED);
        loanRepository.save(loan); auditService.log(actorEmail,"PAYMENT_CONFIRMED","PAYMENT",saved.getId(),"Payment confirmed"); return saved;
    }

    public List<Payment> getPaymentsByLoan(Long loanId,String email){
        Loan loan=loanRepository.findById(loanId).orElseThrow(()->new RuntimeException("Mkopo haujapatikana"));
        User actor=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("Mtumiaji hajapatikana"));
        if(actor.getRole()!=User.Role.ADMIN&&!loan.getBorrower().getId().equals(actor.getId())&&!loan.getLender().getId().equals(actor.getId())) throw new RuntimeException("Huna ruhusa ya kuona malipo haya");
        return paymentRepository.findByLoan(loan);
    }
    public Payment getPaymentById(Long id,String email){
        Payment p=paymentRepository.findById(id).orElseThrow(()->new RuntimeException("Malipo hayajapatikana"));
        User actor=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("Mtumiaji hajapatikana"));
        if(actor.getRole()!=User.Role.ADMIN&&!p.getLoan().getBorrower().getId().equals(actor.getId())&&!p.getLoan().getLender().getId().equals(actor.getId())) throw new RuntimeException("Huna ruhusa ya kuona malipo haya");
        return p;
    }
}
