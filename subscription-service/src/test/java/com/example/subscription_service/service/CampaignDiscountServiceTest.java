package com.example.subscription_service.service;

import com.example.subscription_service.document.CustomerProfile;
import com.example.subscription_service.dto.CampaignEligibilityResult;
import com.example.subscription_service.dto.CampaignResponse;
import com.example.subscription_service.dto.CampaignRuleResponse;
import com.example.subscription_service.dto.DiscountDecision;
import com.example.subscription_service.dto.PlanCampaignResponse;
import com.example.subscription_service.client.ReferenceServiceClient;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CampaignDiscountServiceTest {

    // --- calculateDiscount saf hesap testleri (mock gerektirmez) ---

    private final CampaignDiscountService pureService =
            new CampaignDiscountService(null, null);

    @Test
    void yuzde_indirim_dogru_hesaplanir() {
        CampaignResponse campaign = new CampaignResponse(
                1L, "YOUTH_20", "Gençlere", "PERCENTAGE",
                new BigDecimal("20"), 10, false, true);

        BigDecimal discount = pureService.calculateDiscount(campaign, new BigDecimal("450.00"));

        assertEquals(new BigDecimal("90.00"), discount);
    }

    @Test
    void sabit_indirim_dogru_hesaplanir() {
        CampaignResponse campaign = new CampaignResponse(
                2L, "LOYALTY_50", "Sadakat", "FIXED_AMOUNT",
                new BigDecimal("50"), 5, false, true);

        BigDecimal discount = pureService.calculateDiscount(campaign, new BigDecimal("450.00"));

        assertEquals(new BigDecimal("50.00"), discount);
    }

    // --- decideDiscount akış testleri (mock ile) ---

    private final ReferenceServiceClient client = mock(ReferenceServiceClient.class);
    private final EligibilityService eligibilityService = mock(EligibilityService.class);
    private final CampaignDiscountService service =
            new CampaignDiscountService(client, eligibilityService);

    private final CustomerProfile customer = new CustomerProfile();

    // Yardımcı: uygun (eligible) bir sonuç üretir
    private CampaignEligibilityResult eligible(String code) {
        return new CampaignEligibilityResult(code, true, List.of());
    }

    private CampaignEligibilityResult notEligible(String code) {
        return new CampaignEligibilityResult(code, false, List.of());
    }

    private CampaignResponse campaign(Long id, String code, String type,
                                      String value, int priority, boolean active) {
        return new CampaignResponse(id, code, code, type,
                new BigDecimal(value), priority, false, active);
    }

    @Test
    void yuksek_priority_kampanya_secilir() {
        // İki kampanya bağlı, ikisi de uygun; priority yüksek olan seçilmeli
        when(client.getPlanCampaignsByPlanId(1L)).thenReturn(List.of(
                new PlanCampaignResponse(1L, 10L, true),
                new PlanCampaignResponse(1L, 20L, true)
        ));

        CampaignResponse c1 = campaign(10L, "YOUTH_20", "PERCENTAGE", "20", 10, true);
        CampaignResponse c2 = campaign(20L, "NO_COMMITMENT_10", "PERCENTAGE", "10", 3, true);
        when(client.getCampaignById(10L)).thenReturn(c1);
        when(client.getCampaignById(20L)).thenReturn(c2);
        when(client.getRulesByCampaignId(10L)).thenReturn(List.of());
        when(client.getRulesByCampaignId(20L)).thenReturn(List.of());

        when(eligibilityService.evaluateCampaign("YOUTH_20", customer, List.of()))
                .thenReturn(eligible("YOUTH_20"));
        when(eligibilityService.evaluateCampaign("NO_COMMITMENT_10", customer, List.of()))
                .thenReturn(eligible("NO_COMMITMENT_10"));

        DiscountDecision result = service.decideDiscount(1L, customer, new BigDecimal("450.00"));

        // YOUTH_20 priority 10 > NO_COMMITMENT_10 priority 3 -> YOUTH_20 seçilir
        assertEquals("YOUTH_20", result.appliedCampaignCode());
    }

    @Test
    void esit_priorityde_yuksek_indirim_secilir() {
        when(client.getPlanCampaignsByPlanId(1L)).thenReturn(List.of(
                new PlanCampaignResponse(1L, 10L, true),
                new PlanCampaignResponse(1L, 20L, true)
        ));

        // İkisi de priority 5, ama biri %20 (90 TL) diğeri %10 (45 TL)
        CampaignResponse c1 = campaign(10L, "A_20", "PERCENTAGE", "20", 5, true);
        CampaignResponse c2 = campaign(20L, "B_10", "PERCENTAGE", "10", 5, true);
        when(client.getCampaignById(10L)).thenReturn(c1);
        when(client.getCampaignById(20L)).thenReturn(c2);
        when(client.getRulesByCampaignId(10L)).thenReturn(List.of());
        when(client.getRulesByCampaignId(20L)).thenReturn(List.of());

        when(eligibilityService.evaluateCampaign("A_20", customer, List.of()))
                .thenReturn(eligible("A_20"));
        when(eligibilityService.evaluateCampaign("B_10", customer, List.of()))
                .thenReturn(eligible("B_10"));

        DiscountDecision result = service.decideDiscount(1L, customer, new BigDecimal("450.00"));

        // Eşit priority -> yüksek indirim (A_20, 90 TL) seçilir
        assertEquals("A_20", result.appliedCampaignCode());
        assertEquals(new BigDecimal("90.00"), result.discountAmount());
    }

    @Test
    void eligible_olmayan_kampanya_elenir() {
        when(client.getPlanCampaignsByPlanId(1L)).thenReturn(List.of(
                new PlanCampaignResponse(1L, 10L, true)
        ));

        CampaignResponse c1 = campaign(10L, "YOUTH_20", "PERCENTAGE", "20", 10, true);
        when(client.getCampaignById(10L)).thenReturn(c1);
        when(client.getRulesByCampaignId(10L)).thenReturn(List.of());

        // Uygun DEĞİL
        when(eligibilityService.evaluateCampaign("YOUTH_20", customer, List.of()))
                .thenReturn(notEligible("YOUTH_20"));

        DiscountDecision result = service.decideDiscount(1L, customer, new BigDecimal("450.00"));

        // Hiç uygun kampanya yok -> indirim 0, uygulanan kampanya yok
        assertEquals(new BigDecimal("0"), result.discountAmount());
        assertEquals(null, result.appliedCampaignCode());
    }

    @Test
    void inactive_kampanya_elenir() {
        when(client.getPlanCampaignsByPlanId(1L)).thenReturn(List.of(
                new PlanCampaignResponse(1L, 10L, true)
        ));

        // Kampanya active=false -> hiç değerlendirilmeden atlanmalı
        CampaignResponse c1 = campaign(10L, "YOUTH_20", "PERCENTAGE", "20", 10, false);
        when(client.getCampaignById(10L)).thenReturn(c1);

        DiscountDecision result = service.decideDiscount(1L, customer, new BigDecimal("450.00"));

        // Inactive kampanya atlandı -> indirim yok
        assertEquals(new BigDecimal("0"), result.discountAmount());
        assertEquals(null, result.appliedCampaignCode());
    }
}