package com.resnik.util.math.symbo.parse.intermediate;

import com.resnik.util.math.symbo.operations.Operation;
import com.resnik.util.math.symbo.parse.SymbolicTokenType;
import com.resnik.util.text.Token;

import java.util.List;

public abstract class IntermediateOperation {

    public int startIndex, endIndex;
    List<Token<SymbolicTokenType>> tokens;

    public IntermediateOperation(int startIndex, int endIndex, List<Token<SymbolicTokenType>> tokens) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.tokens = tokens;
    }

    public abstract Operation compile();

}
