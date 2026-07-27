package com.example.subscription_service.dto;

import java.util.List;

public record CampaignEligibilityResult(
        String campaignCode,
        boolean eligible,
        List<RuleEvaluationResult> ruleResults
) { }