package com.example.subscription_service.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateSubscriptionRequest(
        @NotBlank(message = "customerId zorunludur")
        String customerId,

        @NotBlank(message = "quoteId zorunludur")
        String quoteId
) { }