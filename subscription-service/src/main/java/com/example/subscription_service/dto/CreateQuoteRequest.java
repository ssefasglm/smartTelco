package com.example.subscription_service.dto;

public record CreateQuoteRequest(
        String customerId,
        String planCode
) { }