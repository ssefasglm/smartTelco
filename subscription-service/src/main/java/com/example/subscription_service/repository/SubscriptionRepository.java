package com.example.subscription_service.repository;

import com.example.subscription_service.document.Subscription;
import com.example.subscription_service.enums.SubscriptionStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends MongoRepository<Subscription, String> {

    Optional<Subscription> findBySubscriptionId(String subscriptionId);

    boolean existsByCustomerIdAndStatus(String customerId, SubscriptionStatus status);

    List<Subscription> findByCustomerId(String customerId);
}