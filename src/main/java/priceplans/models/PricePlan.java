package priceplans.models;

import priceplans.util.Util;

import java.util.UUID;

public class PricePlan implements PricingEntity{

    private String id;
    private String planName;
    private String country2Digit;
    private int pricePennies;
    private String currency;
    private long effectiveStartTs;


    public PricePlan(String planName, String country2Digit, int pricePennies, String currency, long effectiveStartTs)
    {

        if ((   planName == null) || planName.length() == 0 ||
                country2Digit == null || country2Digit.length() == 0 ||
                pricePennies < 0 ||
                effectiveStartTs < 0 )
            throw new CantCreateEntityException("Invalid Plan details.");

        boolean found=false;
        for ( String s: Util.PLANS)
        {
            if ( planName.equals(s)) found=true;
        }
        if ( found == false) throw new CantCreateEntityException("Invalid Plan details.");



        this.planName=planName;
        this.country2Digit=country2Digit;
        this.pricePennies=pricePennies;
        this.currency=currency;
        this.effectiveStartTs=effectiveStartTs;
        this.id= UUID.randomUUID().toString(); //TODO change it
    }




    public String getId() {
        return id;
    }

    public String getPlanName() {
        return planName;
    }


    public String getCountry2Digit() {
        return country2Digit;
    }


    public int getPricePennies() {
        return pricePennies;
    }

    public String getCurrency() {
        return currency;
    }

    public long getEffectiveStartTs() {
        return effectiveStartTs;
    }


}
