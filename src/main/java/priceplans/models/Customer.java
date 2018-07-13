package priceplans.models;

import priceplans.processors.LocalPricingStateHandle;
import priceplans.processors.PricingState;

import java.util.UUID;

public class Customer implements PricingEntity{
    String customerId;
    String country2Digit;
    String planName;

    public Customer(String customerId, String country2Digit, String planName) {

        //if ( STATE.planExists(country2Digit, planName))
        //{
            this.country2Digit=country2Digit;
            this.planName=planName;
        //}

        if ( customerId == null )
            this.customerId = UUID.randomUUID().toString(); //todo
        else
            this.customerId=customerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCountry2Digit() {
        return country2Digit;
    }

    public String getPlanName() {
        return planName;
    }
}
