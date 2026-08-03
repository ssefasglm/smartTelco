package com.example.subscription_service.eligibility;

import com.example.subscription_service.document.CustomerProfile;
import com.example.subscription_service.dto.CampaignRuleResponse;
import com.example.subscription_service.dto.RuleEvaluationResult;
import com.example.subscription_service.enums.CustomerSegment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerSegmentEvaluatorTest {

    private final CustomerSegmentEvaluator evaluator = new CustomerSegmentEvaluator();

    @Test
    void segment_eslesirse_kural_saglanir() {
        CustomerProfile customer = new CustomerProfile();
        customer.setSegment(CustomerSegment.YOUTH);

        CampaignRuleResponse rule = new CampaignRuleResponse(
                1L, "CUSTOMER_SEGMENT", "EQUALS", "YOUTH", true);

        RuleEvaluationResult result = evaluator.evaluate(customer, rule);

        assertTrue(result.satisfied());
    }

    @Test
    void segment_eslesmezse_kural_saglanmaz() {
        CustomerProfile customer = new CustomerProfile();
        customer.setSegment(CustomerSegment.STANDARD);

        CampaignRuleResponse rule = new CampaignRuleResponse(
                1L, "CUSTOMER_SEGMENT", "EQUALS", "YOUTH", true);

        RuleEvaluationResult result = evaluator.evaluate(customer, rule);

        assertFalse(result.satisfied());
    }
}