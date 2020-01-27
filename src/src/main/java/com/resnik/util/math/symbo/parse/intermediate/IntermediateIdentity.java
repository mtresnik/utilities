package com.resnik.util.math.symbo.parse.intermediate;

import com.resnik.util.math.symbo.operations.Operation;

public class IntermediateIdentity extends IntermediateUnaryOperator {

    public IntermediateIdentity(int startIndex, int endIndex, IntermediateOperation operation) {
        super(startIndex, endIndex, operation);
    }

    @Override
    public Operation compile() {
        return operation.compile();
    }
}
