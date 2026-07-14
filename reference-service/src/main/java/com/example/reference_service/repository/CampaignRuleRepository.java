package com.example.reference_service.repository;

import com.example.reference_service.entity.CampaignRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CampaignRuleRepository extends JpaRepository<CampaignRule, Long> {

    List<CampaignRule> findByCampaignId(Long campaignId);
}