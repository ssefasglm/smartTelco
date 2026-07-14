package com.example.reference_service.repository;

import com.example.reference_service.entity.TaxDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaxDefinitionRepository extends JpaRepository<TaxDefinition, Long> {

    Optional<TaxDefinition> findByCode(String code);}