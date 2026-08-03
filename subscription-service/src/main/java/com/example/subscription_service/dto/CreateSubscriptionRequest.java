package com.example.subscription_service.dto;

public record CreateSubscriptionRequest(
        String customerId,
        String quoteId
) { }