package com.example.subscription_service.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuoteServiceTest {

    // Bağımlılıkları null veriyoruz; calculateSubtotal onları kullanmıyor
    private final QuoteService service =
            new QuoteService(null, null, null, null);

    @Test
    void indirim_bazi_gecerse_subtotal_sifira_sabitlenir() {
        // Senaryo C: 100 baz, 150 sabit indirim -> negatif değil, 0.00
        BigDecimal subtotal = service.calculateSubtotal(
                new BigDecimal("100.00"), new BigDecimal("150.00"));

        assertEquals(new BigDecimal("0.00"), subtotal);
    }

    @Test
    void normal_indirim_dogru_hesaplanir() {
        // 450 baz, 90 indirim -> 360
        BigDecimal subtotal = service.calculateSubtotal(
                new BigDecimal("450.00"), new BigDecimal("90.00"));

        assertEquals(new BigDecimal("360.00"), subtotal);
    }
}