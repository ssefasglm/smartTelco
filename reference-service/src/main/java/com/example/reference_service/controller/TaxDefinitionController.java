package com.example.reference_service.controller;

import com.example.reference_service.entity.TaxDefinition;
import com.example.reference_service.service.TaxDefinitionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/taxes")
public class TaxDefinitionController {
    private final TaxDefinitionService service;

    public TaxDefinitionController(TaxDefinitionService service) {
        this.service = service;
    }

    @GetMapping
    public List<TaxDefinition> getAllTaxDefinitions() {
        return service.getAllTaxDefinitions();
    }
}
