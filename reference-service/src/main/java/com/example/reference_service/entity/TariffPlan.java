package com.example.reference_service.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tariff_plan")
public class TariffPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "monthly_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyFee;

    @Column(name = "data_quota_gb", nullable = false)
    private Integer dataQuotaGb;

    @Column(name = "voice_minutes", nullable = false)
    private Integer voiceMinutes;

    @Column(name = "sms_count", nullable = false)
    private Integer smsCount;

    @Column(name = "commitment_months", nullable = false)
    private Integer commitmentMonths;

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;

    @Column(nullable = false)
    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(BigDecimal monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public Integer getDataQuotaGb() {
        return dataQuotaGb;
    }

    public void setDataQuotaGb(Integer dataQuotaGb) {
        this.dataQuotaGb = dataQuotaGb;
    }

    public Integer getVoiceMinutes() {
        return voiceMinutes;
    }

    public void setVoiceMinutes(Integer voiceMinutes) {
        this.voiceMinutes = voiceMinutes;
    }

    public Integer getSmsCount() {
        return smsCount;
    }

    public void setSmsCount(Integer smsCount) {
        this.smsCount = smsCount;
    }

    public Integer getCommitmentMonths() {
        return commitmentMonths;
    }

    public void setCommitmentMonths(Integer commitmentMonths) {
        this.commitmentMonths = commitmentMonths;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
