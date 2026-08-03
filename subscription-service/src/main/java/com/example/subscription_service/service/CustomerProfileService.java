package com.example.subscription_service.service;

import com.example.subscription_service.document.CustomerProfile;
import com.example.subscription_service.dto.CreateCustomerRequest;
import com.example.subscription_service.dto.CustomerResponse;
import com.example.subscription_service.repository.CustomerProfileRepository;
import org.springframework.stereotype.Service;
import com.example.subscription_service.exception.NotFoundException;
import com.example.subscription_service.exception.ConflictException;

@Service
public class CustomerProfileService {

    private final CustomerProfileRepository repository;

    public CustomerProfileService(CustomerProfileRepository repository) {
        this.repository = repository;
    }

    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        if (repository.findByCustomerId(request.customerId()).isPresent()) {
            throw new ConflictException("Customer already exists: " + request.customerId());
        }
        CustomerProfile profile = new CustomerProfile();
        profile.setCustomerId(request.customerId());
        profile.setAge(request.age());
        profile.setSegment(request.segment());
        profile.setTenureMonths(request.tenureMonths());
        profile.setCurrentPlanCode(request.currentPlanCode());
        profile.setAverageDataUsageGb(request.averageDataUsageGb());
        profile.setMonthlyBudget(request.monthlyBudget());
        profile.setCommitmentEndDate(request.commitmentEndDate());

        CustomerProfile saved = repository.save(profile);

        return toResponse(saved);
    }

    public CustomerResponse getByCustomerId(String customerId) {
        CustomerProfile profile = repository.findByCustomerId(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found: " + customerId));

        return toResponse(profile);
    }

    private CustomerResponse toResponse(CustomerProfile profile) {
        return new CustomerResponse(
                profile.getId(),
                profile.getCustomerId(),
                profile.getAge(),
                profile.getSegment(),
                profile.getTenureMonths(),
                profile.getCurrentPlanCode(),
                profile.getAverageDataUsageGb(),
                profile.getMonthlyBudget(),
                profile.getCommitmentEndDate()
        );
    }
}