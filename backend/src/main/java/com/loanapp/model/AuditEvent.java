package com.loanapp.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name="audit_events", indexes={@Index(name="idx_audit_created",columnList="created_at"),@Index(name="idx_audit_actor",columnList="actor_email")})
@Data @NoArgsConstructor @AllArgsConstructor @Builder
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
 @PrePersist void onCreate(){if(createdAt==null) createdAt=LocalDateTime.now();}
}
