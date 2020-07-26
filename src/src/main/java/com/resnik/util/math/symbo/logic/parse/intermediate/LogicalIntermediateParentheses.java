package com.resnik.util.math.symbo.logic.parse.intermediate;

import com.resnik.util.math.symbo.logic.operations.LogicalOperation;
import com.resnik.util.math.symbo.logic.operations.LogicalParentheses;
import com.resnik.util.math.symbo.logic.parse.LogicalTokenType;
import com.resnik.util.text.Token;

import java.util.List;

public class LogicalIntermediateParentheses extends LogicalIntermediateUnaryOperator {

    public LogicalIntermediateParentheses(int startIndex, int endIndex, LogicalIntermediateOperation operation) {
        super(startIndex, endIndex, operation);
    }

    @Override
    public LogicalOperation compile() {
        return new LogicalParentheses(operation.compile());
    }
}
