package com.loanapp.service;

import com.loanapp.model.*;
import com.loanapp.repository.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

@Service
public class GuarantorAgreementPdfService {
    private final LoanRepository loans;
    private final UserRepository users;
    private final GuarantorRepository guarantors;
    private final SignatureRepository signatures;
    private final RepaymentScheduleRepository schedules;

    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public GuarantorAgreementPdfService(
            LoanRepository loans, UserRepository users, GuarantorRepository guarantors,
            SignatureRepository signatures, RepaymentScheduleRepository schedules) {
        this.loans=loans; this.users=users; this.guarantors=guarantors;
        this.signatures=signatures; this.schedules=schedules;
    }

    public byte[] generate(Long id,String email){
        Loan l=loans.findById(id).orElseThrow(()->new RuntimeException("Mkopo haujapatikana"));
        User u=users.findByEmail(email).orElseThrow(()->new RuntimeException("Mtumiaji hajapatikana"));
        List<Guarantor> gs=guarantors.findByLoan(l);
        boolean party=u.getRole()==User.Role.ADMIN
                || (l.getBorrower()!=null&&l.getBorrower().getId().equals(u.getId()))
                || (l.getLender()!=null&&l.getLender().getId().equals(u.getId()));
        if(!party) throw new RuntimeException("Huna ruhusa ya kuona agreement hii");

        try {
            ByteArrayOutputStream out=new ByteArrayOutputStream();
            Document d=new Document(PageSize.A4,42,42,42,42);
            PdfWriter writer=PdfWriter.getInstance(d,out);
            d.addTitle("JMK LOAN AGREEMENT - "+agreementNo(l));
            d.addAuthor("JMK Loan App");
            d.addSubject("Digital Loan Agreement");
            d.open();

            Font title=FontFactory.getFont(FontFactory.HELVETICA_BOLD,18);
            Font h1=FontFactory.getFont(FontFactory.HELVETICA_BOLD,13);
            Font h2=FontFactory.getFont(FontFactory.HELVETICA_BOLD,10);
            Font body=FontFactory.getFont(FontFactory.HELVETICA,9);
            Font small=FontFactory.getFont(FontFactory.HELVETICA,7);
            Font bold=FontFactory.getFont(FontFactory.HELVETICA_BOLD,9);

            Paragraph p=new Paragraph("JMK LOAN APP",title); p.setAlignment(Element.ALIGN_CENTER); d.add(p);
            p=new Paragraph("DIGITAL LOAN AGREEMENT",h1); p.setAlignment(Element.ALIGN_CENTER); d.add(p);
            p=new Paragraph("MKATABA WA MKOPO WA KIDIJITALI",h1); p.setAlignment(Element.ALIGN_CENTER); d.add(p);
            d.add(Chunk.NEWLINE);

            PdfPTable ref=new PdfPTable(2); ref.setWidthPercentage(100); ref.setWidths(new float[]{1,1});
            cell(ref,"Loan ID", "LN-"+String.format("%06d",l.getId()),bold,body);
            cell(ref,"Agreement No.", agreementNo(l),bold,body);
            cell(ref,"Agreement Version","1.0",bold,body);
            cell(ref,"Generated", l.getCreatedAt()==null?"—":l.getCreatedAt().format(DT),bold,body);
            d.add(ref); d.add(Chunk.NEWLINE);

            section(d,"1. MUHTASARI MUHIMU WA MKOPO",h1);
            PdfPTable summary=new PdfPTable(2); summary.setWidthPercentage(100); summary.setWidths(new float[]{1.15f,1});
            row(summary,"Kiasi kilichoidhinishwa",money(l.getAmount()),bold,body);
            BigDecimal fee=nz(l.getProcessingFee()).add(nz(l.getLawyerFee()));
            BigDecimal net=nz(l.getAmount()).subtract(fee).max(BigDecimal.ZERO);
            row(summary,"Makadirio ya kiasi cha kupokea baada ya ada",money(net),bold,body);
            row(summary,"Riba",nz(l.getInterestRate()).stripTrailingZeros().toPlainString()+"%",bold,body);
            row(summary,"Ada ya processing",money(l.getProcessingFee()),bold,body);
            row(summary,"Ada ya mwanasheria",money(l.getLawyerFee()),bold,body);
            row(summary,"Jumla ya kurejesha",money(l.getTotalRepayment()),bold,body);
            row(summary,"Muda",l.getDurationMonths()+" "+(l.getDurationUnit()==null?"MONTHS":l.getDurationUnit().name()),bold,body);
            row(summary,"Malipo yanayofuata",l.getNextDueDate()==null?"—":l.getNextDueDate().format(DATE),bold,body);
            row(summary,"Madhumuni ya mkopo",safe(l.getPurpose()),bold,body);
            d.add(summary);

            section(d,"2. WAHUSIKA WA MKOPO",h1);
            PdfPTable parties=new PdfPTable(2); parties.setWidthPercentage(100);
            row(parties,"Mkopaji",safe(l.getBorrower().getFullName()),bold,body);
            row(parties,"Mkopeshaji",safe(l.getLender().getFullName()),bold,body);
            d.add(parties);

            section(d,"3. MDHAMINI NA DHAMANA YAKE",h1);
            if(gs.isEmpty()) d.add(new Paragraph("Mkopo huu hauna mdhamini aliyesajiliwa.",body));
            else {
                PdfPTable gt=new PdfPTable(4); gt.setWidthPercentage(100); gt.setWidths(new float[]{1.5f,1.1f,1.1f,1});
                header(gt,"Mdhamini",bold); header(gt,"Kiasi cha dhamana",bold); header(gt,"Uhusiano",bold); header(gt,"Hali",bold);
                for(Guarantor g:gs){
                    String name=g.getGuarantor()!=null?g.getGuarantor().getFullName():g.getGuarantorName();
                    row(gt,safe(name),money(g.getGuaranteedAmount()),safe(g.getRelationship()),safe(g.getStatus().name()));
                }
                d.add(gt);
                for(Guarantor g:gs){
                    addImageData(d,g.getGuarantorPhotoData(),"Picha ya mdhamini",small,220,180);
                    addImageData(d,g.getOnsiteSignatureData(),"Sahihi ya mdhamini wa onsite",small,320,140);
                }
                d.add(new Paragraph("Mdhamini anaona kiasi cha mkopo, kiasi anachowajibika kudhamini na masharti ya wajibu wake kabla ya kukubali na kusaini.",small));
            }

            section(d,"4. RATIBA YA MAREJESHO",h1);
            List<RepaymentSchedule> ss=schedules.findByLoanOrderByInstallmentNumberAsc(l);
            if(ss.isEmpty()) d.add(new Paragraph("Ratiba rasmi bado haijazalishwa.",body));
            else {
                PdfPTable st=new PdfPTable(6); st.setWidthPercentage(100);
                header(st,"#",bold); header(st,"Tarehe",bold); header(st,"Principal",bold); header(st,"Riba",bold); header(st,"Ada",bold); header(st,"Jumla",bold);
                for(RepaymentSchedule s:ss){
                    row(st,String.valueOf(s.getInstallmentNumber()),s.getDueDate()==null?"—":s.getDueDate().format(DATE),money(s.getPrincipalDue()),money(s.getInterestDue()),money(s.getFeesDue()),money(s.getAmountDue()));
                }
                d.add(st);
            }

            section(d,"5. MASHARTI MUHIMU YA MKOPO",h1);
            String[] clauses={
                "Mkopaji anakubali kulipa marejesho kwa tarehe na kiasi kilichoainishwa kwenye ratiba ya marejesho.",
                "Riba na ada zilizoonyeshwa kwenye muhtasari huu ndizo masharti yaliyokubaliwa kwa Loan ID hii.",
                "Malipo yaliyochelewa yanaweza kuhusisha penalty/late fee kwa mujibu wa masharti ya bidhaa na sheria/kanuni zinazotumika.",
                "Mkopaji na mdhamini wanapaswa kutoa taarifa sahihi na kuhifadhi nakala ya mkataba huu.",
                "Mdhamini anakubali wajibu wake hadi kiwango cha dhamana kilichoonyeshwa kwenye mkataba, kwa mujibu wa masharti ya mkataba.",
                "Mabadiliko ya kiasi, riba, ada au ratiba baada ya kusainiwa hayatafanywa kimya kimya; amendment/offer mpya na acceptance inayohitajika itatumika.",
                "Mkataba huu ni kumbukumbu ya masharti yaliyokuwa yamehifadhiwa na mfumo wakati wa kusainiwa. Taarifa za sahihi na audit trail huhifadhiwa na mfumo.",
                "Kwa masuala ya kisheria, haki za mlaji, faragha, KYC, ukusanyaji na dhamana, mkataba huu unatakiwa kutumiwa kwa kuzingatia sheria na mahitaji ya mdhibiti husika."
            };
            for(String c:clauses) d.add(new Paragraph("• "+c,body));

            section(d,"6. TAMKO LA MKOPAJI",h1);
            d.add(new Paragraph("Nathibitisha kuwa nimepewa nafasi ya kusoma mkataba huu, nimeelewa kiasi cha mkopo, riba, ada, jumla ya marejesho, ratiba ya malipo na masharti muhimu, na ninakubali masharti kwa mujibu wa taratibu za mfumo.",body));

            section(d,"7. SAHIHI ZA KIDIJITALI",h1);
            PdfPTable sigt=new PdfPTable(3); sigt.setWidthPercentage(100);
            header(sigt,"Mhusika",bold); header(sigt,"Tarehe/Muda",bold); header(sigt,"Hali",bold);
            for(Signature.SignatureType type:Signature.SignatureType.values()){
                signatures.findByLoan(l).stream().filter(s->s.getSignatureType()==type).findFirst().ifPresentOrElse(
                    s->row(sigt,type.name(),s.getSignedAt()==null?"—":s.getSignedAt().format(DT),"SIGNED"),
                    ()->row(sigt,type.name(),"—","NOT SIGNED"));
            }
            d.add(sigt);

            d.add(Chunk.NEWLINE);
            Paragraph verify=new Paragraph("Agreement Reference: "+agreementNo(l)+" | Version: 1.0 | Verification Code: "+verificationCode(l),small);
            verify.setAlignment(Element.ALIGN_CENTER); d.add(verify);
            Paragraph footer=new Paragraph("Nakala hii ya PDF ni ya kusoma/kutunza. Mabadiliko ya mkataba uliokwisha sainiwa yanapaswa kufanyika kupitia mchakato rasmi wa amendment na acceptance.",small);
            footer.setAlignment(Element.ALIGN_CENTER); d.add(footer);

            d.close();
            return out.toByteArray();
        } catch(Exception e) {
            throw new RuntimeException("PDF haikuundwa",e);
        }
    }

