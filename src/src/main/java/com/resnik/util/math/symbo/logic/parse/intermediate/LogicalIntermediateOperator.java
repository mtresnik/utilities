package com.resnik.util.math.symbo.logic.parse.intermediate;

import com.resnik.util.math.symbo.logic.parse.LogicalTokenType;
import com.resnik.util.text.Token;

import java.util.ArrayList;
import java.util.List;

public abstract class LogicalIntermediateOperator extends LogicalIntermediateOperation {

    List<LogicalIntermediateOperation> operations;

    public LogicalIntermediateOperator(int startIndex, int endIndex, List<LogicalIntermediateOperation> operations) {
        super(startIndex, endIndex, yokeAll(operations));
        this.operations = operations;
    }

    public static List<Token<LogicalTokenType>> yokeAll(List<LogicalIntermediateOperation> operations){
        List<Token<LogicalTokenType>> retList = new ArrayList<>();
        for(LogicalIntermediateOperation op : operations){
            retList.addAll(op.tokens);
        }
        return retList;
    }

}
