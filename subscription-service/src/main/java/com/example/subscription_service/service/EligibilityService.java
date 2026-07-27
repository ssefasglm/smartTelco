package com.example.subscription_service.service;

import com.example.subscription_service.document.CustomerProfile;
import com.example.subscription_service.dto.CampaignEligibilityResult;
import com.example.subscription_service.dto.CampaignRuleResponse;
import com.example.subscription_service.dto.RuleEvaluationResult;
import com.example.subscription_service.eligibility.RuleEvaluator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EligibilityService {

    private final Map<String, RuleEvaluator> evaluatorsByType;

    public EligibilityService(List<RuleEvaluator> evaluators) {
        this.evaluatorsByType = evaluators.stream()
                .collect(Collectors.toMap(RuleEvaluator::getRuleType, Function.identity()));
    }

    public CampaignEligibilityResult evaluateCampaign(
            String campaignCode,
            CustomerProfile customer,
            List<CampaignRuleResponse> rules) {

        List<RuleEvaluationResult> ruleResults = new ArrayList<>();
        boolean eligible = true;

        for (CampaignRuleResponse rule : rules) {
            RuleEvaluator evaluator = evaluatorsByType.get(rule.ruleType());

            if (evaluator == null) {
                // Bu kural tipi için strateji yok -> şimdilik atla, ama işaretle
                ruleResults.add(new RuleEvaluationResult(
                        rule.ruleType(), false,
                        "Bu kural tipi için değerlendirici bulunamadı"));
                if (Boolean.TRUE.equals(rule.mandatory())) {
                    eligible = false;
                }
                continue;
            }

            RuleEvaluationResult result = evaluator.evaluate(customer, rule);
            ruleResults.add(result);

            // Zorunlu bir kural sağlanmadıysa kampanya uygun değildir
            if (Boolean.TRUE.equals(rule.mandatory()) && !result.satisfied()) {
                eligible = false;
            }
        }

        return new CampaignEligibilityResult(campaignCode, eligible, ruleResults);
    }
}