package com.resnik.util.math.symbo.parse.intermediate;

import com.resnik.util.math.symbo.operations.Constant;
import com.resnik.util.math.symbo.operations.Operation;
import com.resnik.util.math.symbo.parse.SymbolicTokenType;
import com.resnik.util.text.Token;
import com.resnik.util.text.TokenizationException;

import java.util.Collections;
import java.util.List;

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
