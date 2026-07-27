package com.example.subscription_service.eligibility;

import com.example.subscription_service.document.CustomerProfile;
import com.example.subscription_service.dto.CampaignRuleResponse;
import com.example.subscription_service.dto.RuleEvaluationResult;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class NoActiveCommitmentEvaluator implements RuleEvaluator {

    @Override
    public String getRuleType() {
        return "NO_ACTIVE_COMMITMENT";
    }

    @Override
    public RuleEvaluationResult evaluate(CustomerProfile customer, CampaignRuleResponse rule) {
        LocalDate commitmentEnd = customer.getCommitmentEndDate();

        // Taahhüt yoksa ya da bitiş tarihi geçmişteyse -> aktif taahhüt yok -> kural sağlanır
        boolean hasActiveCommitment =
                commitmentEnd != null && commitmentEnd.isAfter(LocalDate.now());
        boolean satisfied = !hasActiveCommitment;

        String detail;
        if (commitmentEnd == null) {
            detail = "Aktif taahhüt yok, sağlandı";
        } else if (hasActiveCommitment) {
            detail = "Taahhüt " + commitmentEnd + " tarihine kadar aktif, sağlanmadı";
        } else {
            detail = "Taahhüt " + commitmentEnd + " tarihinde bitmiş, sağlandı";
        }

        return new RuleEvaluationResult("NO_ACTIVE_COMMITMENT", satisfied, detail);
    }
}