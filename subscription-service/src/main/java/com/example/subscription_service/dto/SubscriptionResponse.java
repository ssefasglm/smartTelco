package com.example.subscription_service.dto;

import com.example.subscription_service.enums.SubscriptionStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record SubscriptionResponse(
        String subscriptionId,
        String customerId,
        String quoteId,
        String planCode,
        String planName,
        String appliedCampaignCode,
        BigDecimal baseAmount,
        BigDecimal discountAmount,
        BigDecimal subtotal,
        BigDecimal taxAmount,
        BigDecimal totalAmount,
        SubscriptionStatus status,
        Instant startedAt
) { }