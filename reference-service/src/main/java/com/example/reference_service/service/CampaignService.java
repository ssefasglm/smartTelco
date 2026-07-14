package com.example.reference_service.service;

import com.example.reference_service.entity.Campaign;
import com.example.reference_service.repository.CampaignRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CampaignService {

    private final CampaignRepository repository;

    public CampaignService(CampaignRepository repository) {
        this.repository = repository;
    }

    public List<Campaign> getAllCampaigns() {
        return repository.findAll();
    }
}