package priceplans.processors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import priceplans.models.Customer;
import priceplans.models.PricePlan;

import static org.junit.Assert.*;

public class SimplePricingManagerTest {

    PricingState s ;



    @Before
    public void setUp() throws Exception {

        s = PricingStateFactory.getPricingStateHandle("LOCAL");

        PricePlan p1= new PricePlan("1S","US",499,"USD", System.currentTimeMillis());
        PricePlan p2= new PricePlan("2S","US",799,"USD", System.currentTimeMillis());
        PricePlan p3= new PricePlan("4S","US",1299,"USD", System.currentTimeMillis());
        PricePlan p4= new PricePlan("1S","UK",399,"GBP", System.currentTimeMillis());
        PricePlan p5= new PricePlan("4S","UK",999,"GBP", System.currentTimeMillis());


        s.createPlan(p1);
        s.createPlan(p2);
        s.createPlan(p3);
        s.createPlan(p4);
        s.createPlan(p5);

        Customer[] customers =  {
            new Customer("1", "US", "1S"),
            new Customer("2", "US", "1S"),
            new Customer("3", "UK", "1S"),
            new Customer("4", "UK", "4S"),
            new Customer("5", "US", "2S"),
            new Customer("6", "US", "2S"),
            new Customer("7", "US", "4S") };

        LocalPricingStateHandle ls = (LocalPricingStateHandle)s;
        ls.addCustomers(customers);


    }

    @After
    public void tearDown() throws Exception {
        LocalPricingStateHandle ls = (LocalPricingStateHandle)s;
        ((LocalPricingStateHandle) s).resetState();
    }



    @Test
    public void getPlanForCustomer() {
        PricingManager pm =  new SimplePricingManager(s);
        PricePlan p = pm.getPlanForCustomer("1");
        assertEquals("PricePlanEqual", p.getPricePennies(), 499);
        assertEquals("PricePlanEqual", p.getCurrency(), "USD");
        assertEquals("PricePlanEqual", p.getCountry2Digit(), "US");

        p = pm.getPlanForCustomer("4");
        assertEquals("PricePlanEqual", p.getPricePennies(), 999);
        assertEquals("PricePlanEqual", p.getCurrency(), "GBP");
        assertEquals("PricePlanEqual", p.getCountry2Digit(), "UK");
    }


    @Test
    public void getPlanForCustomerOnTime() {
    }

    @Test
    public void updatePlan() {
        PricingManager pm =  new SimplePricingManager(s);

        PricePlan p = pm.getPlanForCustomer("1");
        assertEquals("PricePlanEqual", p.getPricePennies(), 499);
        assertEquals("PricePlanEqual", p.getCurrency(), "USD");
        assertEquals("PricePlanEqual", p.getCountry2Digit(), "US");

        PricePlan p2 = new PricePlan("1S","US",399,"USD", System.currentTimeMillis());
        pm.updatePlan(p2);
        p = pm.getPlanForCustomer("1");
        assertEquals("PricePlanEqual", p.getPricePennies(), 399);
        assertEquals("PricePlanEqual", p.getCurrency(), "USD");
        assertEquals("PricePlanEqual", p.getCountry2Digit(), "US");

    }

    @Test
    public void createPlan() {

    }

    @Test
    public void getPlanById() {
    }

    @Test
    public void getPlanByNameAndCountry() {

    }

    @Test
    public void getLatestPlanByNameAndCountryOnTime() {
    }
}