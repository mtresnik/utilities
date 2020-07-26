package com.resnik.util.math.symbo.algebra.parse.intermediate;

import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.math.symbo.algebra.operations.base.Division;

public class SymbolicIntermediateDivision extends SymbolicIntermediateBinaryOperator {

    public SymbolicIntermediateDivision(int startIndex, int endIndex, SymbolicIntermediateOperation op1, SymbolicIntermediateOperation op2) {
        super(startIndex, endIndex, op1, op2);
    }

    @Override
    public Operation compile() {
        return new Division(op1.compile(), op2.compile());
    }
}
