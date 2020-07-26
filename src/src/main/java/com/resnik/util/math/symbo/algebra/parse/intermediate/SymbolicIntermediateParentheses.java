package com.resnik.util.math.symbo.algebra.parse.intermediate;

import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.math.symbo.algebra.operations.base.Parentheses;

public class SymbolicIntermediateParentheses extends SymbolicIntermediateUnaryOperator {

    public SymbolicIntermediateParentheses(int startIndex, int endIndex, SymbolicIntermediateOperation operation) {
        super(startIndex, endIndex, operation);
    }

    @Override
    public Operation compile() {
        return new Parentheses(operation.compile());
    }
}
