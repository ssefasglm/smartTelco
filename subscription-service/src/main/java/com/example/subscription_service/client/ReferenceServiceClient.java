package com.example.subscription_service.client;

import com.example.subscription_service.dto.PlanResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import com.example.subscription_service.dto.TaxResponse;
import com.example.subscription_service.dto.CampaignResponse;
import com.example.subscription_service.dto.CampaignRuleResponse;
import org.springframework.core.ParameterizedTypeReference;
import java.util.List;

@Component
public class ReferenceServiceClient {

    private final RestClient referenceRestClient;

    public ReferenceServiceClient(RestClient referenceRestClient) {
        this.referenceRestClient = referenceRestClient;
    }

    public PlanResponse getPlanByCode(String planCode) {
        return referenceRestClient.get()
                .uri("/api/v1/plans/{planCode}", planCode)
                .retrieve()
                .body(PlanResponse.class);
    }
    public List<CampaignResponse> getCampaignsByPlanId(Long planId) {
        return referenceRestClient.get()
                .uri("/api/v1/plans/{planId}/campaigns", planId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<CampaignResponse>>() {});
    }

    public List<CampaignRuleResponse> getRulesByCampaignId(Long campaignId) {
        return referenceRestClient.get()
                .uri("/api/v1/campaigns/{campaignId}/rules", campaignId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<CampaignRuleResponse>>() {});
    }
    public TaxResponse getCurrentTax() {
        return referenceRestClient.get()
                .uri("/api/v1/taxes/current")
                .retrieve()
                .body(TaxResponse.class);
    }
}