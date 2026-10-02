package com.loanapp.service;

import com.loanapp.model.*;
import com.loanapp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class AccountingService {
    private final AccountRepository accounts;
    private final JournalEntryRepository entries;
    private final JournalEntryLineRepository lines;

    public AccountingService(AccountRepository accounts,JournalEntryRepository entries,JournalEntryLineRepository lines){
        this.accounts=accounts;this.entries=entries;this.lines=lines;
    }

    @Transactional
    public JournalEntry postPayment(Payment payment){
        String ref="PAYMENT-"+payment.getId();
        if(entries.findByReference(ref).isPresent()) return entries.findByReference(ref).get();
        Account cash=account("CASH","Cash / Mobile & Bank Collections",Account.Type.ASSET);
        Account receivable=account("LOAN_RECEIVABLE","Loan Receivables",Account.Type.ASSET);
        JournalEntry entry=entries.save(new JournalEntry(ref,LocalDate.now(),"Repayment for loan #"+payment.getLoan().getId()));
        lines.save(new JournalEntryLine(entry,cash,payment.getAmount(),BigDecimal.ZERO,"Payment "+payment.getTransactionId()));
        lines.save(new JournalEntryLine(entry,receivable,BigDecimal.ZERO,payment.getAmount(),"Reduce loan receivable"));
        return entry;
    }

    @Transactional
    public JournalEntry postDisbursement(Loan loan){
        String ref="DISBURSE-"+loan.getId();
        if(entries.findByReference(ref).isPresent()) return entries.findByReference(ref).get();
        Account receivable=account("LOAN_RECEIVABLE","Loan Receivables",Account.Type.ASSET);
        Account cash=account("CASH","Cash / Mobile & Bank Collections",Account.Type.ASSET);
        JournalEntry entry=entries.save(new JournalEntry(ref,LocalDate.now(),"Disbursement for loan #"+loan.getId()));
        lines.save(new JournalEntryLine(entry,receivable,loan.getAmount(),BigDecimal.ZERO,"Loan principal disbursed"));
        lines.save(new JournalEntryLine(entry,cash,BigDecimal.ZERO,loan.getAmount(),"Funds released"));
        return entry;
    }

    private Account account(String code,String name,Account.Type type){
        return accounts.findByCode(code).orElseGet(()->accounts.save(new Account(code,name,type)));
    }
}