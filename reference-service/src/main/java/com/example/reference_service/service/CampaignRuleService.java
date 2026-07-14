package com.example.reference_service.service;

import com.example.reference_service.dto.CampaignRuleDto;
import com.example.reference_service.repository.CampaignRuleRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CampaignRuleService {

    private final CampaignRuleRepository repository;

    public CampaignRuleService(CampaignRuleRepository repository) {
        this.repository = repository;
    }

    public List<CampaignRuleDto> getRulesByCampaignId(Long campaignId) {
        return repository.findByCampaignId(campaignId)
                .stream()
                .map(rule -> new CampaignRuleDto(
                        rule.getId(),
                        rule.getRuleType(),
                        rule.getOperator(),
                        rule.getExpectedValue(),
                        rule.getMandatory()
                ))
                .toList();
    }
}