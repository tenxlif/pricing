package priceplans.models;

// marker interface with exception definitions
public interface PricingEntity {


    public static class CantCreateEntityException extends RuntimeException{
        public CantCreateEntityException(String msg)
        {
            super(msg);
        }
    }
}
