package com.resnik.util.math.symbo.parse.intermediate;

import com.resnik.util.math.symbo.operations.Operation;
import com.resnik.util.math.symbo.operations.Variable;
import com.resnik.util.math.symbo.parse.SymbolicTokenType;
import com.resnik.util.text.Token;
import com.resnik.util.text.TokenizationException;

import java.util.Collections;
import java.util.List;

public class IntermediateVariable extends IntermediateOperation {

    public IntermediateVariable(int startIndex, int endIndex, Token<SymbolicTokenType> token) {
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
