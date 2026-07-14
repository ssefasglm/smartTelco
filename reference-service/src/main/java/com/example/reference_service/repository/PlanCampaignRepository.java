package com.example.reference_service.repository;

import com.example.reference_service.entity.PlanCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlanCampaignRepository extends JpaRepository<PlanCampaign, Long> {

    List<PlanCampaign> findByPlanId(Long planId);
}