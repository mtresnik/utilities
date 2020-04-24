package com.resnik.util.math.symbo.logic.operations;

import com.resnik.util.math.symbo.logic.LogicalConstant;

import java.util.ArrayList;
import java.util.List;

public class Or<T extends LogicalOperation> extends LogicalOperation {

    public Or(T o1, T o2, T ... o_n){
        super(formatSuper2(o1,o2,o_n));
    }

    public Or(T[] o_n){
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
        for(LogicalOperation operation : this.values){
            if(LogicalConstant.TRUE.equals(operation.constantRepresentation())){
                return LogicalConstant.TRUE;
            }
        }
        if(!allConstants()){
            return null;
        }
        return LogicalConstant.FALSE;
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
            return LogicalConstant.FALSE.toString();
        }
        for (int i = 0; i < nonConstantList.size(); i++) {
            retString += nonConstantList.get(i).toString();
            if (i < nonConstantList.size() - 1) {
                retString += " | ";
            }
        }
        if (constantList.isEmpty() == false) {
            boolean sum = false;
            for (int i = 0; i < constantList.size(); i++) {
                sum |= constantList.get(i).constantRepresentation().getValue();
            }
            retString += " | " + new LogicalConstant(sum);
        }
        retString += ")";
        return retString;
    }

    @Override
    public Or generate(LogicalOperation[] ops) {
        return new Or(ops);
    }

}
