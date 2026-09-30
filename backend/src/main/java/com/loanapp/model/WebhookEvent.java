package com.loanapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="webhook_events", uniqueConstraints=@UniqueConstraint(name="uk_webhook_event_key", columnNames="event_key"))
public class WebhookEvent {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="event_key", nullable=false, length=180) private String eventKey;
    @Column(nullable=false, length=40) private String eventType;
    @Column(nullable=false) private LocalDateTime processedAt;

    public WebhookEvent() {}
    public WebhookEvent(String eventKey,String eventType){this.eventKey=eventKey;this.eventType=eventType;this.processedAt=LocalDateTime.now();}
    public Long getId(){return id;}
    public String getEventKey(){return eventKey;}
    public String getEventType(){return eventType;}
    public LocalDateTime getProcessedAt(){return processedAt;}
}
