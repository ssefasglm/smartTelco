package com.example.subscription_service.dto;

import com.example.subscription_service.enums.CustomerSegment;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CustomerResponse(
        String id,
        String customerId,
        Integer age,
        CustomerSegment segment,
        Integer tenureMonths,
        String currentPlanCode,
        BigDecimal averageDataUsageGb,
        BigDecimal monthlyBudget,
        LocalDate commitmentEndDate
) { }