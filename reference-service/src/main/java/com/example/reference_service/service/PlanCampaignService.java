package com.example.reference_service.service;

import com.example.reference_service.dto.PlanCampaignDto;
import com.example.reference_service.repository.PlanCampaignRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PlanCampaignService {

    private final PlanCampaignRepository repository;

    public PlanCampaignService(PlanCampaignRepository repository) {
        this.repository = repository;
    }

    public List<PlanCampaignDto> getCampaignsByPlanId(Long planId) {
        return repository.findByPlanId(planId)
                .stream()
                .map(pc -> new PlanCampaignDto(
                        pc.getId(),
                        pc.getPlan().getId(),
                        pc.getCampaign().getId(),
                        pc.getActive()
                ))
                .toList();
    }
}