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
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SignatureService {
    private static final String CONSENT_VERSION = "JMK-E-SIGN-V1";
    private final SignatureRepository signatures;
    private final LoanRepository loans;
    private final UserRepository users;
    private final GuarantorRepository guarantors;
    private final AuditService auditService;

    public SignatureService(SignatureRepository signatures,LoanRepository loans,UserRepository users,GuarantorRepository guarantors,AuditService auditService){
        this.signatures=signatures;this.loans=loans;this.users=users;this.guarantors=guarantors;this.auditService=auditService;
    }

    public Signature signLoan(SignatureRequest request,String email,String ipAddress){
        Loan loan=loans.findById(request.getLoanId()).orElseThrow(()->new RuntimeException("Mkopo haujapatikana"));
        User user=users.findByEmail(email).orElseThrow(()->new RuntimeException("Mtumiaji hajapatikana"));
        if(!isParticipant(loan,user)) throw new RuntimeException("Huna ruhusa ya kusaini mkopo huu");
        if(loan.getStatus()!=Loan.LoanStatus.APPROVED) throw new RuntimeException("Mkataba unaweza kusainiwa baada ya loan approval");
        if(!request.isConsentAccepted()) throw new RuntimeException("Lazima uthibitishe ridhaa ya kusaini kidigitali");
        if(!CONSENT_VERSION.equals(request.getConsentVersion())) throw new RuntimeException("Toleo la ridhaa ya e-signature halitumiki");
        if(request.getSignatureData()==null || !request.getSignatureData().startsWith("data:image/") || request.getSignatureData().length()>1000000)
            throw new RuntimeException("Sahihi si sahihi au ni kubwa sana");

        Signature.SignatureType requested=request.getSignatureType();
        Signature.SignatureType allowed=allowedType(loan,user);
        if(requested!=allowed) throw new RuntimeException("Akaunti yako inaruhusiwa kusaini kama "+allowed);
        if(!signatures.findByLoanAndSignatureType(loan,requested).isEmpty()) throw new RuntimeException("Sahihi ya "+requested+" tayari ipo kwenye mkopo huu");

        String agreementHash=agreementHash(loan);
        String signatureHash=sha256(request.getSignatureData());
        String consentHash=sha256("I have read, understood and agree to the JMK loan agreement and authorize this electronic signature. "+CONSENT_VERSION+"|"+agreementHash);
        Signature saved=signatures.save(Signature.builder()
            .loan(loan).user(user).signatureData(request.getSignatureData()).signatureType(requested)
            .ipAddress(ipAddress).deviceInfo(safeDevice(request.getDeviceInfo())).signatureHash(signatureHash)
            .agreementHash(agreementHash).consentHash(consentHash).consentVersion(CONSENT_VERSION)
            .signedByName(user.getFullName()).signedByEmail(user.getEmail()).consentAccepted(true).isValid(true).build());

        auditService.log(email,"E_SIGNATURE_CREATED","LOAN",loan.getId(),
            "Electronic signature "+requested+" captured; agreementHash="+agreementHash+", signatureHash="+signatureHash);
        return saved;
    }

    public List<Signature> getSignaturesByLoan(Long loanId,String email){
        Loan loan=loans.findById(loanId).orElseThrow(()->new RuntimeException("Mkopo haujapatikana"));
        User user=users.findByEmail(email).orElseThrow(()->new RuntimeException("Mtumiaji hajapatikana"));
        if(!isParticipant(loan,user)) throw new RuntimeException("Huna ruhusa ya kuona sahihi za mkopo huu");
        return signatures.findByLoan(loan);
    }

    public Map<String,Object> getStatus(Long loanId,String email){
        Loan loan=loans.findById(loanId).orElseThrow(()->new RuntimeException("Mkopo haujapatikana"));
        User user=users.findByEmail(email).orElseThrow(()->new RuntimeException("Mtumiaji hajapatikana"));
        if(!isParticipant(loan,user)) throw new RuntimeException("Huna ruhusa ya kuona status ya signatures");
        List<Signature> rows=signatures.findByLoan(loan);
        boolean borrower=signed(rows,Signature.SignatureType.BORROWER);
        boolean lender=signed(rows,Signature.SignatureType.LENDER);
        boolean guarantorRequired=!guarantors.findByLoan(loan).isEmpty();
        boolean guarantorSigned=!guarantorRequired || guarantors.findByLoan(loan).stream().allMatch(this::guarantorSigned);
        Map<String,Object> out=new LinkedHashMap<>();
        out.put("loanId",loanId);out.put("loanStatus",loan.getStatus());out.put("consentVersion",CONSENT_VERSION);
        out.put("agreementHash",agreementHash(loan));out.put("borrowerSigned",borrower);out.put("lenderSigned",lender);
        out.put("guarantorRequired",guarantorRequired);out.put("guarantorSigned",guarantorSigned);
        out.put("readyForDisbursement",borrower&&lender&&guarantorSigned);
        out.put("signatures",rows);
        return out;
    }

    public String agreementHash(Loan l){
        String canonical=String.join("|",
            "JMK-LOAN-AGREEMENT-V1",String.valueOf(l.getId()),safe(l.getBorrower().getId()),safe(l.getLender().getId()),
            safe(l.getAmount()),safe(l.getInterestRate()),safe(l.getDurationMonths()),safe(l.getDurationUnit()),
            safe(l.getTotalRepayment()),safe(l.getProcessingFee()),safe(l.getLawyerFee()),safe(l.getPurpose()),
            String.valueOf(l.getStatus()),String.valueOf(l.getNextDueDate()));
        return sha256(canonical);
    }

    private boolean signed(List<Signature> rows,Signature.SignatureType type){
        return rows.stream().anyMatch(s->s.getSignatureType()==type && Boolean.TRUE.equals(s.getIsValid()) && Boolean.TRUE.equals(s.getConsentAccepted()));
    }
    private boolean guarantorSigned(Guarantor g){
        if(g.getStatus()!=Guarantor.GuarantorStatus.APPROVED) return false;
        if("ONSITE".equalsIgnoreCase(g.getCaptureMode())) return g.getOnsiteSignatureData()!=null&&!g.getOnsiteSignatureData().isBlank();
        if("REMOTE".equalsIgnoreCase(g.getCaptureMode())) return g.getRemoteSignatureData()!=null&&!g.getRemoteSignatureData().isBlank();
        return signatures.findByLoan(g.getLoan()).stream().anyMatch(s->s.getSignatureType()==Signature.SignatureType.GUARANTOR && s.getUser()!=null && g.getGuarantor()!=null && s.getUser().getId().equals(g.getGuarantor().getId()));
    }

    private Signature.SignatureType allowedType(Loan loan,User user){
        if(user.getRole()==User.Role.ADMIN) return Signature.SignatureType.LENDER;
        if(loan.getBorrower().getId().equals(user.getId())) return Signature.SignatureType.BORROWER;
        if(loan.getLender().getId().equals(user.getId())) return Signature.SignatureType.LENDER;
        boolean approved=guarantors.findByLoan(loan).stream().anyMatch(g->g.getGuarantor()!=null&&g.getGuarantor().getId().equals(user.getId())&&g.getStatus()==Guarantor.GuarantorStatus.APPROVED);
        if(approved) return Signature.SignatureType.GUARANTOR;
        throw new RuntimeException("Huna nafasi ya kusaini mkopo huu");
    }
    private boolean isParticipant(Loan loan,User user){
        if(user.getRole()==User.Role.ADMIN) return true;
        if(loan.getBorrower().getId().equals(user.getId())||loan.getLender().getId().equals(user.getId())) return true;
        return guarantors.findByLoan(loan).stream().anyMatch(g->g.getGuarantor()!=null&&g.getGuarantor().getId().equals(user.getId()));
    }
    private String safe(Object v){return v==null?"":String.valueOf(v);}
    private String safeDevice(String v){return v==null?"unknown":v.substring(0,Math.min(v.length(),500));}
    private String sha256(String value){
        try{byte[] d=MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));StringBuilder b=new StringBuilder();for(byte x:d)b.append(String.format("%02x",x));return b.toString();}
        catch(Exception e){throw new IllegalStateException("Hashing failed",e);}
    }
}