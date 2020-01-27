package com.resnik.util.math.symbo.parse.intermediate;

import com.resnik.util.math.symbo.operations.Operation;
import com.resnik.util.math.symbo.operations.base.Division;

public class IntermediateDivision extends IntermediateBinaryOperator {

    public IntermediateDivision(int startIndex, int endIndex, IntermediateOperation op1, IntermediateOperation op2) {
        super(startIndex, endIndex, op1, op2);
    }

    @Override
    public Operation compile() {
        return new Division(op1.compile(), op2.compile());
    }
}
