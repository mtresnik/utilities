package util.math.symbo;


public class IntegrationException extends RuntimeException{
    
    public static final IntegrationException TRIG_EXCEPTION, LOG_EXCEPTION;
    static{
        TRIG_EXCEPTION = new IntegrationException("Trig functions cannot be integrated generally.");
        LOG_EXCEPTION = new IntegrationException("Logarithms cannot be integrated generally.");
    }
    
    public IntegrationException(String cause){
        super(cause);
    }

}
