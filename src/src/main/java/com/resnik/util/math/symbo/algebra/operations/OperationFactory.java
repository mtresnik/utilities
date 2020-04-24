package com.resnik.util.math.symbo.algebra.operations;

@FunctionalInterface
public interface OperationFactory<T extends Operation> {

    T create(Operation[] values);

}
