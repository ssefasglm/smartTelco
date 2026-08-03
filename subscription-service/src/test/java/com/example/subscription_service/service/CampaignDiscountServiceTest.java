package com.example.subscription_service.service;

import com.example.subscription_service.dto.CampaignResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CampaignDiscountServiceTest {

    // Bağımlılıkları test için null veriyoruz; calculateDiscount onları kullanmıyor
    private final CampaignDiscountService service =
            new CampaignDiscountService(null, null);

    @Test
    void yuzde_indirim_dogru_hesaplanir() {
        // %20 indirim, 450 baz -> 90
        CampaignResponse campaign = new CampaignResponse(
                1L, "YOUTH_20", "Gençlere", "PERCENTAGE",
                new BigDecimal("20"), 10, false);

        BigDecimal discount = service.calculateDiscount(campaign, new BigDecimal("450.00"));

        assertEquals(new BigDecimal("90.00"), discount);
    }

    @Test
    void sabit_indirim_dogru_hesaplanir() {
        // 50 TL sabit indirim
        CampaignResponse campaign = new CampaignResponse(
                2L, "LOYALTY_50", "Sadakat", "FIXED_AMOUNT",
                new BigDecimal("50"), 5, false);

        BigDecimal discount = service.calculateDiscount(campaign, new BigDecimal("450.00"));

        assertEquals(new BigDecimal("50.00"), discount);
    }
}