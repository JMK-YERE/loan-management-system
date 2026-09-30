package com.loanapp.model;
import jakarta.persistence.*;
import java.time.*;
@Entity @Table(name="journal_entries")
public class JournalEntry {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true,length=80) private String reference;
 @Column(nullable=false) private LocalDate entryDate;
 @Column(length=500) private String description;
 @Column(nullable=false,updatable=false) private LocalDateTime createdAt;
 public JournalEntry(){} public JournalEntry(String r,LocalDate d,String desc){reference=r;entryDate=d;description=desc;}
 @PrePersist void create(){createdAt=LocalDateTime.now();}
 public Long getId(){return id;} public String getReference(){return reference;} public LocalDate getEntryDate(){return entryDate;} public String getDescription(){return description;} public LocalDateTime getCreatedAt(){return createdAt;}
}
