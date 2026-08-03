package com.example.subscription_service.service;

import com.example.subscription_service.document.Quote;
import com.example.subscription_service.document.Subscription;
import com.example.subscription_service.dto.SubscriptionResponse;
import com.example.subscription_service.enums.QuoteStatus;
import com.example.subscription_service.enums.SubscriptionStatus;
import com.example.subscription_service.repository.QuoteRepository;
import com.example.subscription_service.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import com.example.subscription_service.exception.BadRequestException;
import com.example.subscription_service.exception.ConflictException;
import com.example.subscription_service.exception.NotFoundException;

import java.time.Instant;
import java.util.UUID;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final QuoteRepository quoteRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               QuoteRepository quoteRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.quoteRepository = quoteRepository;
    }

    public SubscriptionResponse createSubscription(String customerId, String quoteId) {
        // 1. Teklif var mı?
        Quote quote = quoteRepository.findByQuoteId(quoteId)
                .orElseThrow(() -> new NotFoundException("Quote not found: " + quoteId));

        // 2. Teklif bu müşteriye mi ait?
        if (!quote.getCustomerId().equals(customerId)) {
            throw new BadRequestException("Quote " + quoteId + " does not belong to customer " + customerId);
        }

        // 3. Teklif zaten kullanılmış mı?
        if (quote.getStatus() == QuoteStatus.CONSUMED) {
            throw new ConflictException("Quote " + quoteId + " is already consumed");
        }

        // 4. Teklif hâlâ geçerli mi? (süresi dolmuş mu)
        if (quote.getStatus() == QuoteStatus.EXPIRED
                || quote.getExpiresAt().isBefore(Instant.now())) {
            throw new ConflictException("Quote " + quoteId + " has expired");
        }

        // 5. Müşterinin zaten aktif aboneliği var mı? (tek aktif abonelik kuralı)
        subscriptionRepository.findByCustomerIdAndStatus(customerId, SubscriptionStatus.ACTIVE)
                .ifPresent(existing -> {
                    throw new ConflictException(
                            "Customer " + customerId + " already has an active subscription: "
                                    + existing.getSubscriptionId());
                });

        // Tüm kontroller geçti -> aboneliği oluştur (fiyatı tekliften kopyala = snapshot)
        Subscription subscription = new Subscription();
        subscription.setSubscriptionId("S-" + UUID.randomUUID().toString().substring(0, 8));
        subscription.setCustomerId(customerId);
        subscription.setQuoteId(quoteId);

        subscription.setPlanCode(quote.getPlanCode());
        subscription.setPlanName(quote.getPlanName());

        subscription.setAppliedCampaignCode(quote.getAppliedCampaignCode());
        subscription.setBaseAmount(quote.getBaseAmount());
        subscription.setDiscountAmount(quote.getDiscountAmount());
        subscription.setSubtotal(quote.getSubtotal());
        subscription.setTaxAmount(quote.getTaxAmount());
        subscription.setTotalAmount(quote.getTotalAmount());

        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setStartedAt(Instant.now());

        Subscription saved = subscriptionRepository.save(subscription);

        // Teklifi CONSUMED yap -> tekrar kullanılamasın
        quote.setStatus(QuoteStatus.CONSUMED);
        quoteRepository.save(quote);

        return toResponse(saved);
    }

    public SubscriptionResponse getBySubscriptionId(String subscriptionId) {
        Subscription subscription = subscriptionRepository.findBySubscriptionId(subscriptionId)
                .orElseThrow(() -> new NotFoundException("Subscription not found: " + subscriptionId));
        return toResponse(subscription);
    }

    public SubscriptionResponse cancelSubscription(String subscriptionId) {
        Subscription subscription = subscriptionRepository.findBySubscriptionId(subscriptionId)
                .orElseThrow(() -> new NotFoundException("Subscription not found: " + subscriptionId));

        if (subscription.getStatus() == SubscriptionStatus.CANCELLED) {
            throw new ConflictException("Subscription " + subscriptionId + " is already cancelled");
        }

        subscription.setStatus(SubscriptionStatus.CANCELLED);
        Subscription saved = subscriptionRepository.save(subscription);

        return toResponse(saved);
    }

    private SubscriptionResponse toResponse(Subscription s) {
        return new SubscriptionResponse(
                s.getSubscriptionId(),
                s.getCustomerId(),
                s.getQuoteId(),
                s.getPlanCode(),
                s.getPlanName(),
                s.getAppliedCampaignCode(),
                s.getBaseAmount(),
                s.getDiscountAmount(),
                s.getSubtotal(),
                s.getTaxAmount(),
                s.getTotalAmount(),
                s.getStatus(),
                s.getStartedAt()
        );
    }
}