package com.resnik.util.math.symbo.logic.parse.intermediate;

import java.util.Collections;

public abstract class LogicalIntermediateUnaryOperator extends LogicalIntermediateOperator {

    LogicalIntermediateOperation operation;

    public LogicalIntermediateUnaryOperator(int startIndex, int endIndex, LogicalIntermediateOperation operation){
        super(startIndex, endIndex, Collections.singletonList(operation));
        this.operation = operation;
    }

}
