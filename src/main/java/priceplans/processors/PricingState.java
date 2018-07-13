package priceplans.processors;

import priceplans.models.Customer;
import priceplans.models.PricePlan;

public interface PricingState {

    boolean   planExists(String country2Digit, String planName);
    Customer  getCustomerDetails(String customerId);
    PricePlan getLatestPlanByNameAndCountryOnTime(String planName, String country2Digit, long timestampMs);
    PricePlan getPlanByNameAndCountry(String planName, String country2Digit);
    PricePlan getPlanForCustomerOnTime(String customerId, long timestampMs);
    PricePlan getPlanForCustomer(String customerId);
    PricePlan getPlanById(String planId);

    void createPlan(PricePlan plan);
    void modifyPlan(PricePlan newPlan);



    public static class NoEntityFoundException extends RuntimeException {
        public NoEntityFoundException(String msg) { super(msg); }
    }

    public static class InvalidInputException extends RuntimeException {
        public InvalidInputException(String msg) { super(msg); }
    }

}
