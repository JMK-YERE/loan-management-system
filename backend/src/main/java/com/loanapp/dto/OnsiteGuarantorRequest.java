package com.loanapp.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class OnsiteGuarantorRequest {
    @NotBlank(message="Jina la mdhamini linahitajika")
    @Size(max=200)
    private String name;

    @Size(max=50)
    private String phone;

    @Size(max=80)
    private String idNumber;

    @NotNull(message="Kiasi cha dhamana kinahitajika")
    @DecimalMin(value="0.00")
    private BigDecimal guaranteedAmount;

    @Size(max=500)
    private String relationship;

    @NotBlank(message="Picha ya mdhamini inahitajika")
    @Size(max=5000000)
    private String photoData;

    @NotBlank(message="Sahihi ya mdhamini inahitajika")
    @Size(max=1000000)
    private String signatureData;

    public String getName(){return name;} public void setName(String v){name=v;}
    public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
    public String getIdNumber(){return idNumber;} public void setIdNumber(String v){idNumber=v;}
    public BigDecimal getGuaranteedAmount(){return guaranteedAmount;} public void setGuaranteedAmount(BigDecimal v){guaranteedAmount=v;}
    public String getRelationship(){return relationship;} public void setRelationship(String v){relationship=v;}
    public String getPhotoData(){return photoData;} public void setPhotoData(String v){photoData=v;}
    public String getSignatureData(){return signatureData;} public void setSignatureData(String v){signatureData=v;}
}
