package com.example.reference_service.dto;

public record PlanCampaignDto(
        Long id,
        Long planId,
        Long campaignId,
        Boolean active
) { }