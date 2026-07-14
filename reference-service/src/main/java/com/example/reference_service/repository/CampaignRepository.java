package com.example.reference_service.repository;

import com.example.reference_service.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    Optional<Campaign> findByCode(String code);
}