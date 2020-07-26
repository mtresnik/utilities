package com.resnik.util.math.symbo.algebra.parse.intermediate;

import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.math.symbo.algebra.operations.base.SymbolicNegation;

public class SymbolicIntermediateNegation extends SymbolicIntermediateUnaryOperator {

    public SymbolicIntermediateNegation(int startIndex, int endIndex, SymbolicIntermediateOperation operation) {
        super(startIndex, endIndex, operation);
    }

    @Override
    public Operation compile() {
        return new SymbolicNegation(operation.compile());
    }
}
