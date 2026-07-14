package com.example.reference_service.controller;

import com.example.reference_service.dto.PlanCampaignDto;
import com.example.reference_service.service.PlanCampaignService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/plans")
public class PlanCampaignController {

    private final PlanCampaignService service;

    public PlanCampaignController(PlanCampaignService service) {
        this.service = service;
    }

    @GetMapping("/{planId}/campaigns")
    public List<PlanCampaignDto> getCampaigns(@PathVariable Long planId) {
        return service.getCampaignsByPlanId(planId);
    }
}