package com.example.subscription_service.dto;

import java.math.BigDecimal;
import java.util.List;

public record DiscountDecision(
        String appliedCampaignCode,
        BigDecimal discountAmount,
        List<CampaignEligibilityResult> evaluations
) { }