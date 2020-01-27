package com.resnik.util.math.symbo.operations;

@FunctionalInterface
public interface OperationFactory<T extends Operation> {

    T create(Operation[] values);

}
