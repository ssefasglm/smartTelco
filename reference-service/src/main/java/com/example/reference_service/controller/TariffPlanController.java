package com.example.reference_service.controller;

import com.example.reference_service.entity.TariffPlan;
import com.example.reference_service.service.TariffPlanService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/plans")
public class TariffPlanController {

    private final TariffPlanService service;

    public TariffPlanController(TariffPlanService service) {
        this.service = service;
    }

    @GetMapping
    public List<TariffPlan> getAllPlans() {
        return service.getAllPlans();
    }
}