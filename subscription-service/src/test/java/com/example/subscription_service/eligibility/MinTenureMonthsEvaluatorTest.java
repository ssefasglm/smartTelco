package com.example.subscription_service.eligibility;

import com.example.subscription_service.document.CustomerProfile;
import com.example.subscription_service.dto.CampaignRuleResponse;
import com.example.subscription_service.dto.RuleEvaluationResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MinTenureMonthsEvaluatorTest {

    private final MinTenureMonthsEvaluator evaluator = new MinTenureMonthsEvaluator();

    @Test
    void tenure_yeterliyse_kural_saglanir() {
        CustomerProfile customer = new CustomerProfile();
        customer.setTenureMonths(14);

        CampaignRuleResponse rule = new CampaignRuleResponse(
                1L, "MIN_TENURE_MONTHS", "GTE", "6", true);

        RuleEvaluationResult result = evaluator.evaluate(customer, rule);

        assertTrue(result.satisfied());
    }

    @Test
    void tenure_yetersizse_kural_saglanmaz() {
        CustomerProfile customer = new CustomerProfile();
        customer.setTenureMonths(3);

        CampaignRuleResponse rule = new CampaignRuleResponse(
                1L, "MIN_TENURE_MONTHS", "GTE", "6", true);

        RuleEvaluationResult result = evaluator.evaluate(customer, rule);

        assertFalse(result.satisfied());
    }

    @Test
    void tam_sinirda_kural_saglanir() {
        // 6 ay, beklenen "en az 6" -> tam sınır, geçmeli (GTE = büyük EŞİT)
        CustomerProfile customer = new CustomerProfile();
        customer.setTenureMonths(6);

        CampaignRuleResponse rule = new CampaignRuleResponse(
                1L, "MIN_TENURE_MONTHS", "GTE", "6", true);

        RuleEvaluationResult result = evaluator.evaluate(customer, rule);

        assertTrue(result.satisfied());
    }
}