package com.example.subscription_service.service;

import com.example.subscription_service.document.CustomerProfile;
import com.example.subscription_service.dto.CampaignEligibilityResult;
import com.example.subscription_service.dto.CampaignRuleResponse;
import com.example.subscription_service.eligibility.AgeRangeEvaluator;
import com.example.subscription_service.eligibility.CustomerSegmentEvaluator;
import com.example.subscription_service.eligibility.MinTenureMonthsEvaluator;
import com.example.subscription_service.enums.CustomerSegment;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EligibilityServiceTest {

    private final EligibilityService service = new EligibilityService(List.of(
            new AgeRangeEvaluator(),
            new CustomerSegmentEvaluator(),
            new MinTenureMonthsEvaluator()
    ));

    private CustomerProfile customer(int age, CustomerSegment segment, int tenure) {
        CustomerProfile c = new CustomerProfile();
        c.setAge(age);
        c.setSegment(segment);
        c.setTenureMonths(tenure);
        return c;
    }

    @Test
    void butun_zorunlu_kurallar_gecerse_kampanya_uygun() {
        CustomerProfile c = customer(23, CustomerSegment.YOUTH, 14);
        List<CampaignRuleResponse> rules = List.of(
                new CampaignRuleResponse(1L, "AGE_RANGE", "BETWEEN", "18-26", true),
                new CampaignRuleResponse(2L, "CUSTOMER_SEGMENT", "EQUALS", "YOUTH", true),
                new CampaignRuleResponse(3L, "MIN_TENURE_MONTHS", "GTE", "6", true)
        );

        CampaignEligibilityResult result = service.evaluateCampaign("YOUTH_20", c, rules);

        assertTrue(result.eligible());
    }

    @Test
    void zorunlu_bir_kural_kalirsa_kampanya_uygun_degil() {
        // Yaş 40 -> AGE_RANGE zorunlu kuralı kalır
        CustomerProfile c = customer(40, CustomerSegment.YOUTH, 14);
        List<CampaignRuleResponse> rules = List.of(
                new CampaignRuleResponse(1L, "AGE_RANGE", "BETWEEN", "18-26", true),
                new CampaignRuleResponse(2L, "CUSTOMER_SEGMENT", "EQUALS", "YOUTH", true)
        );

        CampaignEligibilityResult result = service.evaluateCampaign("YOUTH_20", c, rules);

        assertFalse(result.eligible());
    }

    @Test
    void zorunlu_olmayan_kural_kalsa_bile_kampanya_uygun() {
        // Yaş 40 ama bu kural mandatory=false -> kampanya yine uygun
        CustomerProfile c = customer(40, CustomerSegment.YOUTH, 14);
        List<CampaignRuleResponse> rules = List.of(
                new CampaignRuleResponse(1L, "AGE_RANGE", "BETWEEN", "18-26", false),
                new CampaignRuleResponse(2L, "CUSTOMER_SEGMENT", "EQUALS", "YOUTH", true)
        );

        CampaignEligibilityResult result = service.evaluateCampaign("YOUTH_20", c, rules);

        assertTrue(result.eligible());
    }

    @Test
    void evaluator_bulunamayan_zorunlu_kural_kampanyayi_eler() {
        CustomerProfile c = customer(23, CustomerSegment.YOUTH, 14);
        List<CampaignRuleResponse> rules = List.of(
                // Bu kural tipi için strateji yok -> zorunluysa kampanya elenir
                new CampaignRuleResponse(9L, "UNKNOWN_RULE", "EQUALS", "X", true)
        );

        CampaignEligibilityResult result = service.evaluateCampaign("TEST", c, rules);

        assertFalse(result.eligible());
    }
}