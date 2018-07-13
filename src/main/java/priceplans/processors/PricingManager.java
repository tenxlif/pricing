package priceplans.processors;

import priceplans.models.PricePlan;

public interface PricingManager {

     PricePlan getPlanForCustomer(String customerId);

     PricePlan getPlanForCustomerOnTime(String customerId, long timestampMs);

     PricePlan updatePlan(PricePlan newPlan);

     PricePlan createPlan(PricePlan plan);

     PricePlan getPlanById(String planId);

     PricePlan getPlanByNameAndCountry(String planName, String country2Digit);

    PricePlan getLatestPlanByNameAndCountryOnTime(String planName, String country2Digit, long timestampMs) ;


}
