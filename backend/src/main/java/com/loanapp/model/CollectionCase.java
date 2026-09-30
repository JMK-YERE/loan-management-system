package com.loanapp.model;
import jakarta.persistence.*;
import java.time.*;
@Entity @Table(name="collection_cases")
public class CollectionCase {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="loan_id") private Loan loan;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private Status status=Status.OPEN;
 @Column(length=120) private String assignedTo;
 @Column(length=500) private String notes;
 @Column(nullable=false,updatable=false) private LocalDateTime createdAt;
 private LocalDateTime updatedAt;
 public CollectionCase(){}
 public CollectionCase(Loan l,String assignee,String notes){loan=l;assignedTo=assignee;this.notes=notes;}
 @PrePersist void create(){createdAt=LocalDateTime.now();updatedAt=createdAt;} @PreUpdate void update(){updatedAt=LocalDateTime.now();}
 public Long getId(){return id;} public Loan getLoan(){return loan;} public Status getStatus(){return status;} public void setStatus(Status s){status=s;} public String getAssignedTo(){return assignedTo;} public String getNotes(){return notes;} public LocalDateTime getCreatedAt(){return createdAt;} public enum Status{OPEN,IN_PROGRESS,PROMISE_TO_PAY,ESCALATED,RESOLVED,WRITTEN_OFF}
}
