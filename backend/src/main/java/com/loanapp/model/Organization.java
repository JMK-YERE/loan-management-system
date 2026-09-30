package com.loanapp.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="organizations",uniqueConstraints=@UniqueConstraint(columnNames="code"))
public class Organization {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=180) private String name;
 @Column(nullable=false,length=40) private String code;
 @Column(length=50) private String licenseNumber;
 @Column(length=30) private String country="Tanzania";
 @Column(length=10) private String currency="TZS";
 @Column(length=10) private String defaultLanguage="sw";
 @Column(nullable=false) private Boolean active=true;
 @Column(nullable=false,updatable=false) private LocalDateTime createdAt;
 public Organization(){} public Organization(String n,String c,String l){name=n;code=c;licenseNumber=l;}
 @PrePersist void create(){createdAt=LocalDateTime.now();}
 public Long getId(){return id;} public String getName(){return name;} public void setName(String v){name=v;} public String getCode(){return code;} public void setCode(String v){code=v;} public String getLicenseNumber(){return licenseNumber;} public void setLicenseNumber(String v){licenseNumber=v;} public String getCountry(){return country;} public String getCurrency(){return currency;} public String getDefaultLanguage(){return defaultLanguage;} public Boolean getActive(){return active;} public LocalDateTime getCreatedAt(){return createdAt;}
}
