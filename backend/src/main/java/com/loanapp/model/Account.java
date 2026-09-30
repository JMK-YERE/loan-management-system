package com.loanapp.model;
import jakarta.persistence.*;
@Entity @Table(name="accounts",uniqueConstraints=@UniqueConstraint(columnNames="code"))
public class Account {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=30) private String code;
 @Column(nullable=false,length=150) private String name;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private Type type;
 @Column(nullable=false) private Boolean active=true;
 public Account(){} public Account(String c,String n,Type t){code=c;name=n;type=t;}
 public Long getId(){return id;} public String getCode(){return code;} public String getName(){return name;} public Type getType(){return type;} public Boolean getActive(){return active;} public enum Type{ASSET,LIABILITY,EQUITY,REVENUE,EXPENSE}
}
