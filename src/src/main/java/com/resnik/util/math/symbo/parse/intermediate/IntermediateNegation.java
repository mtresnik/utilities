package com.resnik.util.math.symbo.parse.intermediate;

import com.resnik.util.math.symbo.operations.Operation;
import com.resnik.util.math.symbo.operations.base.Negation;

public class IntermediateNegation extends IntermediateUnaryOperator {

    public IntermediateNegation(int startIndex, int endIndex, IntermediateOperation operation) {
        super(startIndex, endIndex, operation);
    }

    @Override
    public Operation compile() {
        return new Negation(operation.compile());
    }
}
