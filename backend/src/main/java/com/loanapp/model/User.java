package com.loanapp.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String fullName;

    @Column(unique = true, nullable = false, length = 150)
    private String email;

    @Column(unique = true, nullable = false, length = 20)
    private String phone;

    @Column(nullable = true)
    private String password;

    @Column(name = "national_id", unique = true, length = 50)
    private String nidaNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status = UserStatus.PENDING;

    @Column(unique = true, length = 100)
    private String verificationToken;

    private LocalDateTime tokenExpiry;

    @Column(length = 500)
    private String profilePicture;

    @Column(length = 500)
    private String rejectionReason;

    @Column(nullable = false)
    private Boolean active = false;

    @Column(length = 30) private String idType;
    private LocalDate dateOfBirth;
    @Column(length = 10) private String gender;
    @Column(length = 20) private String maritalStatus;
    @Column(length = 60) private String nationality;
    @Column(length = 200) private String address;
    @Column(length = 80) private String city;
    @Column(length = 80) private String country;
    @Column(length = 30) private String employmentStatus;
    @Column(length = 120) private String occupation;
    @Column(length = 150) private String employer;
    private BigDecimal monthlyIncome;
    @Column(length = 200) private String kinName;
    @Column(length = 20) private String kinPhone;
    @Column(length = 40) private String kinRelationship;
    @Column(columnDefinition = "TEXT") private String photoData;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public User() {}

    public User(Long id, String fullName, String email, String phone, String password, String nidaNumber,
                Role role, UserStatus status, String verificationToken, LocalDateTime tokenExpiry,
                String profilePicture, String rejectionReason, Boolean active, String idType,
                LocalDate dateOfBirth, String gender, String maritalStatus, String nationality,
                String address, String city, String country, String employmentStatus, String occupation,
                String employer, BigDecimal monthlyIncome, String kinName, String kinPhone,
                String kinRelationship, String photoData, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id=id; this.fullName=fullName; this.email=email; this.phone=phone; this.password=password;
        this.nidaNumber=nidaNumber; this.role=role; this.status=status; this.verificationToken=verificationToken;
        this.tokenExpiry=tokenExpiry; this.profilePicture=profilePicture; this.rejectionReason=rejectionReason;
        this.active=active; this.idType=idType; this.dateOfBirth=dateOfBirth; this.gender=gender;
        this.maritalStatus=maritalStatus; this.nationality=nationality; this.address=address; this.city=city;
        this.country=country; this.employmentStatus=employmentStatus; this.occupation=occupation;
        this.employer=employer; this.monthlyIncome=monthlyIncome; this.kinName=kinName; this.kinPhone=kinPhone;
        this.kinRelationship=kinRelationship; this.photoData=photoData; this.createdAt=createdAt; this.updatedAt=updatedAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final User u = new User();
        public Builder id(Long v){u.id=v;return this;} public Builder fullName(String v){u.fullName=v;return this;}
        public Builder email(String v){u.email=v;return this;} public Builder phone(String v){u.phone=v;return this;}
        public Builder password(String v){u.password=v;return this;} public Builder nidaNumber(String v){u.nidaNumber=v;return this;}
        public Builder role(Role v){u.role=v;return this;} public Builder status(UserStatus v){u.status=v;return this;}
        public Builder verificationToken(String v){u.verificationToken=v;return this;} public Builder tokenExpiry(LocalDateTime v){u.tokenExpiry=v;return this;}
        public Builder profilePicture(String v){u.profilePicture=v;return this;} public Builder rejectionReason(String v){u.rejectionReason=v;return this;}
        public Builder active(Boolean v){u.active=v;return this;} public Builder idType(String v){u.idType=v;return this;}
        public Builder dateOfBirth(LocalDate v){u.dateOfBirth=v;return this;} public Builder gender(String v){u.gender=v;return this;}
        public Builder maritalStatus(String v){u.maritalStatus=v;return this;} public Builder nationality(String v){u.nationality=v;return this;}
        public Builder address(String v){u.address=v;return this;} public Builder city(String v){u.city=v;return this;}
        public Builder country(String v){u.country=v;return this;} public Builder employmentStatus(String v){u.employmentStatus=v;return this;}
        public Builder occupation(String v){u.occupation=v;return this;} public Builder employer(String v){u.employer=v;return this;}
        public Builder monthlyIncome(BigDecimal v){u.monthlyIncome=v;return this;} public Builder kinName(String v){u.kinName=v;return this;}
        public Builder kinPhone(String v){u.kinPhone=v;return this;} public Builder kinRelationship(String v){u.kinRelationship=v;return this;}
        public Builder photoData(String v){u.photoData=v;return this;} public Builder createdAt(LocalDateTime v){u.createdAt=v;return this;}
        public Builder updatedAt(LocalDateTime v){u.updatedAt=v;return this;} public User build(){return u;}
    }

    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getFullName(){return fullName;} public void setFullName(String v){fullName=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
    public String getPassword(){return password;} public void setPassword(String v){password=v;}
    public String getNidaNumber(){return nidaNumber;} public void setNidaNumber(String v){nidaNumber=v;}
    public Role getRole(){return role;} public void setRole(Role v){role=v;}
    public UserStatus getStatus(){return status;} public void setStatus(UserStatus v){status=v;}
    public String getVerificationToken(){return verificationToken;} public void setVerificationToken(String v){verificationToken=v;}
    public LocalDateTime getTokenExpiry(){return tokenExpiry;} public void setTokenExpiry(LocalDateTime v){tokenExpiry=v;}
    public String getProfilePicture(){return profilePicture;} public void setProfilePicture(String v){profilePicture=v;}
    public String getRejectionReason(){return rejectionReason;} public void setRejectionReason(String v){rejectionReason=v;}
    public Boolean getActive(){return active;} public void setActive(Boolean v){active=v;}
    public String getIdType(){return idType;} public void setIdType(String v){idType=v;}
    public LocalDate getDateOfBirth(){return dateOfBirth;} public void setDateOfBirth(LocalDate v){dateOfBirth=v;}
    public String getGender(){return gender;} public void setGender(String v){gender=v;}
    public String getMaritalStatus(){return maritalStatus;} public void setMaritalStatus(String v){maritalStatus=v;}
    public String getNationality(){return nationality;} public void setNationality(String v){nationality=v;}
    public String getAddress(){return address;} public void setAddress(String v){address=v;}
    public String getCity(){return city;} public void setCity(String v){city=v;}
    public String getCountry(){return country;} public void setCountry(String v){country=v;}
    public String getEmploymentStatus(){return employmentStatus;} public void setEmploymentStatus(String v){employmentStatus=v;}
    public String getOccupation(){return occupation;} public void setOccupation(String v){occupation=v;}
    public String getEmployer(){return employer;} public void setEmployer(String v){employer=v;}
    public BigDecimal getMonthlyIncome(){return monthlyIncome;} public void setMonthlyIncome(BigDecimal v){monthlyIncome=v;}
    public String getKinName(){return kinName;} public void setKinName(String v){kinName=v;}
    public String getKinPhone(){return kinPhone;} public void setKinPhone(String v){kinPhone=v;}
    public String getKinRelationship(){return kinRelationship;} public void setKinRelationship(String v){kinRelationship=v;}
    public String getPhotoData(){return photoData;} public void setPhotoData(String v){photoData=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
    public LocalDateTime getUpdatedAt(){return updatedAt;} public void setUpdatedAt(LocalDateTime v){updatedAt=v;}

    @PrePersist
    protected void onCreate() { createdAt=LocalDateTime.now(); updatedAt=LocalDateTime.now(); }

    @PreUpdate
    protected void onUpdate() { updatedAt=LocalDateTime.now(); }

    public enum Role { LENDER, BORROWER, GUARANTOR, ADMIN }
    public enum UserStatus { PENDING, APPROVED, REJECTED }
}
