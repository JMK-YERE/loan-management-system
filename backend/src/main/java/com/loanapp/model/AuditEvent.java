package com.loanapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="audit_events", indexes={@Index(name="idx_audit_created",columnList="created_at"),@Index(name="idx_audit_actor",columnList="actor_email")})
public class AuditEvent {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false) private LocalDateTime createdAt;
 @Column(length=150) private String actorEmail;
 @Column(length=30) private String actorRole;
 @Column(nullable=false,length=80) private String action;
 @Column(length=80) private String entityType;
 private Long entityId;
 @Column(length=45) private String ipAddress;
 @Column(length=500) private String userAgent;
 @Column(columnDefinition="TEXT") private String details;
 public AuditEvent() {}
 public AuditEvent(Long id, LocalDateTime createdAt, String actorEmail, String actorRole, String action, String entityType, Long entityId, String ipAddress, String userAgent, String details) { this.id=id; this.createdAt=createdAt; this.actorEmail=actorEmail; this.actorRole=actorRole; this.action=action; this.entityType=entityType; this.entityId=entityId; this.ipAddress=ipAddress; this.userAgent=userAgent; this.details=details; }
 public static Builder builder(){return new Builder();}
 public static class Builder { private final AuditEvent x=new AuditEvent(); public Builder id(Long v){x.id=v;return this;} public Builder createdAt(LocalDateTime v){x.createdAt=v;return this;} public Builder actorEmail(String v){x.actorEmail=v;return this;} public Builder actorRole(String v){x.actorRole=v;return this;} public Builder action(String v){x.action=v;return this;} public Builder entityType(String v){x.entityType=v;return this;} public Builder entityId(Long v){x.entityId=v;return this;} public Builder ipAddress(String v){x.ipAddress=v;return this;} public Builder userAgent(String v){x.userAgent=v;return this;} public Builder details(String v){x.details=v;return this;} public AuditEvent build(){return x;} }
 public Long getId(){return id;} public void setId(Long v){id=v;} public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;} public String getActorEmail(){return actorEmail;} public void setActorEmail(String v){actorEmail=v;} public String getActorRole(){return actorRole;} public void setActorRole(String v){actorRole=v;} public String getAction(){return action;} public void setAction(String v){action=v;} public String getEntityType(){return entityType;} public void setEntityType(String v){entityType=v;} public Long getEntityId(){return entityId;} public void setEntityId(Long v){entityId=v;} public String getIpAddress(){return ipAddress;} public void setIpAddress(String v){ipAddress=v;} public String getUserAgent(){return userAgent;} public void setUserAgent(String v){userAgent=v;} public String getDetails(){return details;} public void setDetails(String v){details=v;}
 @PrePersist void onCreate(){if(createdAt==null) createdAt=LocalDateTime.now();}
}