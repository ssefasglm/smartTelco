package com.example.subscription_service.dto;

import java.math.BigDecimal;

public record PlanResponse(
        Long id,
        String code,
        String name,
        BigDecimal monthlyFee
) { }