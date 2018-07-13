package priceplans;

import org.springframework.web.servlet.view.RedirectView;
import priceplans.models.Customer;
import priceplans.models.PricePlan;
import priceplans.processors.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import priceplans.util.UnknownEntityException;

@RestController
public class PricePlanController {

    PricingManager pricingManger;
    static boolean setupDone = false;

    public PricePlanController()
    {
        pricingManger = new SimplePricingManager(PricingStateFactory.getPricingStateHandle("LOCAL"));
        setUp(); //for testing
    }

    //test to see service is up
    @GetMapping("/canary")
    public String hello() {
        return "Tweet!" ;
    }

    @GetMapping("/customers/{id}/plan")
    public PricePlan getPlanDetailsForCustomer(@PathVariable("id") String cId) {
        try {
            PricePlan plan = pricingManger.getPlanForCustomer(cId);
            if ( plan != null)
                return plan;
            else
                throw new InternalException("No plan found for a valid customer.");

        } catch (PricingState.NoEntityFoundException e) {
            throw new EntityNotFoundException("No Entity found");
        }
    }


    @GetMapping("/plans/{id}")
    public PricePlan getPlanDetails(@PathVariable("id") String planId)
    {

        PricePlan plan = pricingManger.getPlanById(planId);
        if ( plan == null) throw new UnknownEntityException("No Entity found");
        return plan;
    }



    /*
        Modify details for an existing plan for a country
     */
    @PutMapping("country/{rid}/plans/{name}")
    public PricePlan modifyPlan(@RequestBody PricePlan plan, @PathVariable("rid") String rid, @PathVariable("name") String planName)
    {
        try {
            PricePlan newPlan  = pricingManger.updatePlan(plan);
            return newPlan;
        }catch (UnknownEntityException e)
        {
            throw new UnknownEntityException("No such plan exists.");
        }
    }

    /*
        Modify details for an existing plan for a country
     */
    @GetMapping("regions/{rid}/plans/{name}")
    public PricePlan modifyPlan( @PathVariable("rid") String rid, @PathVariable("name") String planName)
    {
        try {
            return pricingManger.getPlanByNameAndCountry(planName, rid);
        }catch (UnknownEntityException e)
        {
            throw new UnknownEntityException("No such plan exists.");
        }
    }

    /*
        Create a brand new new plan for a country.
     */
    @PostMapping("/plans")
    public RedirectView makePlan(@RequestBody PricePlan plan)
    {
        try {
            PricePlan newPlan = pricingManger.createPlan(plan);
            RedirectView rv = new RedirectView();
            rv.setUrl("/plans/" + newPlan.getId());

            return rv;
        }catch(UnknownEntityException e) {
            throw new UnknownEntityException("No plan was created.");
        }
    }


    private void setUp() {

        if  (PricePlanController.setupDone == false) {
            PricingState s = PricingStateFactory.getPricingStateHandle("LOCAL");

            PricePlan p1 = new PricePlan("1S", "US", 499, "USD", System.currentTimeMillis());
            PricePlan p2 = new PricePlan("2S", "US", 799, "USD", System.currentTimeMillis());
            PricePlan p3 = new PricePlan("4S", "US", 1299, "USD", System.currentTimeMillis());
            PricePlan p4 = new PricePlan("1S", "UK", 399, "GBP", System.currentTimeMillis());
            PricePlan p5 = new PricePlan("4S", "UK", 999, "GBP", System.currentTimeMillis());


            s.createPlan(p1);
            s.createPlan(p2);
            s.createPlan(p3);
            s.createPlan(p4);
            s.createPlan(p5);

            Customer[] customers = {
                    new Customer("1", "US", "1S"),
                    new Customer("2", "US", "1S"),
                    new Customer("3", "UK", "1S"),
                    new Customer("4", "UK", "4S"),
                    new Customer("5", "US", "2S"),
                    new Customer("6", "US", "2S"),
                    new Customer("7", "US", "4S")};

            LocalPricingStateHandle ls = (LocalPricingStateHandle) s;
            ls.addCustomers(customers);
        }

    }


    // HTTP status modeling for error cases

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Resource not found")
    public class EntityNotFoundException extends RuntimeException {

        public EntityNotFoundException(String msg) {
            super(msg);
        }
    }

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Oops! Something went wrong. Please try again later.")
    public class InternalException extends RuntimeException {

        public InternalException(String msg) {
            super(msg);
        }
    }
}

