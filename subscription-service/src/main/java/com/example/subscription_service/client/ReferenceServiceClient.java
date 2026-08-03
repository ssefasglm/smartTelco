package com.example.subscription_service.client;

import com.example.subscription_service.dto.*;
import com.example.subscription_service.exception.ServiceUnavailableException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.function.Supplier;

@Component
public class ReferenceServiceClient {

    private final RestClient referenceRestClient;

    public ReferenceServiceClient(RestClient referenceRestClient) {
        this.referenceRestClient = referenceRestClient;
    }

    public PlanResponse getPlanByCode(String planCode) {
        return callReference(() -> referenceRestClient.get()
                .uri("/api/v1/plans/{planCode}", planCode)
                .retrieve()
                .body(PlanResponse.class));
    }

    public List<PlanCampaignResponse> getPlanCampaignsByPlanId(Long planId) {
        return callReference(() -> referenceRestClient.get()
                .uri("/api/v1/plans/{planId}/campaigns", planId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<PlanCampaignResponse>>() {}));
    }

    public List<CampaignRuleResponse> getRulesByCampaignId(Long campaignId) {
        return callReference(() -> referenceRestClient.get()
                .uri("/api/v1/campaigns/{campaignId}/rules", campaignId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<CampaignRuleResponse>>() {}));
    }

    public CampaignResponse getCampaignById(Long campaignId) {
        return callReference(() -> referenceRestClient.get()
                .uri("/api/v1/campaigns/{campaignId}", campaignId)
                .retrieve()
                .body(CampaignResponse.class));
    }

    public TaxResponse getCurrentTax() {
        return callReference(() -> referenceRestClient.get()
                .uri("/api/v1/taxes/current")
                .retrieve()
                .body(TaxResponse.class));
    }

    // Tüm reference çağrılarını saran ortak yardımcı:
    // reference erişilemezse anlamlı bir 503 exception fırlatır.
    private <T> T callReference(Supplier<T> call) {
        try {
            return call.get();
        } catch (RestClientException ex) {
            throw new ServiceUnavailableException(
                    "Reference service is unavailable: " + ex.getMessage());
        }
    }
}