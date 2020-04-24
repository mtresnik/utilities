package com.resnik.util.math.symbo.algebra.parse.intermediate;

import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.math.symbo.algebra.operations.base.Subtraction;

public class IntermediateSubtraction extends IntermediateBinaryOperator{

    public IntermediateSubtraction(int startIndex, int endIndex, IntermediateOperation op1, IntermediateOperation op2) {
        super(startIndex, endIndex, op1, op2);
    }

    @Override
    public Operation compile() {
        return new Subtraction(op1.compile(), op2.compile());
    }
}
