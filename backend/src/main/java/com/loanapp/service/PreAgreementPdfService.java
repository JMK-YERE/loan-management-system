package com.loanapp.service;

import com.loanapp.dto.LoanQuoteRequest;
import com.loanapp.dto.LoanQuoteResponse;
import com.loanapp.model.User;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;

@Service
public class PreAgreementPdfService {
    private final LoanQuoteService quotes;
    @Value("${app.company.name:JmkLoanApp}") private String companyName;
    @Value("${app.company.address:}") private String companyAddress;
    @Value("${app.company.phone:}") private String companyPhone;
    @Value("${app.company.email:}") private String companyEmail;
    @Value("${app.company.license:}") private String companyLicense;

    public PreAgreementPdfService(LoanQuoteService quotes){this.quotes=quotes;}

    public byte[] generate(LoanQuoteRequest request, User borrower, String purpose){
        LoanQuoteResponse q=quotes.quote(request);
        try{
            ByteArrayOutputStream out=new ByteArrayOutputStream();
            Document doc=new Document(PageSize.A4,40,40,45,45);
            PdfWriter.getInstance(doc,out);
            doc.open();

            Font title=FontFactory.getFont(FontFactory.HELVETICA_BOLD,18);
            Font h=FontFactory.getFont(FontFactory.HELVETICA_BOLD,12);
            Font small=FontFactory.getFont(FontFactory.HELVETICA,8);

            doc.add(new Paragraph(safe(companyName)+" — MKATABA WA MKOPO",title));
            doc.add(new Paragraph(safe(companyAddress)+" · "+safe(companyPhone)+" · "+safe(companyEmail)+" · Licence: "+safe(companyLicense)));
            doc.add(new Paragraph("PRE-AGREEMENT / LOAN OFFER PREVIEW"));
            doc.add(new Paragraph("Hii ni preview ya masharti yatakayotumika kwenye offer. Taarifa za borrower zimechukuliwa moja kwa moja kwenye akaunti yake ya JmkLoanApp."));
            doc.add(Chunk.NEWLINE);

            doc.add(new Paragraph("1. TAARIFA KAMILI ZA MKOPAJI",h));
            doc.add(new Paragraph("Jina kamili: "+safe(borrower.getFullName())));
            doc.add(new Paragraph("Email: "+safe(borrower.getEmail())+" | Simu: "+safe(borrower.getPhone())));
            doc.add(new Paragraph("Aina ya ID: "+safe(borrower.getIdType())+" | NIDA/ID: "+safe(borrower.getNidaNumber())));
            doc.add(new Paragraph("Tarehe ya kuzaliwa: "+safe(String.valueOf(borrower.getDateOfBirth()))+" | Jinsia: "+safe(borrower.getGender())+" | Hali ya ndoa: "+safe(borrower.getMaritalStatus())));
            doc.add(new Paragraph("Uraia: "+safe(borrower.getNationality())+" | Nchi: "+safe(borrower.getCountry())));
            doc.add(new Paragraph("Anwani: "+safe(borrower.getAddress())+" | Mji: "+safe(borrower.getCity())));
            doc.add(new Paragraph("Ajira: "+safe(borrower.getEmploymentStatus())+" | Kazi: "+safe(borrower.getOccupation())+" | Mwajiri: "+safe(borrower.getEmployer())));
            doc.add(new Paragraph("Mapato ya mwezi: "+money(borrower.getMonthlyIncome())));
            doc.add(new Paragraph("Next of kin: "+safe(borrower.getKinName())+" | Simu: "+safe(borrower.getKinPhone())+" | Uhusiano: "+safe(borrower.getKinRelationship())));
            doc.add(Chunk.NEWLINE);

            doc.add(new Paragraph("2. TAARIFA ZA MAOMBI",h));
            doc.add(new Paragraph("Loan product: "+safe(q.productName)+" | Loan type: "+safe(q.loanType)));
            doc.add(new Paragraph("Dhumuni la mkopo: "+safe(purpose)));
            doc.add(new Paragraph("Kiasi kinachoombwa (Principal): "+money(q.principal)));
            doc.add(new Paragraph("Muda: "+request.getDuration()+" "+safe(q.durationUnit)));
            doc.add(new Paragraph("Gharama za mwezi zilizotolewa na borrower: "+money(request.getMonthlyExpenses())));
            doc.add(new Paragraph("Madeni ya mwezi yaliyotolewa na borrower: "+money(request.getExistingMonthlyDebt())));
            doc.add(new Paragraph("Dhamana: "+safe(request.getCollateralDescription())+" | Thamani: "+money(request.getCollateralValue())));
            doc.add(Chunk.NEWLINE);

            doc.add(new Paragraph("3. MASHARTI YALIYOWEKWA NA TAASISI",h));
            doc.add(new Paragraph("Interest rate: "+safe(rateText(q, request))));
            doc.add(new Paragraph("Interest method: "+safe(q.interestType)));
            doc.add(new Paragraph("Interest amount: "+money(q.interest)));
            doc.add(new Paragraph("Processing fee: "+money(q.processingFee)));
            doc.add(new Paragraph("Other charges: "+money(q.otherCharges)));
            doc.add(new Paragraph("Late fee / penalty: "+money(q.lateFee)));
            doc.add(new Paragraph("Grace period: "+q.gracePeriodDays+" day(s)"));
            doc.add(new Paragraph("Repayment frequency: "+safe(q.repaymentFrequency)));
            doc.add(new Paragraph("Installment count: "+q.installmentCount));
            doc.add(new Paragraph("Installment amount: "+money(q.installmentAmount)));
            doc.add(new Paragraph("TOTAL REPAYMENT: "+money(q.totalRepayment)));
            doc.add(new Paragraph("Currency: "+safe(q.currency)));
            doc.add(new Paragraph("Terms version: "+safe(q.termsVersion)));
            doc.add(Chunk.NEWLINE);

            doc.add(new Paragraph("4. RATIBA YA MALIPO",h));
            DateTimeFormatter f=DateTimeFormatter.ISO_DATE;
            if(q.dueDates!=null){
                for(int i=0;i<q.dueDates.size();i++){
                    doc.add(new Paragraph((i+1)+". Due: "+q.dueDates.get(i).format(f)+" — "+money(q.installmentAmount)));
                }
            }
            doc.add(Chunk.NEWLINE);

            doc.add(new Paragraph("5. TAMKO LA MKOPAJI",h));
            doc.add(new Paragraph("Mkopaji anathibitisha kuwa amepewa nafasi ya kusoma kiasi, riba, ada, penalty, jumla ya marejesho, ratiba na masharti ya Loan Product kabla ya kutuma ombi."));
            doc.add(new Paragraph("Riba, ada, penalty na jumla ya kurejesha huamuliwa na Loan Product ya taasisi na quotation ya server. Borrower hawezi kuzi-edit."));
            doc.add(new Paragraph("Hii ni preview; approval, guarantor/collateral verification, signatures na final agreement vitafuata workflow ya lender."));

            doc.add(new Paragraph("6. SAHIHI / CONSENT",h));
            doc.add(new Paragraph("Mkopaji: "+safe(borrower.getFullName())));
            doc.add(new Paragraph("Consent ya kusoma masharti: ____________________  Tarehe: ____________________"));
            doc.add(new Paragraph("Lender/Authorized officer: _______________________  Tarehe: ____________________"));
            doc.add(new Paragraph("System generated preview · JmkLoanApp"));

            doc.close();
            return out.toByteArray();
        }catch(Exception e){throw new RuntimeException("Imeshindikana kutengeneza PDF ya masharti",e);}
    }

    private String rateText(LoanQuoteResponse q,LoanQuoteRequest r){
        if(q.interestType==null)return "";
        return q.interestType+" · product rate used by institution";
    }
    private String safe(String s){return s==null||"null".equals(s)?"—":s;}
    private String money(BigDecimal v){return "TZS "+(v==null?BigDecimal.ZERO:v.setScale(2,java.math.RoundingMode.HALF_UP).toPlainString());}
}
