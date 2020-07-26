package com.resnik.util.math.symbo.algebra.parse.intermediate;

import com.resnik.util.math.symbo.algebra.parse.SymbolicTokenType;
import com.resnik.util.text.Token;

import java.util.ArrayList;
import java.util.List;

public abstract class SymbolicIntermediateOperator extends SymbolicIntermediateOperation {

    List<SymbolicIntermediateOperation> operations;

    public SymbolicIntermediateOperator(int startIndex, int endIndex, List<SymbolicIntermediateOperation> operations) {
        super(startIndex, endIndex, yokeAll(operations));
        this.operations = operations;
    }

    public static List<Token<SymbolicTokenType>> yokeAll(List<SymbolicIntermediateOperation> operations){
        List<Token<SymbolicTokenType>> retList = new ArrayList<>();
        for(SymbolicIntermediateOperation op : operations){
            retList.addAll(op.tokens);
        }
        return retList;
    }

}
