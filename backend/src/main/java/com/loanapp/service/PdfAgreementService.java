package com.loanapp.service;

import com.loanapp.model.Guarantor;
import com.loanapp.model.Loan;
import com.loanapp.model.User;
import com.loanapp.repository.GuarantorRepository;
import com.loanapp.repository.LoanRepository;
import com.loanapp.repository.SignatureRepository;
import com.loanapp.repository.UserRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import org.springframework.stereotype.Service;

@Service
public class PdfAgreementService {
 private final LoanRepository loans; private final UserRepository users; private final SignatureRepository signatures; private final GuarantorRepository guarantors;
 public PdfAgreementService(LoanRepository loans,UserRepository users,SignatureRepository signatures,GuarantorRepository guarantors){this.loans=loans;this.users=users;this.signatures=signatures;this.guarantors=guarantors;}

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
   d.add(new Paragraph("Status: "+l.getStatus()));
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
   }

   d.add(new Paragraph("Digital signatures",h));
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
