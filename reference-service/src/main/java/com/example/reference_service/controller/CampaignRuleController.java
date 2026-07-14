package com.example.reference_service.controller;

import com.example.reference_service.dto.CampaignRuleDto;
import com.example.reference_service.service.CampaignRuleService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/campaigns")
public class CampaignRuleController {

    private final CampaignRuleService service;

    public CampaignRuleController(CampaignRuleService service) {
        this.service = service;
    }

    @GetMapping("/{campaignId}/rules")
    public List<CampaignRuleDto> getRules(@PathVariable Long campaignId) {
        return service.getRulesByCampaignId(campaignId);
    }
}