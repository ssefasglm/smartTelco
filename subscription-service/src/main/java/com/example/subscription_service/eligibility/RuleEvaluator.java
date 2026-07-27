package com.example.subscription_service.eligibility;

import com.example.subscription_service.document.CustomerProfile;
import com.example.subscription_service.dto.CampaignRuleResponse;
import com.example.subscription_service.dto.RuleEvaluationResult;

public interface RuleEvaluator {

    String getRuleType();

    RuleEvaluationResult evaluate(CustomerProfile customer, CampaignRuleResponse rule);
}