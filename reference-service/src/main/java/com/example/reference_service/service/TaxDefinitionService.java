package com.example.reference_service.service;

import com.example.reference_service.entity.TaxDefinition;
import com.example.reference_service.repository.TaxDefinitionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaxDefinitionService {
    private final TaxDefinitionRepository repository;
     public TaxDefinitionService(TaxDefinitionRepository repository){
         this.repository = repository;
     }
    public List<TaxDefinition> getAllTaxDefinitions() {
        return repository.findAll();
    }
    public TaxDefinition getCurrentTax() {
        return repository.findFirstByActiveTrue()
                .orElseThrow(() -> new RuntimeException("No active tax definition found"));
    }
}
