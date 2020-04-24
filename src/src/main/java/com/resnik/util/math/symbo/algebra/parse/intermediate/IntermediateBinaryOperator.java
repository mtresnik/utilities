package com.resnik.util.math.symbo.algebra.parse.intermediate;

import java.util.Arrays;

public abstract class IntermediateBinaryOperator extends IntermediateOperator {
    IntermediateOperation op1;
    IntermediateOperation op2;
    public IntermediateBinaryOperator(int startIndex, int endIndex, IntermediateOperation op1, IntermediateOperation op2) {
        super(startIndex, endIndex, Arrays.asList(op1, op2));
        this.op1 = op1;
        this.op2 = op2;
    }
}
