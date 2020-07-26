package com.resnik.util.math.symbo.logic.parse.intermediate;

import com.resnik.util.math.symbo.logic.operations.And;
import com.resnik.util.math.symbo.logic.operations.LogicalOperation;

public class LogicalIntermediateAnd extends LogicalIntermediateBinaryOperator {

    public LogicalIntermediateAnd(int startIndex, int endIndex, LogicalIntermediateOperation op1, LogicalIntermediateOperation op2) {
        super(startIndex, endIndex, op1, op2);
    }

    @Override
    public LogicalOperation compile() {
        return new And(op1.compile(), op2.compile());
    }
}