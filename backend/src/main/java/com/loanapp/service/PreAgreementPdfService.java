package com.loanapp.service;

import com.loanapp.dto.LoanQuoteRequest;
import com.loanapp.dto.LoanQuoteResponse;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PreAgreementPdfService {
    private final LoanQuoteService quotes;
    @Value("${app.company.name:JmkLoanApp}") private String companyName;
    @Value("${app.company.address:}") private String companyAddress;
    @Value("${app.company.phone:}") private String companyPhone;
    @Value("${app.company.email:}") private String companyEmail;
    @Value("${app.company.license:}") private String companyLicense;
    public PreAgreementPdfService(LoanQuoteService quotes){this.quotes=quotes;}

    public byte[] generate(LoanQuoteRequest request,String borrowerName,String purpose){
        LoanQuoteResponse q=quotes.quote(request);
        try{
            ByteArrayOutputStream out=new ByteArrayOutputStream();
            Document doc=new Document(PageSize.A4,40,40,45,45);
            PdfWriter.getInstance(doc,out);
            doc.open();
            Font title=FontFactory.getFont(FontFactory.HELVETICA_BOLD,18);
            Font h=FontFactory.getFont(FontFactory.HELVETICA_BOLD,12);
            doc.add(new Paragraph(safe(companyName)+" — MKATABA WA MKOPO",title));
            doc.add(new Paragraph(safe(companyAddress)+" · "+safe(companyPhone)+" · "+safe(companyEmail)+" · Licence: "+safe(companyLicense)));
            doc.add(new Paragraph("Loan Agreement Preview · Mfumo hujaza taarifa za taasisi, borrower na loan halisi kwenye final agreement."));
            doc.add(Chunk.NEWLINE);
            doc.add(new Paragraph("1. TAARIFA BINAFSI ZA MKOPAJI",h));
            doc.add(new Paragraph("Jina kamili: "+safe(borrowerName)));
            doc.add(new Paragraph("NIDA/ID: ____________________ | Simu: ____________________ | Anwani: ____________________"));
            doc.add(new Paragraph("Loan product: "+safe(q.productName)));
            doc.add(new Paragraph("Dhumuni la mkopo: "+safe(purpose)));
            doc.add(new Paragraph("Dhamana: ______________________________ | Thamani: TZS ____________________"));
            doc.add(new Paragraph("Mdhamini (ikiwa anahitajika): ______________________________ | Liability: TZS ____________________"));
            doc.add(Chunk.NEWLINE);
            doc.add(new Paragraph("2. TAARIFA ZA MKOPO NA MASHARTI",h));
            doc.add(new Paragraph("Principal: TZS "+q.principal));
            doc.add(new Paragraph("Interest: TZS "+q.interest+" ("+q.interestType+")"));
            doc.add(new Paragraph("Processing fee: TZS "+q.processingFee));
            doc.add(new Paragraph("Total repayment: TZS "+q.totalRepayment));
            doc.add(new Paragraph("Installments: "+q.installmentCount+" × TZS "+q.installmentAmount));
            doc.add(new Paragraph("Repayment frequency: "+q.repaymentFrequency));
            doc.add(new Paragraph("Duration: "+request.getDuration()+" "+q.durationUnit));
            doc.add(new Paragraph("Late fee: TZS "+q.lateFee));
            doc.add(new Paragraph("Grace period: "+q.gracePeriodDays+" day(s)"));
            doc.add(Chunk.NEWLINE);
            doc.add(new Paragraph("EXPECTED DUE DATES",h));
            DateTimeFormatter f=DateTimeFormatter.ISO_DATE;
            for(int i=0;i<q.dueDates.size();i++) doc.add(new Paragraph((i+1)+". "+q.dueDates.get(i).format(f)+" — TZS "+q.installmentAmount));
            doc.add(Chunk.NEWLINE);
            doc.add(new Paragraph("Terms version: "+safe(q.termsVersion)));
            doc.add(new Paragraph("3. TAMKO LA MKOPAJI",h));
            doc.add(new Paragraph("Mkopaji anathibitisha kuwa amepata nafasi ya kusoma kiasi, riba, ada, muda, ratiba na masharti ya marejesho. Terms za product zinadhibitiwa na taasisi na borrower hawezi kubadilisha riba, ada au penalty."));
            doc.add(new Paragraph("4. SAHIHI",h));
            doc.add(new Paragraph("Mkopaji: ______________________________  Tarehe: ____________________"));
            doc.add(new Paragraph("Mkopeshaji: ____________________________  Tarehe: ____________________"));
            doc.add(new Paragraph("Shahidi: _______________________________  Tarehe: ____________________"));
            doc.close();
            return out.toByteArray();
        }catch(Exception e){throw new RuntimeException("Imeshindikana kutengeneza PDF ya masharti",e);}
    }
    private String safe(String s){return s==null?"":s;}
}
