package com.loanapp.service;

import com.loanapp.model.*;
import com.loanapp.repository.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;

@Service
public class GuarantorAgreementPdfService {
 private final LoanRepository loans; private final UserRepository users; private final GuarantorRepository guarantors; private final SignatureRepository signatures;
 public GuarantorAgreementPdfService(LoanRepository loans,UserRepository users,GuarantorRepository guarantors,SignatureRepository signatures){this.loans=loans;this.users=users;this.guarantors=guarantors;this.signatures=signatures;}
 public byte[] generate(Long id,String email){
  Loan l=loans.findById(id).orElseThrow(()->new RuntimeException("Mkopo haujapatikana"));User u=users.findByEmail(email).orElseThrow();
  boolean isParty=u.getRole()==User.Role.ADMIN||(l.getBorrower()!=null&&l.getBorrower().getId().equals(u.getId()))||(l.getLender()!=null&&l.getLender().getId().equals(u.getId()))||guarantors.findByLoan(l).stream().anyMatch(g->g.getGuarantor().getId().equals(u.getId()));
  if(!isParty)throw new RuntimeException("Huna ruhusa ya kuona agreement hii");
  try{ByteArrayOutputStream out=new ByteArrayOutputStream();Document d=new Document();PdfWriter.getInstance(d,out);d.open();Font t=FontFactory.getFont(FontFactory.HELVETICA_BOLD,18);Font h=FontFactory.getFont(FontFactory.HELVETICA_BOLD,12);
   d.add(new Paragraph("JMK LOAN - FINAL LOAN AGREEMENT",t));d.add(new Paragraph("Mkataba wa Mwisho wa Mkopo"));d.add(new Paragraph("Loan #"+l.getId(),h));d.add(new Paragraph("Borrower: "+l.getBorrower().getFullName()));d.add(new Paragraph("Lender: "+l.getLender().getFullName()));d.add(new Paragraph("Principal: TZS "+l.getAmount()));d.add(new Paragraph("Interest: "+l.getInterestRate()+"%"));d.add(new Paragraph("Duration: "+l.getDurationMonths()+" "+(l.getDurationUnit()==null?"MONTHS":l.getDurationUnit())));d.add(new Paragraph("Processing fee: TZS "+l.getProcessingFee()));d.add(new Paragraph("Total repayment: TZS "+l.getTotalRepayment()));d.add(new Paragraph("Purpose: "+(l.getPurpose()==null?"—":l.getPurpose())));
   d.add(new Paragraph(""));d.add(new Paragraph("Guarantor(s)",h));guarantors.findByLoan(l).forEach(g->d.add(new Paragraph(g.getGuarantor().getFullName()+" | Guaranteed amount: TZS "+g.getGuaranteedAmount()+" | "+g.getStatus())));
   d.add(new Paragraph(""));d.add(new Paragraph("Signatures",h));signatures.findByLoan(l).forEach(s->d.add(new Paragraph(s.getSignatureType()+": "+s.getUser().getFullName()+" | "+s.getSignedAt())));
   d.add(new Paragraph(""));d.add(new Paragraph("Disbursement is permitted only after the required borrower, lender and approved-guarantor signatures have been recorded. This agreement should be retained and provided to the borrower as applicable."));
   d.close();return out.toByteArray();
  }catch(Exception e){throw new RuntimeException("PDF haikuundwa",e);}
 }
}
