package com.resnik.util.math.symbo.operations;

public class DerivativeException extends RuntimeException {
    public static final DerivativeException PRODUCT_EXCEPTION;

    static {
        PRODUCT_EXCEPTION = new DerivativeException("BulkProducts cannot be derived generally. (Try specifying the bounds of the product)");
    }

    public DerivativeException(String cause){
        super(cause);
    }
}
