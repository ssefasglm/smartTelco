package com.example.subscription_service.repository;

import com.example.subscription_service.document.Quote;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface QuoteRepository extends MongoRepository<Quote, String> {

    Optional<Quote> findByQuoteId(String quoteId);
}