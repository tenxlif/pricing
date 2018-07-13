package priceplans.processors;

import priceplans.models.PricePlan;


public class SimplePricingManager implements PricingManager {

    PricingState STATE  ;

    public SimplePricingManager(PricingState ps)
    {
        STATE = ps;
    }

    @Override
    public PricePlan getPlanForCustomer(String customerId) {
        return STATE.getPlanForCustomer(customerId);
    }

    @Override
    public PricePlan getPlanForCustomerOnTime(String customerId, long timestampMs) {
        return STATE.getPlanForCustomerOnTime(customerId,timestampMs);

    }

    @Override
    public PricePlan updatePlan(PricePlan newPlan) {
        STATE.modifyPlan(newPlan);
        return STATE.getPlanByNameAndCountry(newPlan.getPlanName(), newPlan.getCountry2Digit()); //race condition
    }

    @Override
    public PricePlan createPlan(PricePlan plan) {
        STATE.createPlan(plan);
        return STATE.getPlanByNameAndCountry(plan.getPlanName(), plan.getCountry2Digit()); //race condition

    }

    @Override
    public PricePlan getPlanById(String planId) {
        return STATE.getPlanById(planId);
    }

    @Override
    public PricePlan getPlanByNameAndCountry(String planName, String country2Digit) {
        return STATE.getPlanByNameAndCountry(planName, country2Digit);

    }

    @Override
    public PricePlan getLatestPlanByNameAndCountryOnTime(String planName, String country2Digit, long timestampMs) {
       return STATE.getLatestPlanByNameAndCountryOnTime(planName,country2Digit,timestampMs);
    }

}
