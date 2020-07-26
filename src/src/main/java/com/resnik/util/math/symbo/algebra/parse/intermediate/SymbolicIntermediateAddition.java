package com.resnik.util.math.symbo.algebra.parse.intermediate;

import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.math.symbo.algebra.operations.base.Addition;

public class SymbolicIntermediateAddition extends SymbolicIntermediateBinaryOperator {

    public SymbolicIntermediateAddition(int startIndex, int endIndex, SymbolicIntermediateOperation op1, SymbolicIntermediateOperation op2) {
        super(startIndex, endIndex, op1, op2);
    }

    @Override
    public Operation compile() {
        return new Addition(op1.compile(), op2.compile());
    }
}
