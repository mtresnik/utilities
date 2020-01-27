package com.resnik.util.math.symbo.parse.intermediate;

import java.util.Collections;
import java.util.List;

public abstract class IntermediateUnaryOperator extends IntermediateOperator {

    IntermediateOperation operation;

    public IntermediateUnaryOperator(int startIndex, int endIndex, IntermediateOperation operation) {
        super(startIndex, endIndex, Collections.singletonList(operation));
        this.operation = operation;
    }

}
