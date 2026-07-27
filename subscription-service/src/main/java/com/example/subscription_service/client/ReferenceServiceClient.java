package com.example.subscription_service.client;

import com.example.subscription_service.dto.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
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
    public List<PlanCampaignResponse> getPlanCampaignsByPlanId(Long planId) {
        return referenceRestClient.get()
                .uri("/api/v1/plans/{planId}/campaigns", planId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<PlanCampaignResponse>>() {});
    }

    public List<CampaignRuleResponse> getRulesByCampaignId(Long campaignId) {
        return referenceRestClient.get()
                .uri("/api/v1/campaigns/{campaignId}/rules", campaignId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<CampaignRuleResponse>>() {});
    }
    public CampaignResponse getCampaignById(Long campaignId) {
        return referenceRestClient.get()
                .uri("/api/v1/campaigns/{campaignId}", campaignId)
                .retrieve()
                .body(CampaignResponse.class);
    }
    public TaxResponse getCurrentTax() {
        return referenceRestClient.get()
                .uri("/api/v1/taxes/current")
                .retrieve()
                .body(TaxResponse.class);
    }
}