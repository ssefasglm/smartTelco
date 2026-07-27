package com.example.subscription_service.dto;

public record CampaignRuleResponse(
        Long id,
        String ruleType,
        String operator,
        String expectedValue,
        Boolean mandatory
) { }