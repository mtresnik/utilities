package com.resnik.util.math.symbo.parse.intermediate;

import com.resnik.util.math.symbo.operations.Operation;
import com.resnik.util.math.symbo.operations.base.Parentheses;

public class IntermediateParentheses extends IntermediateUnaryOperator {

    public IntermediateParentheses(int startIndex, int endIndex, IntermediateOperation operation) {
        super(startIndex, endIndex, operation);
    }

    @Override
    public Operation compile() {
        return new Parentheses(operation.compile());
    }
}
