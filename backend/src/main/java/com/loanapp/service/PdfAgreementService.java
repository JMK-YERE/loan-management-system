package com.loanapp.service;

import com.loanapp.model.Guarantor;
import com.loanapp.model.Loan;
import com.loanapp.model.User;
import com.loanapp.repository.GuarantorRepository;
import com.loanapp.repository.LoanRepository;
import com.loanapp.repository.SignatureRepository;
import com.loanapp.repository.UserRepository;
import com.loanapp.repository.LoanCollateralRepository;
import com.loanapp.repository.RepaymentScheduleRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class PdfAgreementService {
 @Value("${app.company.name:JmkLoanApp}") private String companyName;
 @Value("${app.company.address:}") private String companyAddress;
 @Value("${app.company.phone:}") private String companyPhone;
 @Value("${app.company.email:}") private String companyEmail;
 @Value("${app.company.license:}") private String companyLicense;
 private final LoanRepository loans; private final UserRepository users; private final SignatureRepository signatures; private final GuarantorRepository guarantors; private final LoanCollateralRepository collaterals; private final RepaymentScheduleRepository schedules;
 public PdfAgreementService(LoanRepository loans,UserRepository users,SignatureRepository signatures,GuarantorRepository guarantors,LoanCollateralRepository collaterals,RepaymentScheduleRepository schedules){this.loans=loans;this.users=users;this.signatures=signatures;this.guarantors=guarantors;this.collaterals=collaterals;this.schedules=schedules;}

 public byte[] generate(Long id,String email){
  Loan l=loans.findById(id).orElseThrow(()->new RuntimeException("Mkopo haujapatikana"));
  User actor=users.findByEmail(email).orElseThrow(()->new RuntimeException("Mtumiaji hajapatikana"));
  boolean participant=actor.getRole()==User.Role.ADMIN
    ||l.getBorrower().getId().equals(actor.getId())
    ||l.getLender().getId().equals(actor.getId())
    ||guarantors.findByLoan(l).stream().anyMatch(g->g.getGuarantor()!=null && g.getGuarantor().getId().equals(actor.getId()));
  if(!participant) throw new RuntimeException("Huna ruhusa ya mkataba huu");

  try{
   ByteArrayOutputStream out=new ByteArrayOutputStream(); Document d=new Document(); PdfWriter.getInstance(d,out); d.open();
   Font title=FontFactory.getFont(FontFactory.HELVETICA_BOLD,18); Font h=FontFactory.getFont(FontFactory.HELVETICA_BOLD,12);
   d.add(new Paragraph("JMK LOAN - LOAN AGREEMENT",title));
   d.add(new Paragraph("Mkataba wa Mkopo / Loan Agreement"));
   d.add(new Paragraph("Agreement reference: JMK-LOAN-"+l.getId()+" | Version: FINAL"));
   d.add(new Paragraph(" "));
   d.add(new Paragraph("PART A — TAARIFA ZA MKOPAJI",h));
   d.add(new Paragraph("Jina kamili: "+l.getBorrower().getFullName()));
   d.add(new Paragraph("Barua pepe: "+l.getBorrower().getEmail()+" | Simu: "+(l.getBorrower().getPhone()==null?"—":l.getBorrower().getPhone())));
   d.add(new Paragraph("NIDA/ID: "+(l.getBorrower().getNidaNumber()==null?"—":l.getBorrower().getNidaNumber())+" | Ajira: "+(l.getBorrower().getEmploymentStatus()==null?"—":l.getBorrower().getEmploymentStatus())));
   d.add(new Paragraph("Kazi: "+(l.getBorrower().getOccupation()==null?"—":l.getBorrower().getOccupation())+" | Mwajiri: "+(l.getBorrower().getEmployer()==null?"—":l.getBorrower().getEmployer())));
   d.add(new Paragraph("Anwani: "+(l.getBorrower().getAddress()==null?"—":l.getBorrower().getAddress())+" | Mji: "+(l.getBorrower().getCity()==null?"—":l.getBorrower().getCity())));
   d.add(new Paragraph("Next of kin: "+(l.getBorrower().getKinName()==null?"—":l.getBorrower().getKinName())+" | Simu: "+(l.getBorrower().getKinPhone()==null?"—":l.getBorrower().getKinPhone())+" | Uhusiano: "+(l.getBorrower().getKinRelationship()==null?"—":l.getBorrower().getKinRelationship())));
   d.add(new Paragraph(" "));
   d.add(new Paragraph("PART B — TAARIFA ZA MKOPO",h));   d.add(new Paragraph(companyName+" — MKATABA WA MKOPO",h));
   d.add(new Paragraph(companyAddress+" · "+companyPhone+" · "+companyEmail+" · Licence: "+companyLicense));
   d.add(new Paragraph("Loan #"+l.getId(),h));
   d.add(new Paragraph("Borrower: "+l.getBorrower().getFullName()+" ("+l.getBorrower().getEmail()+")"));
   d.add(new Paragraph("Lender: "+l.getLender().getFullName()+" ("+l.getLender().getEmail()+")"));
   d.add(new Paragraph("Principal: TZS "+l.getAmount()));
   d.add(new Paragraph("Interest rate: "+l.getInterestRate()+"%"));
   d.add(new Paragraph("Duration: "+l.getDurationMonths()+" "+(l.getDurationUnit()==null?"MONTHS":l.getDurationUnit())));
   d.add(new Paragraph("Processing fee: TZS "+l.getProcessingFee()));
   d.add(new Paragraph("Lawyer fee: TZS "+l.getLawyerFee()));
   d.add(new Paragraph("Total repayment: TZS "+l.getTotalRepayment()));
   d.add(new Paragraph("Purpose: "+(l.getPurpose()==null?"—":l.getPurpose())));
   d.add(new Paragraph("Collateral: "+(l.getCollateralDescription()==null?"Hakuna":l.getCollateralDescription())));
   d.add(new Paragraph("Collateral value: TZS "+(l.getCollateralValue()==null?"0":l.getCollateralValue())));
   addImageData(d,l.getCollateralPhotoData(),"Collateral evidence photo",h,420,260);
   d.add(new Paragraph("Collateral evidence records",h));
   var collateralRecords=collaterals.findByLoanOrderByIdAsc(l);
   if(collateralRecords.isEmpty()) d.add(new Paragraph("Hakuna collateral record ya ziada iliyowekwa."));
   for(var c:collateralRecords){
    d.add(new Paragraph("Type: "+c.getType()+" | Value: TZS "+c.getValue()+" | Verification: "+c.getVerificationStatus()));
    d.add(new Paragraph("Description: "+c.getDescription()));
    if(c.getPhotoDataJson()!=null){
      try{
       var photos=new com.fasterxml.jackson.databind.ObjectMapper().readValue(c.getPhotoDataJson(),java.util.List.class);
       for(Object p:photos) addImageData(d,String.valueOf(p),"Collateral photo",h,420,260);
      }catch(Exception ignored){}
    }
    if(c.getDocumentDataJson()!=null){
      try{
       var docs=new com.fasterxml.jackson.databind.ObjectMapper().readValue(c.getDocumentDataJson(),java.util.List.class);
       d.add(new Paragraph("Supporting documents: "+docs.size(),h));
       for(Object item:docs){
        if(item instanceof java.util.Map<?,?> m){
         String name=String.valueOf(m.get("name"));
         String data=String.valueOf(m.get("data"));
         d.add(new Paragraph("Document: "+name));
         if(data.startsWith("data:image/")) addImageData(d,data,"Document image",h,420,260);
        }
       }
      }catch(Exception ignored){}
    }
   }
   d.add(new Paragraph("PART C — DHUMUNI, DHAMANA NA RATIBA",h));
   d.add(new Paragraph("Dhumuni la mkopo: "+(l.getPurpose()==null?"—":l.getPurpose())));
   d.add(new Paragraph("Dhamana: "+(l.getCollateralDescription()==null?"Hakuna":l.getCollateralDescription())+" | Thamani: TZS "+(l.getCollateralValue()==null?"0":l.getCollateralValue())));
   d.add(new Paragraph("REPAYMENT SCHEDULE",h));
   var scheduleRows=schedules.findByLoanOrderByInstallmentNumberAsc(l);
   if(scheduleRows.isEmpty()) d.add(new Paragraph("Ratiba bado haijatengenezwa."));
   for(var s:scheduleRows) d.add(new Paragraph("#"+s.getInstallmentNumber()+" | Due: "+s.getDueDate()+" | Principal: TZS "+s.getPrincipalDue()+" | Interest: TZS "+s.getInterestDue()+" | Fees: TZS "+s.getFeesDue()+" | Due: TZS "+s.getAmountDue()+" | Paid: TZS "+s.getAmountPaid()+" | Status: "+s.getStatus()));
   d.add(new Paragraph(" "));
   d.add(new Paragraph("PART D — TAMKO LA MKOPAJI",h));
   d.add(new Paragraph("Mkopaji anathibitisha kuwa taarifa zilizotolewa ni za kweli, amepata nafasi ya kusoma masharti, anaelewa kiasi cha mkopo, riba, ada, faini, ratiba na wajibu wa marejesho, na atatumia njia rasmi za taasisi kuwasiliana kuhusu changamoto za malipo."));
   d.add(new Paragraph("Mabadiliko ya masharti ya baadaye hayabadilishi kimya kimya snapshot ya mkataba huu; mabadiliko yanayohitaji ridhaa yatafanywa kwa utaratibu unaotambulika."));
   d.add(new Paragraph(" "));   d.add(new Paragraph("Status: "+l.getStatus()));
   d.add(new Paragraph("Next due date: "+(l.getNextDueDate()==null?"—":l.getNextDueDate())));
   d.add(new Paragraph(" "));

   d.add(new Paragraph("Guarantor / Dhamana responsibility",h));
   var gs=guarantors.findByLoan(l);
   if(gs.isEmpty()) d.add(new Paragraph("Hakuna mdhamini aliyewekwa."));
   for(Guarantor g:gs){
    String name=g.getGuarantor()!=null?g.getGuarantor().getFullName():g.getGuarantorName();
    String contact=g.getGuarantor()!=null?g.getGuarantor().getPhone():g.getGuarantorPhone();
    d.add(new Paragraph("Guarantor: "+(name==null?"—":name)+" | Simu: "+(contact==null?"—":contact)));
    d.add(new Paragraph("Amount covered: TZS "+g.getGuaranteedAmount()+" | Status: "+g.getStatus()+" | Mode: "+g.getCaptureMode()));
    if(g.getGuarantorIdNumber()!=null) d.add(new Paragraph("ID: "+g.getGuarantorIdNumber()));
    addImageData(d,g.getGuarantorPhotoData(),"Guarantor photo",h,220,180);
    addImageData(d,g.getOnsiteSignatureData(),"Onsite guarantor signature",h,320,140);
    addImageData(d,g.getRemoteSignatureData(),"Remote guarantor signature",h,320,140);
    if(g.getRemoteSignedAt()!=null) d.add(new Paragraph("Remote signed at: "+g.getRemoteSignedAt()));
   }

   d.add(new Paragraph("PART E — SAHIHI, UTHIBITISHO NA WADAU",h));
   d.add(new Paragraph("Mkopaji: ______________________________  Tarehe/Muda: ______________________________"));
   d.add(new Paragraph("Lender/Authorized officer: ______________________________  Tarehe/Muda: ______________________________"));
   d.add(new Paragraph("Witness: ______________________________  ID: ______________________________"));
   d.add(new Paragraph("Commissioner/Notary (ikiwa inahitajika): ______________________________"));
   d.add(new Paragraph(" "));   d.add(new Paragraph("Digital signatures",h));
   var sigs=signatures.findByLoan(l);
   if(sigs.isEmpty()) d.add(new Paragraph("Hakuna sahihi za account zilizorekodiwa bado."));
   sigs.forEach(s->d.add(new Paragraph(s.getSignatureType()+": "+s.getUser().getFullName()+" | signed "+s.getSignedAt())));
   d.add(new Paragraph(" "));
   d.add(new Paragraph("This agreement records the loan terms and evidence captured in JmkLoanApp. Applicable Tanzanian lending, payment, consumer-protection and data-protection requirements remain the responsibility of the licensed/approved lender."));
   d.close(); return out.toByteArray();
  }catch(Exception e){throw new RuntimeException("PDF haikuundwa",e);}
 }

 private void addImageData(Document d,String data,String title,Font h,float maxW,float maxH){
  if(data==null || !data.startsWith("data:image/")) return;
  try{
   int comma=data.indexOf(',');
   if(comma<0) return;
   byte[] bytes=Base64.getDecoder().decode(data.substring(comma+1));
   Image img=Image.getInstance(bytes); img.scaleToFit(maxW,maxH);
   d.add(new Paragraph(title,h)); d.add(img);
  }catch(Exception ignored){}
 }
}
