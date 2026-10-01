package com.loanapp.service;

import com.loanapp.dto.GuarantorRequest;
import com.loanapp.dto.OnsiteGuarantorRequest;
import com.loanapp.model.Guarantor;
import com.loanapp.model.Loan;
import com.loanapp.model.User;
import com.loanapp.repository.GuarantorRepository;
import com.loanapp.repository.LoanRepository;
import com.loanapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @Autowired
    private NotificationService notifications;

    @Value("${app.frontend.url:}")
    private String frontendUrl;

    public Guarantor addGuarantor(Long loanId, GuarantorRequest request, String lenderEmail) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Mkopo haujapatikana"));
        User lender = userRepository.findByEmail(lenderEmail).orElseThrow(() -> new RuntimeException("Mkopeshaji hajapatikana"));
        if (!loan.getLender().getId().equals(lender.getId()) && lender.getRole() != User.Role.ADMIN) throw new RuntimeException("Huna ruhusa ya kuongeza mdhamini kwenye mkopo huu");
        User guarantor = userRepository.findById(request.getGuarantorId())
                .orElseThrow(() -> new RuntimeException("Mdhamini hajapatikana"));

        if (guarantor.getId().equals(loan.getBorrower().getId())) {
            throw new RuntimeException("Mkopaji hawezi kuwa mdhamini wake mwenyewe");
        }
        if (guarantor.getRole() != User.Role.GUARANTOR || !Boolean.TRUE.equals(guarantor.getActive()) || guarantor.getStatus() != User.UserStatus.APPROVED) {
            throw new RuntimeException("Chagua mdhamini mwenye account iliyoidhinishwa na hai");
        }
        if (request.getGuaranteedAmount().compareTo(loan.getAmount()) > 0) {
            throw new RuntimeException("Kiasi cha dhamana hakiwezi kuzidi kiasi cha mkopo");
        }

        Guarantor g = Guarantor.builder()
                .loan(loan)
                .guarantor(guarantor)
                .guaranteedAmount(request.getGuaranteedAmount())
                .relationship(request.getRelationship())
                .status(Guarantor.GuarantorStatus.PENDING)
                .build();

        Guarantor saved = guarantorRepository.save(g);
        String text = "JmkLoanApp: umeombwa kuwa mdhamini wa " + loan.getBorrower().getFullName()
                + ". Kiasi cha mkopo TZS " + loan.getAmount()
                + ", exposure yako TZS " + request.getGuaranteedAmount()
                + ". Ingia kwenye mfumo kusoma mkataba na kukubali/kukataa.";
        notifications.sendSms(guarantor.getPhone(), text);
        notifications.sendWhatsApp(guarantor.getPhone(), text);
        if (guarantor.getEmail() != null && !guarantor.getEmail().isBlank()) {
            notifications.sendEmail(guarantor.getEmail(), "JmkLoanApp - Ombi la Udhamini",
                    "<div style='font-family:Arial,sans-serif'><p>Habari " + guarantor.getFullName()
                    + ",</p><p>Umeombwa kuwa mdhamini wa " + loan.getBorrower().getFullName()
                    + ". Kiasi cha mkopo ni TZS " + loan.getAmount()
                    + " na exposure yako ni TZS " + request.getGuaranteedAmount()
                    + ".</p><p>Ingia JmkLoanApp kusoma mkataba na kukubali au kukataa.</p></div>");
        }
        auditService.log(lenderEmail,"GUARANTOR_ASSIGNED","GUARANTOR",saved.getId(),"Guarantor notified with loan amount and guarantee exposure");
        return saved;
    }

    @Transactional
    public Guarantor addOnsiteGuarantor(Long loanId, OnsiteGuarantorRequest request, String lenderEmail) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new RuntimeException("Mkopo haujapatikana"));
        User lender = userRepository.findByEmail(lenderEmail).orElseThrow(() -> new RuntimeException("Mkopeshaji hajapatikana"));
        if (lender.getRole() != User.Role.ADMIN && !loan.getLender().getId().equals(lender.getId()))
            throw new RuntimeException("Huna ruhusa ya kuweka mdhamini kwenye mkopo huu");
        if (loan.getStatus() == Loan.LoanStatus.DISBURSED || loan.getStatus() == Loan.LoanStatus.PAID)
            throw new RuntimeException("Mkopo huu tayari umeanza/umemalizika; onsite guarantor hawezi kuongezwa");
        if (request.getGuaranteedAmount().compareTo(loan.getAmount()) > 0)
            throw new RuntimeException("Kiasi cha dhamana hakiwezi kuzidi kiasi cha mkopo");
        if (!request.getPhotoData().startsWith("data:image/") || request.getPhotoData().length() > 5000000)
            throw new RuntimeException("Picha ya mdhamini si sahihi au ni kubwa");
        if (!request.getSignatureData().startsWith("data:image/") || request.getSignatureData().length() > 1000000)
            throw new RuntimeException("Sahihi ya mdhamini si sahihi au ni kubwa");

        boolean existing = guarantorRepository.findByLoan(loan).stream()
                .anyMatch(g -> g.getStatus() == Guarantor.GuarantorStatus.PENDING || g.getStatus() == Guarantor.GuarantorStatus.APPROVED);
        if (existing) throw new RuntimeException("Mkopo huu tayari una mdhamini aliyewekwa");

        Guarantor g = Guarantor.builder()
                .loan(loan)
                .guarantor(null)
                .guarantorName(request.getName().trim())
                .guarantorPhone(request.getPhone())
                .guarantorIdNumber(request.getIdNumber())
                .guarantorPhotoData(request.getPhotoData())
                .onsiteSignatureData(request.getSignatureData())
                .guaranteedAmount(request.getGuaranteedAmount())
                .relationship(request.getRelationship())
                .captureMode("ONSITE")
                .capturedBy(lenderEmail)
                .capturedAt(LocalDateTime.now())
                .status(Guarantor.GuarantorStatus.APPROVED)
                .approvedAt(LocalDateTime.now())
                .build();

        Guarantor saved = guarantorRepository.save(g);
        auditService.log(lenderEmail, "ONSITE_GUARANTOR_CAPTURED", "GUARANTOR", saved.getId(),
                "Onsite guarantor captured with photo and signature for loan #" + loanId);
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            String text = "JmkLoanApp: " + request.getName() + " amerekodiwa kama mdhamini wa "
                    + loan.getBorrower().getFullName() + ". Kiasi cha mkopo TZS " + loan.getAmount()
                    + ", liability TZS " + request.getGuaranteedAmount() + ".";
            notifications.sendSms(request.getPhone(), text);
            notifications.sendWhatsApp(request.getPhone(), text);
        }
        return saved;
    }

    @Transactional
    public Map<String,Object> createRemoteInvite(Long loanId, String name, String phone, String idNumber, String relationship, java.math.BigDecimal amount, String lenderEmail) {
        Loan loan=loanRepository.findById(loanId).orElseThrow(()->new RuntimeException("Mkopo haujapatikana"));
        User lender=userRepository.findByEmail(lenderEmail).orElseThrow(()->new RuntimeException("Mkopeshaji hajapatikana"));
        if(lender.getRole()!=User.Role.ADMIN && !loan.getLender().getId().equals(lender.getId())) throw new RuntimeException("Huna ruhusa ya loan hii");
        if(loan.getStatus()==Loan.LoanStatus.DISBURSED || loan.getStatus()==Loan.LoanStatus.PAID) throw new RuntimeException("Loan tayari imeanza/imefungwa");
        if(name==null||name.isBlank()) throw new RuntimeException("Jina la mdhamini linahitajika");
        if(amount==null||amount.signum()<0||amount.compareTo(loan.getAmount())>0) throw new RuntimeException("Kiasi cha udhamini si sahihi");
        boolean existing=guarantorRepository.findByLoan(loan).stream().anyMatch(g->g.getStatus()==Guarantor.GuarantorStatus.PENDING||g.getStatus()==Guarantor.GuarantorStatus.APPROVED);
        if(existing) throw new RuntimeException("Loan hii tayari ina mdhamini");
        String token=UUID.randomUUID().toString().replace("-","")+UUID.randomUUID().toString().replace("-","");
        Guarantor g=Guarantor.builder().loan(loan).guarantor(null).guarantorName(name.trim()).guarantorPhone(phone).guarantorIdNumber(idNumber)
          .guaranteedAmount(amount).relationship(relationship).captureMode("REMOTE").capturedBy(lenderEmail)
          .status(Guarantor.GuarantorStatus.PENDING).remoteTokenHash(hash(token)).remoteExpiresAt(LocalDateTime.now().plusHours(48)).build();
        Guarantor saved=guarantorRepository.save(g);
        String base=(frontendUrl==null||frontendUrl.isBlank())?"":frontendUrl.replaceAll("/+$","");
        String link=base+"/guarantor-sign/"+token;
        String body="JmkLoanApp: "+name+" umeombwa kuwa mdhamini wa "+loan.getBorrower().getFullName()+". Loan #"+loan.getId()+", liability TZS "+amount+". Fungua: "+link;
        if(phone!=null&&!phone.isBlank()){notifications.sendSms(phone,body);notifications.sendWhatsApp(phone,body);}
        auditService.log(lenderEmail,"REMOTE_GUARANTOR_INVITED","GUARANTOR",saved.getId(),"Secure remote guarantor signing link created for loan #"+loanId);
        return Map.of("guarantorId",saved.getId(),"loanId",loanId,"link",link,"expiresAt",saved.getRemoteExpiresAt());
    }

    public Map<String,Object> getRemoteInvite(String token){
        Guarantor g=findByRemoteToken(token);
        if(g.getStatus()!=Guarantor.GuarantorStatus.PENDING) throw new RuntimeException("Kiungo hiki hakisubiri sahihi");
        Loan l=g.getLoan();
        return Map.of("guarantorId",g.getId(),"loanId",l.getId(),"borrowerName",l.getBorrower().getFullName(),
          "loanAmount",l.getAmount(),"guarantorName",g.getGuarantorName(),"guarantorPhone",g.getGuarantorPhone()==null?"":g.getGuarantorPhone(),
          "guaranteedAmount",g.getGuaranteedAmount(),"relationship",g.getRelationship()==null?"":g.getRelationship(),
          "purpose",l.getPurpose()==null?"":l.getPurpose(),"interestRate",l.getInterestRate(),"processingFee",l.getProcessingFee(),
          "totalRepayment",l.getTotalRepayment(),"duration",l.getDurationMonths(),"durationUnit",l.getDurationUnit()==null?"MONTHS":l.getDurationUnit(),
          "nextDueDate",l.getNextDueDate()==null?"":l.getNextDueDate(),"expiresAt",g.getRemoteExpiresAt());
    }

    @Transactional
    public Map<String,Object> signRemote(String token,String signatureData,String deviceInfo){
        Guarantor g=findByRemoteToken(token);
        if(g.getStatus()!=Guarantor.GuarantorStatus.PENDING) throw new RuntimeException("Mdhamini tayari ameshughulikiwa");
        if(signatureData==null||!signatureData.startsWith("data:image/")||signatureData.length()>1000000) throw new RuntimeException("Sahihi si sahihi");
        g.setRemoteSignatureData(signatureData);g.setRemoteSignedAt(LocalDateTime.now());g.setStatus(Guarantor.GuarantorStatus.APPROVED);g.setApprovedAt(LocalDateTime.now());
        Guarantor saved=guarantorRepository.save(g);
        auditService.log("REMOTE_GUARANTOR","REMOTE_GUARANTOR_SIGNED","GUARANTOR",saved.getId(),"Remote guarantor signed loan #"+g.getLoan().getId()+" via secure invite");
        String lenderPhone=g.getLoan().getLender().getPhone();
        notifications.sendSms(lenderPhone,"JmkLoanApp: Mdhamini "+g.getGuarantorName()+" amesaini remote kwa Loan #"+g.getLoan().getId()+". Liability TZS "+g.getGuaranteedAmount()+".");
        notifications.sendWhatsApp(lenderPhone,"JmkLoanApp: Mdhamini "+g.getGuarantorName()+" amesaini remote kwa Loan #"+g.getLoan().getId()+".");
        return Map.of("guarantorId",saved.getId(),"loanId",g.getLoan().getId(),"status",saved.getStatus(),"signedAt",saved.getRemoteSignedAt());
    }

    private Guarantor findByRemoteToken(String token){
        if(token==null||token.isBlank()) throw new RuntimeException("Signing link si sahihi");
        Guarantor g=guarantorRepository.findByRemoteTokenHash(hash(token)).orElseThrow(()->new RuntimeException("Signing link haijapatikana"));
        if(g.getRemoteExpiresAt()==null||g.getRemoteExpiresAt().isBefore(LocalDateTime.now())) throw new RuntimeException("Signing link ime-expire");
        return g;
    }

    private String hash(String token){
        try{return Base64.getUrlEncoder().withoutPadding().encodeToString(MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8)));}
        catch(Exception e){throw new IllegalStateException("Token hashing failed",e);}
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
        if (g.getGuarantor() == null) throw new RuntimeException("Mdhamini huyu alisainiwa onsite; hakuna account ya ku-approve hapa");
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
                || guarantorRepository.findByLoan(loan).stream().anyMatch(g->g.getGuarantor()!=null && g.getGuarantor().getId().equals(actor.getId()));
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
