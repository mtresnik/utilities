package com.resnik.util.math.symbo.logic.parse.intermediate;

import com.resnik.util.math.symbo.logic.LogicalConstant;
import com.resnik.util.math.symbo.logic.operations.LogicalOperation;
import com.resnik.util.math.symbo.logic.parse.LogicalTokenType;
import com.resnik.util.text.Token;
import com.resnik.util.text.TokenizationException;

import java.util.Collections;
import java.util.List;

public class LogicalIntermediateValue extends LogicalIntermediateOperation {

    public LogicalIntermediateValue(int startIndex, int endIndex, Token<LogicalTokenType> token) {
        super(startIndex, endIndex, Collections.singletonList(token));
        if(token.type != LogicalTokenType.VALUE){
            throw new TokenizationException("LogicalIntermediateValue requires VALUE type.");
        }
    }

    @Override
    public LogicalOperation compile() {
        String rep = tokens.get(0).rep;
        boolean bRep = Boolean.parseBoolean(rep.toLowerCase());
        return new LogicalConstant(bRep);
    }
}
