package com.example.subscription_service.dto;

public record RuleEvaluationResult(
        String ruleType,
        boolean satisfied,
        String detail
) { }