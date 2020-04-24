package com.resnik.util.math.symbo.algebra.parse.intermediate;

import java.util.Collections;

public abstract class IntermediateUnaryOperator extends IntermediateOperator {

    IntermediateOperation operation;

    public IntermediateUnaryOperator(int startIndex, int endIndex, IntermediateOperation operation) {
        super(startIndex, endIndex, Collections.singletonList(operation));
        this.operation = operation;
    }

}
