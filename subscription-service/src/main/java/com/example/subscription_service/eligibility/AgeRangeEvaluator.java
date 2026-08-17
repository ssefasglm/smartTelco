package com.example.subscription_service.eligibility;

import com.example.subscription_service.document.CustomerProfile;
import com.example.subscription_service.dto.CampaignRuleResponse;
import com.example.subscription_service.dto.RuleEvaluationResult;
import org.springframework.stereotype.Component;

@Component
public class AgeRangeEvaluator implements RuleEvaluator {

    private static final String AGE_RANGE = "AGE_RANGE";
    @Override
    public String getRuleType() {
        return AGE_RANGE;
    }

    @Override
    public RuleEvaluationResult evaluate(CustomerProfile customer, CampaignRuleResponse rule) {
        Integer age = customer.getAge();

        // expectedValue "18-26" formatında -> alt ve üst sınıra ayır
        String[] parts = rule.expectedValue().split("-");
        int min = Integer.parseInt(parts[0].trim());
        int max = Integer.parseInt(parts[1].trim());

        boolean satisfied = age != null && age >= min && age <= max;

        String detail = "Yaş " + age + ", beklenen aralık " + min + "-" + max
                + (satisfied ? ", sağlandı" : ", sağlanmadı");

        return new RuleEvaluationResult("AGE_RANGE", satisfied, detail);
    }
}