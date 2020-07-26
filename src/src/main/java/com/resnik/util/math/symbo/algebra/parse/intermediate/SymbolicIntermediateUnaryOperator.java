package com.resnik.util.math.symbo.algebra.parse.intermediate;

import java.util.Collections;

public abstract class SymbolicIntermediateUnaryOperator extends SymbolicIntermediateOperator {

    SymbolicIntermediateOperation operation;

    public SymbolicIntermediateUnaryOperator(int startIndex, int endIndex, SymbolicIntermediateOperation operation) {
        super(startIndex, endIndex, Collections.singletonList(operation));
        this.operation = operation;
    }

}
