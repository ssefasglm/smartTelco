package com.example.subscription_service.repository;

import com.example.subscription_service.document.CustomerProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CustomerProfileRepository extends MongoRepository<CustomerProfile, String> {

    Optional<CustomerProfile> findByCustomerId(String customerId);
}