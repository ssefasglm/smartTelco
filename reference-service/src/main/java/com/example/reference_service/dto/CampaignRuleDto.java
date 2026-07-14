package com.example.reference_service.dto;

import com.example.reference_service.entity.RuleType;

public record CampaignRuleDto(
        Long id,
        RuleType ruleType,
        String operator,
        String expectedValue,
        Boolean mandatory
) { }