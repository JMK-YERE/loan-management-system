package com.loanapp.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class GuarantorRequest {
    @NotNull(message = "Guarantor ID inahitajika")
    private Long guarantorId;
    @NotNull(message = "Kiasi cha dhamana kinahitajika")
    private BigDecimal guaranteedAmount;
    private String relationship;

    public GuarantorRequest() {}
    public GuarantorRequest(Long guarantorId, BigDecimal guaranteedAmount, String relationship) {
        this.guarantorId = guarantorId;
        this.guaranteedAmount = guaranteedAmount;
        this.relationship = relationship;
    }
    public Long getGuarantorId() { return guarantorId; }
    public void setGuarantorId(Long guarantorId) { this.guarantorId = guarantorId; }
    public BigDecimal getGuaranteedAmount() { return guaranteedAmount; }
    public void setGuaranteedAmount(BigDecimal guaranteedAmount) { this.guaranteedAmount = guaranteedAmount; }
    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long guarantorId;
        private BigDecimal guaranteedAmount;
        private String relationship;
        public Builder guarantorId(Long v) { this.guarantorId = v; return this; }
        public Builder guaranteedAmount(BigDecimal v) { this.guaranteedAmount = v; return this; }
        public Builder relationship(String v) { this.relationship = v; return this; }
        public GuarantorRequest build() { return new GuarantorRequest(guarantorId, guaranteedAmount, relationship); }
    }
}