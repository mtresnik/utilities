package com.resnik.util.math.symbo.algebra.parse.intermediate;

import com.resnik.util.math.symbo.algebra.parse.SymbolicTokenType;
import com.resnik.util.text.Token;

import java.util.ArrayList;
import java.util.List;

public abstract class IntermediateOperator extends IntermediateOperation {

    List<IntermediateOperation> operations;

    public IntermediateOperator(int startIndex, int endIndex, List<IntermediateOperation> operations) {
        super(startIndex, endIndex, yokeAll(operations));
        this.operations = operations;
    }

    public static List<Token<SymbolicTokenType>> yokeAll(List<IntermediateOperation> operations){
        List<Token<SymbolicTokenType>> retList = new ArrayList<>();
        for(IntermediateOperation op : operations){
            retList.addAll(op.tokens);
        }
        return retList;
    }

}
