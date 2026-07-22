package com.example.subscription_service.service;

import com.example.subscription_service.client.ReferenceServiceClient;
import com.example.subscription_service.document.CustomerProfile;
import com.example.subscription_service.document.Quote;
import com.example.subscription_service.dto.PlanResponse;
import com.example.subscription_service.dto.QuoteResponse;
import com.example.subscription_service.dto.TaxResponse;
import com.example.subscription_service.enums.QuoteStatus;
import com.example.subscription_service.repository.CustomerProfileRepository;
import com.example.subscription_service.repository.QuoteRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.UUID;

@Service
public class QuoteService {

    private final CustomerProfileRepository customerRepository;
    private final QuoteRepository quoteRepository;
    private final ReferenceServiceClient referenceServiceClient;

    public QuoteService(CustomerProfileRepository customerRepository,
                        QuoteRepository quoteRepository,
                        ReferenceServiceClient referenceServiceClient) {
        this.customerRepository = customerRepository;
        this.quoteRepository = quoteRepository;
        this.referenceServiceClient = referenceServiceClient;
    }

    public QuoteResponse createQuote(String customerId, String planCode) {
        // 1. Müşteri gerçekten var mı?
        CustomerProfile customer = customerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + customerId));

        // 2. Tarife ve vergiyi reference'tan çek
        PlanResponse plan = referenceServiceClient.getPlanByCode(planCode);
        TaxResponse tax = referenceServiceClient.getCurrentTax();

        // 3. Fiyat hesabı (kampanyasız — indirim yok)
        BigDecimal baseAmount = plan.monthlyFee().setScale(2, RoundingMode.HALF_UP);

        BigDecimal taxRate = tax.rate();
        BigDecimal taxAmount = baseAmount
                .multiply(taxRate)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal totalAmount = baseAmount.add(taxAmount);

        // 4. Quote document'ını doldur (snapshot)
        Quote quote = new Quote();
        quote.setQuoteId("Q-" + UUID.randomUUID().toString().substring(0, 8));
        quote.setCustomerId(customerId);

        quote.setPlanCode(plan.code());
        quote.setPlanName(plan.name());
        quote.setBaseAmount(baseAmount);

        quote.setTaxRate(taxRate);
        quote.setTaxAmount(taxAmount);
        quote.setTotalAmount(totalAmount);

        quote.setStatus(QuoteStatus.VALID);
        quote.setCreatedAt(Instant.now());
        quote.setExpiresAt(Instant.now().plusSeconds(15 * 60));

        // 5. Mongo'ya kaydet
        Quote saved = quoteRepository.save(quote);

        // 6. Response DTO'ya çevirip dön
        return toResponse(saved);
    }

    private QuoteResponse toResponse(Quote quote) {
        return new QuoteResponse(
                quote.getQuoteId(),
                quote.getCustomerId(),
                quote.getPlanCode(),
                quote.getPlanName(),
                quote.getBaseAmount(),
                quote.getTaxRate(),
                quote.getTaxAmount(),
                quote.getTotalAmount(),
                quote.getStatus(),
                quote.getExpiresAt()
        );
    }
}