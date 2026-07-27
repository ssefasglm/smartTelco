package com.example.subscription_service.dto;

import java.math.BigDecimal;

public record CampaignResponse(
        Long id,
        String code,
        String name,
        String discountType,
        BigDecimal discountValue,
        Integer priority,
        Boolean combinable
) { }