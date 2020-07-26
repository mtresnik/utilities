package com.resnik.util.math.symbo.logic.parse.intermediate;

import com.resnik.util.math.symbo.logic.operations.LogicalOperation;
import com.resnik.util.math.symbo.logic.operations.Xor;

public class LogicalIntermediateXor extends LogicalIntermediateBinaryOperator {
    public LogicalIntermediateXor(int startIndex, int endIndex, LogicalIntermediateOperation op1, LogicalIntermediateOperation op2) {
        super(startIndex, endIndex, op1, op2);
    }

    @Override
    public LogicalOperation compile() {
        return new Xor(op1.compile(), op2.compile());
    }
}
