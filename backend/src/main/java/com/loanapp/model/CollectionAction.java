package com.loanapp.model;
import jakarta.persistence.*;
import java.time.*;
@Entity @Table(name="collection_actions")
public class CollectionAction {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="case_id") private CollectionCase collectionCase;
 @Column(nullable=false,length=40) private String actionType;
 @Column(length=1000) private String notes;
 @Column(nullable=false,updatable=false) private LocalDateTime createdAt;
 @Column(length=120) private String actorEmail;
 public CollectionAction(){}
 public CollectionAction(CollectionCase c,String type,String notes,String actor){collectionCase=c;actionType=type;this.notes=notes;actorEmail=actor;}
 @PrePersist void create(){createdAt=LocalDateTime.now();}
 public Long getId(){return id;} public CollectionCase getCollectionCase(){return collectionCase;} public String getActionType(){return actionType;} public String getNotes(){return notes;} public LocalDateTime getCreatedAt(){return createdAt;} public String getActorEmail(){return actorEmail;}
}
