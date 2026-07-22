package com.example.reference_service.repository;

import com.example.reference_service.entity.TariffPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface TariffPlanRepository extends JpaRepository<TariffPlan, Long> {

    Optional<TariffPlan> findByCode(String code);

}