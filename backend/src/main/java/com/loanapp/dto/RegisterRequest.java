package com.loanapp.dto;

import com.loanapp.model.User;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class RegisterRequest {
<<<<<<< Updated upstream
 @NotBlank(message="Jina kamili linahitajika") @Size(min=3,max=200,message="Jina liwe kati ya 3 na 200 herufi") private String fullName;
 @NotBlank(message="Barua pepe inahitajika") @Email(message="Barua pepe si sahihi") private String email;
 @NotBlank(message="Namba ya simu inahitajika") @Pattern(regexp="^\\+[1-9][0-9]{6,14}$",message="Simu iwe na code ya nchi, mfano +255712345678") private String phone;
 @NotNull(message="Aina ya akaunti inahitajika") private User.Role role;
 @NotBlank(message="Aina ya kitambulisho inahitajika") private String idType;
 @NotBlank(message="Namba ya kitambulisho inahitajika") @Size(min=5,max=30,message="Namba ya kitambulisho iwe herufi 5 hadi 30") private String idNumber;
 @NotNull(message="Tarehe ya kuzaliwa inahitajika") @Past(message="Tarehe ya kuzaliwa si sahihi") private LocalDate dateOfBirth;
 @NotBlank(message="Jinsia inahitajika") private String gender;
 @NotBlank(message="Hali ya ndoa inahitajika") private String maritalStatus;
 @NotBlank(message="Utaifa inahitajika") private String nationality;
 @NotBlank(message="Anwani inahitajika") private String address;
 @NotBlank(message="Mji unahitajika") private String city;
 @NotBlank(message="Nchi inahitajika") private String country;
 @NotBlank(message="Hali ya ajira inahitajika") private String employmentStatus;
 private String occupation; private String employer;
 @NotNull(message="Kipato cha mwezi kinahitajika") @DecimalMin(value="0",message="Kipato hakiwezi kuwa hasi") private BigDecimal monthlyIncome;
 @NotBlank(message="Jina la ndugu wa karibu linahitajika") private String kinName;
 @NotBlank(message="Simu ya ndugu wa karibu inahitajika") @Pattern(regexp="^\\+[1-9][0-9]{6,14}$",message="Simu ya ndugu iwe na code ya nchi") private String kinPhone;
 @NotBlank(message="Uhusiano na ndugu unahitajika") private String kinRelationship;
 @NotBlank(message="Picha inahitajika") private String photo;
 public RegisterRequest(){}
 public RegisterRequest(String fullName,String email,String phone,User.Role role,String idType,String idNumber,LocalDate dateOfBirth,String gender,String maritalStatus,String nationality,String address,String city,String country,String employmentStatus,String occupation,String employer,BigDecimal monthlyIncome,String kinName,String kinPhone,String kinRelationship,String photo){this.fullName=fullName;this.email=email;this.phone=phone;this.role=role;this.idType=idType;this.idNumber=idNumber;this.dateOfBirth=dateOfBirth;this.gender=gender;this.maritalStatus=maritalStatus;this.nationality=nationality;this.address=address;this.city=city;this.country=country;this.employmentStatus=employmentStatus;this.occupation=occupation;this.employer=employer;this.monthlyIncome=monthlyIncome;this.kinName=kinName;this.kinPhone=kinPhone;this.kinRelationship=kinRelationship;this.photo=photo;}
 public static Builder builder(){return new Builder();} public static class Builder{private final RegisterRequest x=new RegisterRequest(); public Builder fullName(String v){x.fullName=v;return this;} public Builder email(String v){x.email=v;return this;} public Builder phone(String v){x.phone=v;return this;} public Builder role(User.Role v){x.role=v;return this;} public Builder idType(String v){x.idType=v;return this;} public Builder idNumber(String v){x.idNumber=v;return this;} public Builder dateOfBirth(LocalDate v){x.dateOfBirth=v;return this;} public Builder gender(String v){x.gender=v;return this;} public Builder maritalStatus(String v){x.maritalStatus=v;return this;} public Builder nationality(String v){x.nationality=v;return this;} public Builder address(String v){x.address=v;return this;} public Builder city(String v){x.city=v;return this;} public Builder country(String v){x.country=v;return this;} public Builder employmentStatus(String v){x.employmentStatus=v;return this;} public Builder occupation(String v){x.occupation=v;return this;} public Builder employer(String v){x.employer=v;return this;} public Builder monthlyIncome(BigDecimal v){x.monthlyIncome=v;return this;} public Builder kinName(String v){x.kinName=v;return this;} public Builder kinPhone(String v){x.kinPhone=v;return this;} public Builder kinRelationship(String v){x.kinRelationship=v;return this;} public Builder photo(String v){x.photo=v;return this;} public RegisterRequest build(){return x;}}
 public String getFullName(){return fullName;} public void setFullName(String v){fullName=v;} public String getEmail(){return email;} public void setEmail(String v){email=v;} public String getPhone(){return phone;} public void setPhone(String v){phone=v;} public User.Role getRole(){return role;} public void setRole(User.Role v){role=v;} public String getIdType(){return idType;} public void setIdType(String v){idType=v;} public String getIdNumber(){return idNumber;} public void setIdNumber(String v){idNumber=v;} public LocalDate getDateOfBirth(){return dateOfBirth;} public void setDateOfBirth(LocalDate v){dateOfBirth=v;} public String getGender(){return gender;} public void setGender(String v){gender=v;} public String getMaritalStatus(){return maritalStatus;} public void setMaritalStatus(String v){maritalStatus=v;} public String getNationality(){return nationality;} public void setNationality(String v){nationality=v;} public String getAddress(){return address;} public void setAddress(String v){address=v;} public String getCity(){return city;} public void setCity(String v){city=v;} public String getCountry(){return country;} public void setCountry(String v){country=v;} public String getEmploymentStatus(){return employmentStatus;} public void setEmploymentStatus(String v){employmentStatus=v;} public String getOccupation(){return occupation;} public void setOccupation(String v){occupation=v;} public String getEmployer(){return employer;} public void setEmployer(String v){employer=v;} public BigDecimal getMonthlyIncome(){return monthlyIncome;} public void setMonthlyIncome(BigDecimal v){monthlyIncome=v;} public String getKinName(){return kinName;} public void setKinName(String v){kinName=v;} public String getKinPhone(){return kinPhone;} public void setKinPhone(String v){kinPhone=v;} public String getKinRelationship(){return kinRelationship;} public void setKinRelationship(String v){kinRelationship=v;} public String getPhoto(){return photo;} public void setPhoto(String v){photo=v;}
}
=======

    @NotBlank(message = "Jina kamili linahitajika")
    @Size(min = 3, max = 200, message = "Jina liwe kati ya 3 na 200 herufi")
    private String fullName;

    @NotBlank(message = "Barua pepe inahitajika")
    @Email(message = "Barua pepe si sahihi")
    private String email;

    @NotBlank(message = "Namba ya simu inahitajika")
    private String phone;

    @NotNull(message = "Aina ya akaunti inahitajika")
    private User.Role role;

    @NotBlank(message = "Aina ya kitambulisho inahitajika")
    private String idType;

    @NotBlank(message = "Namba ya kitambulisho inahitajika")
    private String idNumber;

    @NotNull(message = "Tarehe ya kuzaliwa inahitajika")
    @Past(message = "Tarehe ya kuzaliwa si sahihi")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Jinsia inahitajika")
    private String gender;

    @NotBlank(message = "Hali ya ndoa inahitajika")
    private String maritalStatus;

    @NotBlank(message = "Utaifa unahitajika")
    private String nationality;

    @NotBlank(message = "Anwani inahitajika")
    private String address;

    @NotBlank(message = "Mji unahitajika")
    private String city;

    @NotBlank(message = "Nchi inahitajika")
    private String country;

    @NotBlank(message = "Hali ya ajira inahitajika")
    private String employmentStatus;

    private String occupation;
    private String employer;

    @NotNull(message = "Kipato cha mwezi kinahitajika")
    @DecimalMin(value = "0", message = "Kipato hakiwezi kuwa hasi")
    private BigDecimal monthlyIncome;

    @NotBlank(message = "Jina la ndugu wa karibu linahitajika")
    private String kinName;

    @NotBlank(message = "Simu ya ndugu wa karibu inahitajika")
    private String kinPhone;

    @NotBlank(message = "Uhusiano na ndugu unahitajika")
    private String kinRelationship;

    private String photo;
}
>>>>>>> Stashed changes
