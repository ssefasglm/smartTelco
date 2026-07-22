package com.example.subscription_service.client;

import com.example.subscription_service.dto.PlanResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

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
}