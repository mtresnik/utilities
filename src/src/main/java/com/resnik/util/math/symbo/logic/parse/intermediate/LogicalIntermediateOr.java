package com.resnik.util.math.symbo.logic.parse.intermediate;

import com.resnik.util.math.symbo.logic.operations.LogicalOperation;
import com.resnik.util.math.symbo.logic.operations.Or;

public class LogicalIntermediateOr extends LogicalIntermediateBinaryOperator {
    public LogicalIntermediateOr(int startIndex, int endIndex, LogicalIntermediateOperation op1, LogicalIntermediateOperation op2) {
        super(startIndex, endIndex, op1, op2);
    }

    @Override
    public LogicalOperation compile() {
        return new Or(op1.compile(), op2.compile());
    }
}
