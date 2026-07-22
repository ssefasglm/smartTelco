package com.example.reference_service.service;

import com.example.reference_service.entity.TariffPlan;
import com.example.reference_service.repository.TariffPlanRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TariffPlanService {

    private final TariffPlanRepository repository;

    public TariffPlanService(TariffPlanRepository repository) {
        this.repository = repository;
    }

    public List<TariffPlan> getAllPlans() {
        return repository.findAll();
    }
    public TariffPlan getPlanByCode(String code) {
        return repository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Plan not found: " + code));
    }
}