package com.loanapp.service;

import com.loanapp.model.LoanApplication;
import com.loanapp.model.User;
import com.loanapp.repository.LoanApplicationRepository;
import com.loanapp.repository.UserRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;

@Service
public class ApplicationAgreementPdfService {
 private final LoanApplicationRepository apps; private final UserRepository users;
 public ApplicationAgreementPdfService(LoanApplicationRepository apps,UserRepository users){this.apps=apps;this.users=users;}
 public byte[] generate(Long id,String email){
  LoanApplication a=apps.findById(id).orElseThrow(()->new RuntimeException("Ombi halijapatikana"));User u=users.findByEmail(email).orElseThrow();
  if(!a.getBorrower().getId().equals(u.getId())&&u.getRole()!=User.Role.ADMIN&&a.getLender()!=null&&!a.getLender().getId().equals(u.getId()))throw new RuntimeException("Huna ruhusa");
  if(a.getProduct()==null)throw new RuntimeException("Agreement bado haijaandaliwa; lender lazima aweke offer kwanza");
  try{ByteArrayOutputStream out=new ByteArrayOutputStream();Document d=new Document();PdfWriter.getInstance(d,out);d.open();Font t=FontFactory.getFont(FontFactory.HELVETICA_BOLD,18);Font h=FontFactory.getFont(FontFactory.HELVETICA_BOLD,12);
   d.add(new Paragraph("JMK LOAN - PRE-CONTRACT LOAN OFFER",t));d.add(new Paragraph("Mkataba/Offer ya Mkopo"));d.add(new Paragraph("Application #"+a.getId(),h));d.add(new Paragraph("Borrower: "+a.getBorrower().getFullName()));
   d.add(new Paragraph("Email: "+safe(a.getBorrower().getEmail())+" | Simu: "+safe(a.getBorrower().getPhone())));
   d.add(new Paragraph("ID type: "+safe(a.getBorrower().getIdType())+" | NIDA/ID: "+safe(a.getBorrower().getNidaNumber())));
   d.add(new Paragraph("Tarehe ya kuzaliwa: "+(a.getBorrower().getDateOfBirth()==null?"—":a.getBorrower().getDateOfBirth())+" | Jinsia: "+safe(a.getBorrower().getGender())+" | Hali ya ndoa: "+safe(a.getBorrower().getMaritalStatus())));
   d.add(new Paragraph("Uraia: "+safe(a.getBorrower().getNationality())+" | Nchi: "+safe(a.getBorrower().getCountry())));
   d.add(new Paragraph("Anwani: "+safe(a.getBorrower().getAddress())+" | Mji: "+safe(a.getBorrower().getCity())));
   d.add(new Paragraph("Ajira: "+safe(a.getBorrower().getEmploymentStatus())+" | Kazi: "+safe(a.getBorrower().getOccupation())+" | Mwajiri: "+safe(a.getBorrower().getEmployer())));
   d.add(new Paragraph("Mapato ya mwezi: TZS "+(a.getBorrower().getMonthlyIncome()==null?"0":a.getBorrower().getMonthlyIncome())));
   d.add(new Paragraph("Next of kin: "+safe(a.getBorrower().getKinName())+" | Simu: "+safe(a.getBorrower().getKinPhone())+" | Uhusiano: "+safe(a.getBorrower().getKinRelationship())));d.add(new Paragraph("Product: "+a.getProduct().getName()));d.add(new Paragraph("Principal: TZS "+a.getAmount()));d.add(new Paragraph("Duration: "+a.getDuration()+" "+a.getDurationUnit()));d.add(new Paragraph("Interest: "+a.getInterestSnapshot()));d.add(new Paragraph("Processing fee: TZS "+a.getProcessingFeeSnapshot()));d.add(new Paragraph("Late fee: TZS "+a.getLateFeeSnapshot()));d.add(new Paragraph("Grace period: "+a.getGracePeriodDaysSnapshot()+" day(s)"));d.add(new Paragraph("Total repayment: TZS "+a.getTotalRepaymentSnapshot()));d.add(new Paragraph("Installment: TZS "+a.getInstallmentAmountSnapshot()+" x "+a.getInstallmentCountSnapshot()));d.add(new Paragraph("Repayment frequency: "+a.getRepaymentFrequencySnapshot()));d.add(new Paragraph("Purpose: "+a.getPurpose()));d.add(new Paragraph("Terms version: "+a.getTermsVersion()));d.add(new Paragraph(""));d.add(new Paragraph("Borrower must review and accept these terms before approval. Where a guarantor is required, the approved guarantor must review and sign the final agreement before disbursement."));d.close();return out.toByteArray();
  }catch(Exception e){throw new RuntimeException("PDF haikuundwa",e);}
 }
 private String safe(String s){return s==null||s.isBlank()?"—":s;}
}
