package com.example.subscription_service.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateQuoteRequest(
        @NotBlank(message = "customerId zorunludur")
        String customerId,

        @NotBlank(message = "planCode zorunludur")
        String planCode
) { }