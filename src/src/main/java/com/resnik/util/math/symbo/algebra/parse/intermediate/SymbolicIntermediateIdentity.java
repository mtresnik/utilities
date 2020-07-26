package com.resnik.util.math.symbo.algebra.parse.intermediate;

import com.resnik.util.math.symbo.algebra.operations.Operation;

public class SymbolicIntermediateIdentity extends SymbolicIntermediateUnaryOperator {

    public SymbolicIntermediateIdentity(int startIndex, int endIndex, SymbolicIntermediateOperation operation) {
        super(startIndex, endIndex, operation);
    }

    @Override
    public Operation compile() {
        return operation.compile();
    }
}
