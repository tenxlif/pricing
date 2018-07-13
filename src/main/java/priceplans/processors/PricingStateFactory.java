package priceplans.processors;

public class PricingStateFactory {

    public PricingStateFactory() {}

    public static PricingState getPricingStateHandle(String handleType)
    {
        if ( handleType.equals("LOCAL"))
        {
            return  LocalPricingStateHandle.getPricingState();
        }
        return null;
    }
}
