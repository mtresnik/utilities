package com.resnik.util.math.symbo;

import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.math.symbo.algebra.parse.SymbolicTokenType;
import com.resnik.util.text.Token;

import java.util.List;

public abstract class IntermediateOperation<OPERATION, TOKEN_TYPE> {

    public int startIndex, endIndex;
    public List<Token<TOKEN_TYPE>> tokens;

    public IntermediateOperation(int startIndex, int endIndex, List<Token<TOKEN_TYPE>> tokens) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.tokens = tokens;
    }

    public abstract OPERATION compile();
}
