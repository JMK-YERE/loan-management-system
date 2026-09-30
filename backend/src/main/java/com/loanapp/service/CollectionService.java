package com.loanapp.service;
import com.loanapp.model.*;
import com.loanapp.repository.*;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class CollectionService {
 private final CollectionCaseRepository cases; private final CollectionActionRepository actions; private final LoanRepository loans; private final UserRepository users;
 public CollectionService(CollectionCaseRepository c,CollectionActionRepository a,LoanRepository l,UserRepository u){cases=c;actions=a;loans=l;users=u;}
 public List<CollectionCase> open(){return cases.findByStatusOrderByCreatedAtDesc(CollectionCase.Status.OPEN);}
 public CollectionCase create(Long loanId,String assignee,String notes,String actor){Loan l=loans.findById(loanId).orElseThrow();CollectionCase c=cases.findFirstByLoanOrderByCreatedAtDesc(l).orElseGet(()->cases.save(new CollectionCase(l,assignee,notes))); actions.save(new CollectionAction(c,"CASE_OPENED",notes,actor)); return c;}
 public CollectionAction action(Long id,String type,String notes,String actor){CollectionCase c=cases.findById(id).orElseThrow(); if("PROMISE_TO_PAY".equals(type))c.setStatus(CollectionCase.Status.PROMISE_TO_PAY); else if("ESCALATE".equals(type))c.setStatus(CollectionCase.Status.ESCALATED); else if("RESOLVE".equals(type))c.setStatus(CollectionCase.Status.RESOLVED); else c.setStatus(CollectionCase.Status.IN_PROGRESS); cases.save(c); return actions.save(new CollectionAction(c,type,notes,actor));}
 public List<CollectionAction> history(Long id){return actions.findByCollectionCaseOrderByCreatedAtDesc(cases.findById(id).orElseThrow());}
}
