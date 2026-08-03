package com.example.subscription_service.dto;

import com.example.subscription_service.enums.CustomerSegment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateCustomerRequest(
        @NotBlank(message = "customerId zorunludur")
        String customerId,

        @NotNull(message = "age zorunludur")
        @Positive(message = "age pozitif olmalıdır")
        Integer age,

        @NotNull(message = "segment zorunludur")
        CustomerSegment segment,

        @NotNull(message = "tenureMonths zorunludur")
        @PositiveOrZero(message = "tenureMonths negatif olamaz")
        Integer tenureMonths,

        @NotBlank(message = "currentPlanCode zorunludur")
        String currentPlanCode,

        @NotNull(message = "averageDataUsageGb zorunludur")
        @PositiveOrZero(message = "averageDataUsageGb negatif olamaz")
        BigDecimal averageDataUsageGb,

        @NotNull(message = "monthlyBudget zorunludur")
        @Positive(message = "monthlyBudget pozitif olmalıdır")
        BigDecimal monthlyBudget,

        LocalDate commitmentEndDate
) { }