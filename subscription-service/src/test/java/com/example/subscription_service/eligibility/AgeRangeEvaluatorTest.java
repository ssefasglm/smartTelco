package com.example.subscription_service.eligibility;

import com.example.subscription_service.document.CustomerProfile;
import com.example.subscription_service.dto.CampaignRuleResponse;
import com.example.subscription_service.dto.RuleEvaluationResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AgeRangeEvaluatorTest {

    private final AgeRangeEvaluator evaluator = new AgeRangeEvaluator();

    @Test
    void yas_aralik_icindeyse_kural_saglanir() {
        // Hazırla: 23 yaşında müşteri, "18-26" kuralı
        CustomerProfile customer = new CustomerProfile();
        customer.setAge(23);

        CampaignRuleResponse rule = new CampaignRuleResponse(
                1L, "AGE_RANGE", "BETWEEN", "18-26", true);

        // Çalıştır
        RuleEvaluationResult result = evaluator.evaluate(customer, rule);

        // Kontrol et: sağlanmalı
        assertTrue(result.satisfied());
    }

    @Test
    void yas_aralik_disindaysa_kural_saglanmaz() {
        // Hazırla: 40 yaşında müşteri, "18-26" kuralı
        CustomerProfile customer = new CustomerProfile();
        customer.setAge(40);

        CampaignRuleResponse rule = new CampaignRuleResponse(
                1L, "AGE_RANGE", "BETWEEN", "18-26", true);

        // Çalıştır
        RuleEvaluationResult result = evaluator.evaluate(customer, rule);

        // Kontrol et: sağlanmamalı
        assertFalse(result.satisfied());
    }
}