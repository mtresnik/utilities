package com.resnik.util.math.symbo.algebra.parse.intermediate;

import com.resnik.util.math.symbo.IntermediateOperation;
import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.math.symbo.algebra.parse.SymbolicTokenType;
import com.resnik.util.text.Token;

import java.util.List;

public abstract class SymbolicIntermediateOperation extends IntermediateOperation<Operation, SymbolicTokenType> {

    public SymbolicIntermediateOperation(int startIndex, int endIndex, List<Token<SymbolicTokenType>> tokens) {
        super(startIndex, endIndex, tokens);
    }

}
