package com.example.subscription_service.dto;

import java.math.BigDecimal;

public record PlanResponse(
        String code,
        String name,
        BigDecimal monthlyFee
) { }