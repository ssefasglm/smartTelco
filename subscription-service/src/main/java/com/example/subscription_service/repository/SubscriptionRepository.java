package com.example.subscription_service.repository;

import com.example.subscription_service.document.Subscription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SubscriptionRepository extends MongoRepository<Subscription, String> {

    Optional<Subscription> findBySubscriptionId(String subscriptionId);
}