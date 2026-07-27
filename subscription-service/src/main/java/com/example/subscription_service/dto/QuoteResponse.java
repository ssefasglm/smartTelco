package com.example.subscription_service.dto;

import com.example.subscription_service.enums.QuoteStatus;
import com.example.subscription_service.dto.CampaignEligibilityResult;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record QuoteResponse(
        String quoteId,
        String customerId,
        String planCode,
        String planName,
        BigDecimal baseAmount,
        String appliedCampaignCode,
        BigDecimal discountAmount,
        BigDecimal subtotal,
        BigDecimal taxRate,
        BigDecimal taxAmount,
        BigDecimal totalAmount,
        QuoteStatus status,
        Instant expiresAt,
        List<CampaignEligibilityResult> eligibilityResults
) { }