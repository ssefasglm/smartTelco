package com.example.subscription_service.repository;

import com.example.subscription_service.document.Subscription;
import com.example.subscription_service.dto.SubscriptionResponse;
import com.example.subscription_service.enums.SubscriptionStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.subscription_service.enums.SubscriptionStatus;

import java.util.Optional;

public interface SubscriptionRepository extends MongoRepository<Subscription, String> {

    Optional<Subscription> findBySubscriptionId(String subscriptionId);

    Optional<Subscription> findByCustomerIdAndStatus(String customerID, SubscriptionStatus status);
}