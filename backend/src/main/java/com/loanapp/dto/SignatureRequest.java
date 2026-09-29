package com.loanapp.dto;

import com.loanapp.model.Signature;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SignatureRequest {
    @NotNull(message = "Loan ID inahitajika") private Long loanId;
    @NotBlank(message = "Sahihi inahitajika") private String signatureData;
    @NotNull(message = "Aina ya sahihi inahitajika") private Signature.SignatureType signatureType;
    private String deviceInfo;

    public SignatureRequest() {}
    public SignatureRequest(Long loanId, String signatureData, Signature.SignatureType signatureType, String deviceInfo) {
        this.loanId=loanId; this.signatureData=signatureData; this.signatureType=signatureType; this.deviceInfo=deviceInfo;
    }
    public Long getLoanId(){return loanId;} public void setLoanId(Long v){loanId=v;}
    public String getSignatureData(){return signatureData;} public void setSignatureData(String v){signatureData=v;}
    public Signature.SignatureType getSignatureType(){return signatureType;} public void setSignatureType(Signature.SignatureType v){signatureType=v;}
    public String getDeviceInfo(){return deviceInfo;} public void setDeviceInfo(String v){deviceInfo=v;}
    public static Builder builder(){return new Builder();}
    public static class Builder {
        private Long loanId; private String signatureData; private Signature.SignatureType signatureType; private String deviceInfo;
        public Builder loanId(Long v){loanId=v;return this;} public Builder signatureData(String v){signatureData=v;return this;}
        public Builder signatureType(Signature.SignatureType v){signatureType=v;return this;} public Builder deviceInfo(String v){deviceInfo=v;return this;}
        public SignatureRequest build(){return new SignatureRequest(loanId,signatureData,signatureType,deviceInfo);}
    }
}
