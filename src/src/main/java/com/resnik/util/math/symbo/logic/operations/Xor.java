package com.resnik.util.math.symbo.logic.operations;

import com.resnik.util.math.symbo.logic.LogicalConstant;

import java.util.ArrayList;
import java.util.List;

public class Xor<T extends LogicalOperation> extends LogicalOperation {

    public Xor(T o1, T o2, T ... o_n){
        super(formatSuper2(o1,o2,o_n));
    }

    public Xor(T[] o_n){
        super(formatInput(o_n));
    }

    public static LogicalOperation[] formatInput(LogicalOperation[] ops){
        if (ops.length == 0) {
            return new LogicalOperation[]{LogicalConstant.TRUE, LogicalConstant.TRUE};
        } else if (ops.length == 1) {
            return new LogicalOperation[]{LogicalConstant.TRUE, ops[0]};
        } else {
            return ops;
        }
    }

    @Override
    public LogicalConstant constantRepresentation() {
        if(!allConstants()){
            return null;
        }
        int numTrue = 0;
        for(LogicalOperation operation : this.values){
            if(operation.constantRepresentation().equals(LogicalConstant.TRUE)){
                numTrue++;
            }
        }
        if(numTrue % 2 == 0){
            return LogicalConstant.FALSE;
        }else{
            return LogicalConstant.TRUE;
        }
    }

    @Override
    public String nonConstantString() {
        String retString = "(";
        List<LogicalOperation> nonConstantList = new ArrayList();
        if (allConstants()) {
            return this.constantRepresentation().toString();
        }
        List<LogicalOperation> constantList = new ArrayList();
        for (LogicalOperation o : this.values) {
            if(o.constantRepresentation() == null){
                nonConstantList.add(o);
            }else{
                constantList.add(o.constantRepresentation());
            }
        }
        if (nonConstantList.isEmpty() && constantList.isEmpty()) {
            return LogicalConstant.TRUE.toString();
        }
        for (int i = 0; i < nonConstantList.size(); i++) {
            retString += nonConstantList.get(i).toString();
            if (i < nonConstantList.size() - 1) {
                retString += " ^ ";
            }
        }
        if (!constantList.isEmpty()) {
            int numTrue = 0;
            for(LogicalOperation operation : constantList){
                if(operation.constantRepresentation().equals(LogicalConstant.TRUE)){
                    numTrue++;
                }
            }
            LogicalConstant constantRep = (numTrue % 2 == 0) ? LogicalConstant.FALSE : LogicalConstant.TRUE;
            retString += " ^ " + constantRep;
        }
        retString += ")";
        return retString;
    }

    @Override
    public Xor generate(LogicalOperation[] ops) {
        return new Xor<>(ops);
    }
}
