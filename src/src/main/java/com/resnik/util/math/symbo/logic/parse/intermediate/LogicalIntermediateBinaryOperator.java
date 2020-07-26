package com.resnik.util.math.symbo.logic.parse.intermediate;

import java.util.Arrays;

public abstract class LogicalIntermediateBinaryOperator extends LogicalIntermediateOperator{
    LogicalIntermediateOperation op1;
    LogicalIntermediateOperation op2;
    public LogicalIntermediateBinaryOperator(int startIndex, int endIndex, LogicalIntermediateOperation op1, LogicalIntermediateOperation op2) {
        super(startIndex, endIndex, Arrays.asList(op1, op2));
        this.op1 = op1;
        this.op2 = op2;
    }
}
