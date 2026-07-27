package com.example.subscription_service.eligibility;

import com.example.subscription_service.document.CustomerProfile;
import com.example.subscription_service.dto.CampaignRuleResponse;
import com.example.subscription_service.dto.RuleEvaluationResult;
import org.springframework.stereotype.Component;

@Component
public class CustomerSegmentEvaluator implements RuleEvaluator {

    @Override
    public String getRuleType() {
        return "CUSTOMER_SEGMENT";
    }

    @Override
    public RuleEvaluationResult evaluate(CustomerProfile customer, CampaignRuleResponse rule) {
        String customerSegment = customer.getSegment() != null
                ? customer.getSegment().name()
                : null;
        String expected = rule.expectedValue();

        boolean satisfied = expected.equals(customerSegment);

        String detail = "Segment " + customerSegment + ", beklenen " + expected
                + (satisfied ? ", sağlandı" : ", sağlanmadı");

        return new RuleEvaluationResult("CUSTOMER_SEGMENT", satisfied, detail);
    }
}