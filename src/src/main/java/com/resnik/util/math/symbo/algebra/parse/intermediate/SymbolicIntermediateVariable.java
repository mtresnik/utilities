package com.resnik.util.math.symbo.algebra.parse.intermediate;

import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.math.symbo.algebra.operations.Variable;
import com.resnik.util.math.symbo.algebra.parse.SymbolicTokenType;
import com.resnik.util.text.Token;
import com.resnik.util.text.TokenizationException;

import java.util.Collections;

public class SymbolicIntermediateVariable extends SymbolicIntermediateOperation {

    public SymbolicIntermediateVariable(int startIndex, int endIndex, Token<SymbolicTokenType> token) {
        super(startIndex, endIndex, Collections.singletonList(token));
        if(token.type != SymbolicTokenType.VARIABLE){
            throw new TokenizationException("IntermediateVariable requires variable type.");
        }
    }

    @Override
    public Operation compile() {
        return new Variable(tokens.get(0).rep);
    }

    @Override
    public String toString() {
        return "IVariable:" + tokens.get(0).rep;
    }
}
