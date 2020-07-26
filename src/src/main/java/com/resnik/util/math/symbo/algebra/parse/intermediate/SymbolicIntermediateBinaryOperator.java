package com.resnik.util.math.symbo.algebra.parse.intermediate;

import java.util.Arrays;

public abstract class SymbolicIntermediateBinaryOperator extends SymbolicIntermediateOperator {
    SymbolicIntermediateOperation op1;
    SymbolicIntermediateOperation op2;
    public SymbolicIntermediateBinaryOperator(int startIndex, int endIndex, SymbolicIntermediateOperation op1, SymbolicIntermediateOperation op2) {
        super(startIndex, endIndex, Arrays.asList(op1, op2));
        this.op1 = op1;
        this.op2 = op2;
    }
}
