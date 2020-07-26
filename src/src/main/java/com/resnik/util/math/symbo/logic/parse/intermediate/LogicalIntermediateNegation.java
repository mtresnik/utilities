package com.resnik.util.math.symbo.logic.parse.intermediate;

import com.resnik.util.math.symbo.logic.operations.LogicalOperation;
import com.resnik.util.math.symbo.logic.operations.LogicalNegation;

public class LogicalIntermediateNegation extends LogicalIntermediateUnaryOperator{

    public LogicalIntermediateNegation(int startIndex, int endIndex, LogicalIntermediateOperation operation) {
        super(startIndex, endIndex, operation);
    }

    @Override
    public LogicalOperation compile() {
        return new LogicalNegation(operation.compile());
    }
}
