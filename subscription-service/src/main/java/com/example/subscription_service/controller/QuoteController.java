package com.example.subscription_service.controller;

import com.example.subscription_service.dto.CreateQuoteRequest;
import com.example.subscription_service.dto.QuoteResponse;
import com.example.subscription_service.service.QuoteService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/quotes")
public class QuoteController {

    private final QuoteService service;

    public QuoteController(QuoteService service) {
        this.service = service;
    }

    @PostMapping
    public QuoteResponse createQuote(@RequestBody CreateQuoteRequest request) {
        return service.createQuote(request.customerId(), request.planCode());
    }
}