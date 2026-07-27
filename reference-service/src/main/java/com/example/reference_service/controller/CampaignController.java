package com.example.reference_service.controller;

import com.example.reference_service.entity.Campaign;
import com.example.reference_service.service.CampaignService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/campaigns")
public class CampaignController {

    private final CampaignService service;

    public CampaignController(CampaignService service) {
        this.service = service;
    }

    @GetMapping
    public List<Campaign> getAllCampaigns() {
        return service.getAllCampaigns();
    }
    @GetMapping("/{id}")
    public Campaign getCampaignById(@PathVariable Long id) {
        return service.getCampaignById(id);
    }
}