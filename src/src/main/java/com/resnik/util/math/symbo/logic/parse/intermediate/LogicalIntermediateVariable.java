package com.resnik.util.math.symbo.logic.parse.intermediate;

import com.resnik.util.math.symbo.logic.LogicalVariable;
import com.resnik.util.math.symbo.logic.operations.LogicalOperation;
import com.resnik.util.math.symbo.logic.parse.LogicalTokenType;
import com.resnik.util.text.Token;

import java.util.List;

public class LogicalIntermediateVariable extends LogicalIntermediateOperation {

    public LogicalIntermediateVariable(int startIndex, int endIndex, List<Token<LogicalTokenType>> tokens) {
        super(startIndex, endIndex, tokens);
    }

    @Override
    public LogicalOperation compile() {
        return new LogicalVariable(tokens.get(0).rep);
    }

    @Override
    public String toString() {
        return "IVariable:" + tokens.get(0).rep;
    }
}
