package com.example.subscription_service.dto;

import com.example.subscription_service.enums.QuoteStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record QuoteResponse(
        String quoteId,
        String customerId,
        String planCode,
        String planName,
        BigDecimal baseAmount,
        BigDecimal taxRate,
        BigDecimal taxAmount,
        BigDecimal totalAmount,
        QuoteStatus status,
        Instant expiresAt
) { }