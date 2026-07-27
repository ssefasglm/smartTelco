package com.example.subscription_service.eligibility;

import com.example.subscription_service.document.CustomerProfile;
import com.example.subscription_service.dto.CampaignRuleResponse;
import com.example.subscription_service.dto.RuleEvaluationResult;
import org.springframework.stereotype.Component;

@Component
public class MinTenureMonthsEvaluator implements RuleEvaluator {

    @Override
    public String getRuleType() {
        return "MIN_TENURE_MONTHS";
    }

    @Override
    public RuleEvaluationResult evaluate(CustomerProfile customer, CampaignRuleResponse rule) {
        Integer tenure = customer.getTenureMonths();
        int expected = Integer.parseInt(rule.expectedValue().trim());

        boolean satisfied = tenure != null && tenure >= expected;

        String detail = "Üyelik süresi " + tenure + " ay, beklenen en az " + expected + " ay"
                + (satisfied ? ", sağlandı" : ", sağlanmadı");

        return new RuleEvaluationResult("MIN_TENURE_MONTHS", satisfied, detail);
    }
}