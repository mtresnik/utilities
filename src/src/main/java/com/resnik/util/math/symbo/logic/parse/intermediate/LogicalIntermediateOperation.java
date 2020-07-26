package com.resnik.util.math.symbo.logic.parse.intermediate;

import com.resnik.util.math.symbo.IntermediateOperation;
import com.resnik.util.math.symbo.algebra.parse.SymbolicTokenType;
import com.resnik.util.math.symbo.logic.operations.LogicalOperation;
import com.resnik.util.math.symbo.logic.parse.LogicalTokenType;
import com.resnik.util.text.Token;

import java.util.List;

public abstract class LogicalIntermediateOperation extends IntermediateOperation<LogicalOperation, LogicalTokenType> {

    public LogicalIntermediateOperation(int startIndex, int endIndex, List<Token<LogicalTokenType>> tokens) {
        super(startIndex, endIndex, tokens);
    }

}
