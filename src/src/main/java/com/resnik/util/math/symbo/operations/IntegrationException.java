package com.resnik.util.math.symbo.operations;


public class IntegrationException extends RuntimeException{
    
    public static final IntegrationException TRIG_EXCEPTION, LOG_EXCEPTION, PRODUCT_EXCEPTION;
    static{
        TRIG_EXCEPTION = new IntegrationException("Trig functions cannot be integrated generally.");
        LOG_EXCEPTION = new IntegrationException("Logarithms cannot be integrated generally.");
        PRODUCT_EXCEPTION = new IntegrationException("BulkProducts cannot be integrated generally.");
    }
    
    public IntegrationException(String cause){
        super(cause);
    }

}
