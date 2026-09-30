package com.loanapp.model;
import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity @Table(name="journal_entry_lines")
public class JournalEntryLine {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="journal_entry_id") private JournalEntry entry;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="account_id") private Account account;
 @Column(nullable=false,precision=15,scale=2) private BigDecimal debit=BigDecimal.ZERO;
 @Column(nullable=false,precision=15,scale=2) private BigDecimal credit=BigDecimal.ZERO;
 @Column(length=300) private String memo;
 public JournalEntryLine(){} public JournalEntryLine(JournalEntry e,Account a,BigDecimal d,BigDecimal c,String m){entry=e;account=a;debit=d;credit=c;memo=m;}
 public Long getId(){return id;} public JournalEntry getEntry(){return entry;} public Account getAccount(){return account;} public BigDecimal getDebit(){return debit;} public BigDecimal getCredit(){return credit;} public String getMemo(){return memo;}
}
