package com.resnik.util.math.symbo.parse.intermediate;

import com.resnik.util.math.symbo.operations.Operation;
import com.resnik.util.math.symbo.operations.base.Addition;
import com.resnik.util.math.symbo.parse.SymbolicTokenType;
import com.resnik.util.text.Token;

import java.util.List;

public class IntermediateAddition extends IntermediateBinaryOperator {

    public IntermediateAddition(int startIndex, int endIndex, IntermediateOperation op1, IntermediateOperation op2) {
        super(startIndex, endIndex, op1, op2);
    }

    @Override
    public Operation compile() {
        return new Addition(op1.compile(), op2.compile());
    }
}
