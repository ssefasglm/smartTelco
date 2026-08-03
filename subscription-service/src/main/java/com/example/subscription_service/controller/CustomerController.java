package com.example.subscription_service.controller;

import com.example.subscription_service.dto.CreateCustomerRequest;
import com.example.subscription_service.dto.CustomerResponse;
import com.example.subscription_service.service.CustomerProfileService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerProfileService service;

    public CustomerController(CustomerProfileService service) {
        this.service = service;
    }

    @PostMapping
    public CustomerResponse createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        return service.createCustomer(request);
    }

    @GetMapping("/{customerId}")
    public CustomerResponse getCustomer(@PathVariable String customerId) {
        return service.getByCustomerId(customerId);
    }
}