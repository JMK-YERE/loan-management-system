package com.loanapp.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="branches",uniqueConstraints=@UniqueConstraint(columnNames={"organization_id","code"}))
public class Branch {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="organization_id") private Organization organization;
 @Column(nullable=false,length=150) private String name;
 @Column(nullable=false,length=30) private String code;
 @Column(length=120) private String city;
 @Column(length=250) private String address;
 @Column(nullable=false) private Boolean active=true;
 @Column(nullable=false,updatable=false) private LocalDateTime createdAt;
 public Branch(){} public Branch(Organization o,String n,String c,String city,String address){organization=o;name=n;code=c;this.city=city;this.address=address;}
 @PrePersist void create(){createdAt=LocalDateTime.now();}
 public Long getId(){return id;} public Organization getOrganization(){return organization;} public String getName(){return name;} public String getCode(){return code;} public String getCity(){return city;} public String getAddress(){return address;} public Boolean getActive(){return active;} public LocalDateTime getCreatedAt(){return createdAt;}
}
