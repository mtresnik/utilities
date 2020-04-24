package com.resnik.util.math.symbo.algebra.parse.intermediate;

import com.resnik.util.math.symbo.algebra.operations.Constant;
import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.math.symbo.algebra.parse.SymbolicTokenType;
import com.resnik.util.text.Token;
import com.resnik.util.text.TokenizationException;

import java.util.Collections;

public class IntermediateNumber extends IntermediateOperation {

    public IntermediateNumber(int startIndex, int endIndex, Token<SymbolicTokenType> token) {
        super(startIndex, endIndex, Collections.singletonList(token));
        if(token.type != SymbolicTokenType.NUMBER){
            throw new TokenizationException("IntermediateNumber requires number type.");
        }
    }

    @Override
    public Operation compile() {
        String rep = tokens.get(0).rep;
        double dRep = Double.parseDouble(rep);
        return new Constant(dRep);
    }
}
