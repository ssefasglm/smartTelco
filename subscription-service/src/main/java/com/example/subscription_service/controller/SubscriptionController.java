package com.example.subscription_service.controller;

import com.example.subscription_service.dto.CreateSubscriptionRequest;
import com.example.subscription_service.dto.SubscriptionResponse;
import com.example.subscription_service.service.SubscriptionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {

    private final SubscriptionService service;

    public SubscriptionController(SubscriptionService service) {
        this.service = service;
    }

    @PostMapping
    public SubscriptionResponse createSubscription(@RequestBody CreateSubscriptionRequest request) {
        return service.createSubscription(request.customerId(), request.quoteId());
    }
}