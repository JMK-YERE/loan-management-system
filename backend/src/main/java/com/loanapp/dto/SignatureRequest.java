package com.loanapp.dto;

import com.loanapp.model.Signature;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SignatureRequest {
    @NotNull(message = "Loan ID inahitajika") private Long loanId;
    @NotBlank(message = "Sahihi inahitajika") @Size(max = 1000000, message = "Sahihi ni kubwa sana") private String signatureData;
    @NotNull(message = "Aina ya sahihi inahitajika") private Signature.SignatureType signatureType;
    @NotBlank(message = "Toleo la ridhaa linahitajika") private String consentVersion;
    @AssertTrue(message = "Lazima uthibitishe kuwa umesoma na kuelewa mkataba") private boolean consentAccepted;
    private String deviceInfo;

    public SignatureRequest() {}
    public SignatureRequest(Long loanId,String signatureData,Signature.SignatureType signatureType,String consentVersion,boolean consentAccepted,String deviceInfo){
        this.loanId=loanId;this.signatureData=signatureData;this.signatureType=signatureType;this.consentVersion=consentVersion;this.consentAccepted=consentAccepted;this.deviceInfo=deviceInfo;
    }
    public Long getLoanId(){return loanId;} public void setLoanId(Long v){loanId=v;}
    public String getSignatureData(){return signatureData;} public void setSignatureData(String v){signatureData=v;}
    public Signature.SignatureType getSignatureType(){return signatureType;} public void setSignatureType(Signature.SignatureType v){signatureType=v;}
    public String getConsentVersion(){return consentVersion;} public void setConsentVersion(String v){consentVersion=v;}
    public boolean isConsentAccepted(){return consentAccepted;} public void setConsentAccepted(boolean v){consentAccepted=v;}
    public String getDeviceInfo(){return deviceInfo;} public void setDeviceInfo(String v){deviceInfo=v;}
}