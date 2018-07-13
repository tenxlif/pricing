package priceplans.processors;

import priceplans.models.Customer;
import priceplans.models.PricePlan;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class LocalPricingStateHandle implements PricingState{

    public static Map<String,Customer> customerMap ;
    public static Map<String,Map<String,Stack<PricePlan>>> plansByCountry;
    public static Map<String,PricePlan> plans;
    static {
        customerMap = new HashMap<String, Customer>();
        plansByCountry = new HashMap<>();
        plans= new HashMap<>();
    }

    private static LocalPricingStateHandle instance = new LocalPricingStateHandle();

    private LocalPricingStateHandle() {} ;

    private static void makeInstance()
    {
        instance = new LocalPricingStateHandle();
    }

    public static PricingState getPricingState()
    {
        if ( instance == null)
        {
           makeInstance();
        }
        return instance;
    }


    @Override
    public boolean planExists(String country2Digit, String planName) {
        return planName!= null && plansByCountry.get(planName) != null && country2Digit != null && plansByCountry.get(planName).get(country2Digit) != null;
    }

    @Override
    public Customer getCustomerDetails(String customerId) {
        if ( customerId == null) return null;
        return customerMap.get(customerId);
    }

    @Override
    public PricePlan getLatestPlanByNameAndCountryOnTime(String planName, String country2Digit, long timestampMs) {
        Stack<PricePlan> plans = getPlans(planName,country2Digit);
        if  (plans == null || plans.size() == 0) return null; //MUST throw exception;. This shouldnt happen
        Stack<PricePlan> temp = new Stack<PricePlan>();
        PricePlan p = plans.peek();
        while ( plans.size() > 0 && p.getEffectiveStartTs() > timestampMs) {
            temp.push(plans.pop());
        }


        if ( plans.size() == 0 )
        {
            while(temp.size() > 0) { plans.push(temp.pop()); }
            return null;
        }else{
            PricePlan result = temp.peek();
            while(temp.size() > 0) { plans.push(temp.pop()); }
            return result;
        }
    }

    @Override
    public PricePlan getPlanByNameAndCountry(String planName, String country2Digit) {
        Stack<PricePlan> plans = getPlans(planName,country2Digit);
        if  (plans == null || plans.size() == 0) return null; //MUST throw exception;. This shouldnt happen
        return plans.peek();
    }

    @Override
    public PricePlan getPlanForCustomerOnTime(String customerId, long timestampMs) {
        Customer customer = getCustomerDetails(customerId);
        if ( customer != null)
        {
            String planName = customer.getPlanName();
            String country2Digit = customer.getCountry2Digit();

            return getLatestPlanByNameAndCountryOnTime(planName,country2Digit, timestampMs);
        }
        return null;
    }

    @Override
    public PricePlan getPlanForCustomer(String customerId) {
        Customer customer = getCustomerDetails(customerId);
        if ( customer != null)
        {
            String planName = customer.getPlanName();
            String country2Digit = customer.getCountry2Digit();

            return getPlanByNameAndCountry(planName,country2Digit);
        }
        throw new NoEntityFoundException("Customer not found");
    }

    @Override
    public PricePlan getPlanById(String planId) {
        if ( planId == null) throw new InvalidInputException("Null plan not allowed");
        return plans.get(planId);
    }

    @Override
    public void createPlan(PricePlan plan) {
        String country2Digit = plan.getCountry2Digit();
        String planName= plan.getPlanName();


        Map<String,Stack<PricePlan>> countryMap = plansByCountry.get(planName);
        if ( countryMap == null)
        {
            plansByCountry.put(planName, new HashMap<String,Stack<PricePlan>>() );
        }

        countryMap = plansByCountry.get(planName);
        Stack<PricePlan> s = countryMap.get(country2Digit);
        if ( s == null)
        {
            countryMap.put(country2Digit,new Stack<PricePlan>());
        }else{
            //must throw some exception. There should not be any plan
        }

        s=countryMap.get(country2Digit);
        s.push(plan);

        plans.put(plan.getId(),plan);
    }

    @Override
    public void modifyPlan(PricePlan newPlan) {
        if ( ! planExists(newPlan.getCountry2Digit(), newPlan.getPlanName())) throw new RuntimeException("Invalid Plan");

        //assert(plansByCountry.get(newPlan.getPlanName()) != null);
        //assert(plansByCountry.get(newPlan.getPlanName()).get(newPlan.getCountry2Digit()) != null);

        Stack<PricePlan> s= plansByCountry.get(newPlan.getPlanName()).get(newPlan.getCountry2Digit());
        s.push(newPlan);

        plans.put(newPlan.getId(),newPlan);
    }

    private  Stack<PricePlan> getPlans(String planName, String country2Digit)
    {
        if (planName == null) return null;
        if ( country2Digit == null) return null;
        return plansByCountry.get(planName).get(country2Digit);

    }


    /* for testing alone */
    public void resetState()
    {
        customerMap.clear();
        plansByCountry.clear();
        plans.clear();
    }

    /* for testing alone */
    public void addCustomers(Customer[] customers)
    {
        for( Customer  customer : customers)
            customerMap.put(customer.getCustomerId(),customer);
    }
}
