package com.example.subscription_service.dto;

public record PlanCampaignResponse(
        Long planId,
        Long campaignId,
        Boolean active
) { }