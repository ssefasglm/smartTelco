package com.example.subscription_service.dto;

import java.math.BigDecimal;

public record TaxResponse(
        String code,
        BigDecimal rate
) { }