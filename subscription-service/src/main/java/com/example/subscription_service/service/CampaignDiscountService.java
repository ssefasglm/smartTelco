package com.example.subscription_service.service;

import com.example.subscription_service.client.ReferenceServiceClient;
import com.example.subscription_service.document.CustomerProfile;
import com.example.subscription_service.dto.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class CampaignDiscountService {

    private final ReferenceServiceClient referenceServiceClient;
    private final EligibilityService eligibilityService;

    public CampaignDiscountService(ReferenceServiceClient referenceServiceClient,
                                   EligibilityService eligibilityService) {
        this.referenceServiceClient = referenceServiceClient;
        this.eligibilityService = eligibilityService;
    }

    public DiscountDecision decideDiscount(Long planId,
                                           CustomerProfile customer,
                                           BigDecimal baseAmount) {

        // 1. Tarifenin bağlı olduğu kampanya bağlantılarını al
        List<PlanCampaignResponse> planCampaigns =
                referenceServiceClient.getPlanCampaignsByPlanId(planId);

        List<CampaignEligibilityResult> evaluations = new ArrayList<>();

        String bestCampaignCode = null;
        BigDecimal bestDiscount = BigDecimal.ZERO;
        Integer bestPriority = null;

        // 2. Her kampanya için: detayını + kurallarını çek, değerlendir
        for (PlanCampaignResponse link : planCampaigns) {

            CampaignResponse campaign = referenceServiceClient.getCampaignById(link.campaignId());

            List<CampaignRuleResponse> rules = referenceServiceClient.getRulesByCampaignId(link.campaignId());

            CampaignEligibilityResult evaluation = eligibilityService.evaluateCampaign(campaign.code(), customer, rules);
            evaluations.add(evaluation);

            // 3. Uygunsa, bu kampanyanın indirimini hesapla
            if (evaluation.eligible()) {
                BigDecimal discount = calculateDiscount(campaign, baseAmount);

                // 4. Seçim: önce priority yüksek olan, eşitlikte indirimi yüksek olan
                boolean isBetter =
                        bestPriority == null
                                || campaign.priority() > bestPriority
                                || (campaign.priority().equals(bestPriority)
                                && discount.compareTo(bestDiscount) > 0);

                if (isBetter) {
                    bestPriority = campaign.priority();
                    bestDiscount = discount;
                    bestCampaignCode = campaign.code();
                }
            }
        }

        return new DiscountDecision(bestCampaignCode, bestDiscount, evaluations);
    }

    BigDecimal calculateDiscount(CampaignResponse campaign, BigDecimal baseAmount) {
        if ("PERCENTAGE".equals(campaign.discountType())) {
            return baseAmount
                    .multiply(campaign.discountValue())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else if ("FIXED_AMOUNT".equals(campaign.discountType())) {
            return campaign.discountValue().setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }
}