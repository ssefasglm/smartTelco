package com.example.subscription_service.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.example.subscription_service.enums.CustomerSegment;

@Document(collection = "customer_profile")
public class CustomerProfile {

    @Id
    private String id;

    private String customerId;
    private Integer age;
    private CustomerSegment segment;
    private Integer tenureMonths;
    private String currentPlanCode;
    private BigDecimal averageDataUsageGb;
    private BigDecimal monthlyBudget;
    private LocalDate commitmentEndDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public CustomerSegment getSegment() {
        return segment;
    }

    public void setSegment(CustomerSegment segment) {
        this.segment = segment;
    }

    public Integer getTenureMonths() {
        return tenureMonths;
    }

    public void setTenureMonths(Integer tenureMonths) {
        this.tenureMonths = tenureMonths;
    }

    public String getCurrentPlanCode() {
        return currentPlanCode;
    }

    public void setCurrentPlanCode(String currentPlanCode) {
        this.currentPlanCode = currentPlanCode;
    }

    public BigDecimal getAverageDataUsageGb() {
        return averageDataUsageGb;
    }

    public void setAverageDataUsageGb(BigDecimal averageDataUsageGb) {
        this.averageDataUsageGb = averageDataUsageGb;
    }

    public BigDecimal getMonthlyBudget() {
        return monthlyBudget;
    }

    public void setMonthlyBudget(BigDecimal monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }

    public LocalDate getCommitmentEndDate() {
        return commitmentEndDate;
    }

    public void setCommitmentEndDate(LocalDate commitmentEndDate) {
        this.commitmentEndDate = commitmentEndDate;
    }
}