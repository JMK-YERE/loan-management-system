package com.loanapp.service;

import com.loanapp.dto.LoanQuoteRequest;
import com.loanapp.dto.LoanQuoteResponse;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PreAgreementPdfService {
    private final LoanQuoteService quotes;
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
            doc.add(new Paragraph("JmkLoanApp - PRE-APPLICATION LOAN OFFER",title));
            doc.add(new Paragraph("This is a pricing and terms preview. It is not a final approval or disbursement notice."));
            doc.add(Chunk.NEWLINE);
            doc.add(new Paragraph("Borrower: "+safe(borrowerName),h));
            doc.add(new Paragraph("Loan product: "+safe(q.productName)));
            doc.add(new Paragraph("Purpose: "+safe(purpose)));
            doc.add(Chunk.NEWLINE);
            doc.add(new Paragraph("LOAN TERMS",h));
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
            doc.add(new Paragraph("The borrower should review the above pricing before submitting the application. Product terms are controlled by the institution and cannot be edited by the borrower."));
            doc.close();
            return out.toByteArray();
        }catch(Exception e){throw new RuntimeException("Imeshindikana kutengeneza PDF ya masharti",e);}
    }
    private String safe(String s){return s==null?"":s;}
}
