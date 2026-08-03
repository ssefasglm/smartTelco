package com.example.subscription_service.controller;

import com.example.subscription_service.dto.CreateSubscriptionRequest;
import com.example.subscription_service.dto.SubscriptionResponse;
import com.example.subscription_service.service.SubscriptionService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {

    private final SubscriptionService service;

    public SubscriptionController(SubscriptionService service) {
        this.service = service;
    }

    @PostMapping
    public SubscriptionResponse createSubscription(@Valid @RequestBody CreateSubscriptionRequest request) {
        return service.createSubscription(request.customerId(), request.quoteId());
    }

    @GetMapping("/{subscriptionId}")
    public SubscriptionResponse getSubscription(@PathVariable String subscriptionId) {
        return service.getBySubscriptionId(subscriptionId);
    }
}