package com.loanapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "announcements")
public class Announcement {
 @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
 @Column(nullable = false, length = 200) private String title;
 @Column(nullable = false, columnDefinition = "TEXT") private String content;
 @Column(length = 50) private String tag;
 @Column(length = 100) private String color;
 @Column(length = 20) private String emoji;
 @Column(length = 100) private String ctaText;
 @Column(length = 200) private String ctaLink;
 @Column(nullable = false) private Boolean active = true;
 @Column(nullable = false) private Integer displayOrder = 0;
 @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "created_by") private User createdBy;
 @Column(nullable = false, updatable = false) private LocalDateTime createdAt;
 private LocalDateTime updatedAt;
 public Announcement() {}
 public Announcement(Long id,String title,String content,String tag,String color,String emoji,String ctaText,String ctaLink,Boolean active,Integer displayOrder,User createdBy,LocalDateTime createdAt,LocalDateTime updatedAt){this.id=id;this.title=title;this.content=content;this.tag=tag;this.color=color;this.emoji=emoji;this.ctaText=ctaText;this.ctaLink=ctaLink;this.active=active;this.displayOrder=displayOrder;this.createdBy=createdBy;this.createdAt=createdAt;this.updatedAt=updatedAt;}
 public static Builder builder(){return new Builder();}
 public static class Builder { private final Announcement x=new Announcement(); public Builder id(Long v){x.id=v;return this;} public Builder title(String v){x.title=v;return this;} public Builder content(String v){x.content=v;return this;} public Builder tag(String v){x.tag=v;return this;} public Builder color(String v){x.color=v;return this;} public Builder emoji(String v){x.emoji=v;return this;} public Builder ctaText(String v){x.ctaText=v;return this;} public Builder ctaLink(String v){x.ctaLink=v;return this;} public Builder active(Boolean v){x.active=v;return this;} public Builder displayOrder(Integer v){x.displayOrder=v;return this;} public Builder createdBy(User v){x.createdBy=v;return this;} public Builder createdAt(LocalDateTime v){x.createdAt=v;return this;} public Builder updatedAt(LocalDateTime v){x.updatedAt=v;return this;} public Announcement build(){return x;} }
 public Long getId(){return id;} public void setId(Long v){id=v;} public String getTitle(){return title;} public void setTitle(String v){title=v;} public String getContent(){return content;} public void setContent(String v){content=v;} public String getTag(){return tag;} public void setTag(String v){tag=v;} public String getColor(){return color;} public void setColor(String v){color=v;} public String getEmoji(){return emoji;} public void setEmoji(String v){emoji=v;} public String getCtaText(){return ctaText;} public void setCtaText(String v){ctaText=v;} public String getCtaLink(){return ctaLink;} public void setCtaLink(String v){ctaLink=v;} public Boolean getActive(){return active;} public void setActive(Boolean v){active=v;} public Integer getDisplayOrder(){return displayOrder;} public void setDisplayOrder(Integer v){displayOrder=v;} public User getCreatedBy(){return createdBy;} public void setCreatedBy(User v){createdBy=v;} public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;} public LocalDateTime getUpdatedAt(){return updatedAt;} public void setUpdatedAt(LocalDateTime v){updatedAt=v;}
 @PrePersist protected void onCreate(){createdAt=LocalDateTime.now();updatedAt=LocalDateTime.now();}
 @PreUpdate protected void onUpdate(){updatedAt=LocalDateTime.now();}
}