    private static String agreementNo(Loan l){return "AGR-"+(l.getCreatedAt()==null?"2026":String.valueOf(l.getCreatedAt().getYear()))+"-"+String.format("%06d",l.getId());}
    private static BigDecimal nz(BigDecimal v){return v==null?BigDecimal.ZERO:v;}
    private static String money(BigDecimal v){return "TZS "+nz(v).setScale(2,RoundingMode.HALF_UP).toPlainString();}
    private static String safe(String v){return v==null||v.isBlank()?"—":v;}
    private static String verificationCode(Loan l){
        try{
            String raw=agreementNo(l)+"|"+nz(l.getAmount())+"|"+nz(l.getInterestRate())+"|"+nz(l.getTotalRepayment())+"|"+safe(l.getPurpose());
            byte[] h=MessageDigest.getInstance("SHA-256").digest(raw.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(h).substring(0,16).toUpperCase();
        }catch(Exception e){return "UNAVAILABLE";}
    }
    private static void addImageData(Document d,String data,String title,Font font,float maxW,float maxH){
        if(data==null || !data.startsWith("data:image/")) return;
        try{
            int comma=data.indexOf(',');
            if(comma<0) return;
            Image img=Image.getInstance(Base64.getDecoder().decode(data.substring(comma+1)));
            img.scaleToFit(maxW,maxH);
            d.add(new Paragraph(title,font));
            d.add(img);
        }catch(Exception ignored){}
    }
    private static void section(Document d,String s,Font f){Paragraph p=new Paragraph(s,f);p.setSpacingBefore(8);p.setSpacingAfter(5);d.add(p);}
    private static void header(PdfPTable t,String s,Font f){PdfPCell c=new PdfPCell(new Phrase(s,f));c.setPadding(5);c.setBackgroundColor(java.awt.Color.LIGHT_GRAY);t.addCell(c);}
    private static void row(PdfPTable t,String... values){for(String v:values){PdfPCell c=new PdfPCell(new Phrase(safe(v),FontFactory.getFont(FontFactory.HELVETICA,8)));c.setPadding(4);t.addCell(c);}}
    private static void row(PdfPTable t,String a,String b,Font f,Font body){row(t,a,b);}
    private static void cell(PdfPTable t,String a,String b,Font f,Font body){row(t,a,b);}
}
