package com.example.subscription_service.service;

import com.example.subscription_service.document.Quote;
import com.example.subscription_service.document.Subscription;
import com.example.subscription_service.dto.SubscriptionResponse;
import com.example.subscription_service.enums.QuoteStatus;
import com.example.subscription_service.enums.SubscriptionStatus;
import com.example.subscription_service.exception.BadRequestException;
import com.example.subscription_service.exception.ConflictException;
import com.example.subscription_service.exception.NotFoundException;
import com.example.subscription_service.repository.QuoteRepository;
import com.example.subscription_service.repository.SubscriptionRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SubscriptionServiceTest {

    private final SubscriptionRepository subscriptionRepository = mock(SubscriptionRepository.class);
    private final QuoteRepository quoteRepository = mock(QuoteRepository.class);
    private final SubscriptionService service =
            new SubscriptionService(subscriptionRepository, quoteRepository);

    // Yardımcı: geçerli (VALID, süresi geçmemiş, C-1001'e ait) bir teklif üretir
    private Quote validQuote() {
        Quote q = new Quote();
        q.setQuoteId("Q-123");
        q.setCustomerId("C-1001");
        q.setPlanCode("PLAN_YOUTH_25");
        q.setPlanName("Youth 25 GB");
        q.setBaseAmount(new BigDecimal("450.00"));
        q.setDiscountAmount(new BigDecimal("90.00"));
        q.setSubtotal(new BigDecimal("360.00"));
        q.setTaxAmount(new BigDecimal("72.00"));
        q.setTotalAmount(new BigDecimal("432.00"));
        q.setStatus(QuoteStatus.VALID);
        q.setExpiresAt(Instant.now().plusSeconds(600)); // 10 dk sonrası -> geçerli
        return q;
    }

    @Test
    void gecerli_teklifle_abonelik_olusur_ve_teklif_consumed_olur() {
        Quote quote = validQuote();
        when(quoteRepository.findByQuoteId("Q-123")).thenReturn(Optional.of(quote));
        when(subscriptionRepository.existsByCustomerIdAndStatus("C-1001", SubscriptionStatus.ACTIVE))
                .thenReturn(false);
        // save çağrılınca verilen aboneliği geri dön
        when(subscriptionRepository.save(any(Subscription.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        SubscriptionResponse response = service.createSubscription("C-1001", "Q-123");

        // Abonelik ACTIVE, fiyat tekliften kopyalanmış
        assertEquals(SubscriptionStatus.ACTIVE, response.status());
        assertEquals(new BigDecimal("432.00"), response.totalAmount());
        // Teklif CONSUMED yapılmış olmalı
        assertEquals(QuoteStatus.CONSUMED, quote.getStatus());
        verify(quoteRepository).save(quote);
    }

    @Test
    void olmayan_teklif_404() {
        when(quoteRepository.findByQuoteId("Q-YOK")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.createSubscription("C-1001", "Q-YOK"));
    }

    @Test
    void baskasinin_teklifiyle_abone_olunamaz() {
        Quote quote = validQuote(); // C-1001'e ait
        when(quoteRepository.findByQuoteId("Q-123")).thenReturn(Optional.of(quote));

        // C-9999 başkasının teklifiyle deniyor
        assertThrows(BadRequestException.class,
                () -> service.createSubscription("C-9999", "Q-123"));
    }

    @Test
    void suresi_dolmus_teklifle_abone_olunamaz() {
        Quote quote = validQuote();
        quote.setExpiresAt(Instant.now().minusSeconds(60)); // 1 dk önce dolmuş

        when(quoteRepository.findByQuoteId("Q-123")).thenReturn(Optional.of(quote));

        assertThrows(ConflictException.class,
                () -> service.createSubscription("C-1001", "Q-123"));
        // Süresi dolmuşsa abonelik kaydedilmemeli
        verify(subscriptionRepository, never()).save(any());
    }

    @Test
    void zaten_aktif_aboneligi_olan_musteri_tekrar_abone_olamaz() {
        Quote quote = validQuote();
        when(quoteRepository.findByQuoteId("Q-123")).thenReturn(Optional.of(quote));
        // Müşterinin zaten aktif aboneliği var
        when(subscriptionRepository.existsByCustomerIdAndStatus("C-1001", SubscriptionStatus.ACTIVE))
                .thenReturn(true);

        assertThrows(ConflictException.class,
                () -> service.createSubscription("C-1001", "Q-123"));
    }
}