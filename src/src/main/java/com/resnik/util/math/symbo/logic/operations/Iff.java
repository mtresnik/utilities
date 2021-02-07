package com.resnik.util.math.symbo.logic.operations;

import com.resnik.util.math.symbo.logic.LogicalConstant;

public class Iff extends LogicalOperation {
    private LogicalOperation left, right;

    public Iff(LogicalOperation left, LogicalOperation right){
        super(left, right);
        this.left = left;
        this.right = right;
    }

    @Override
    public LogicalConstant constantRepresentation() {
        if(this.left.equals(this.right)){
            return LogicalConstant.TRUE;
        }
        if(allConstants() == false){
            return null;
        }
        boolean leftVal = left.constantRepresentation().getValue();
        boolean rightVal = right.constantRepresentation().getValue();
        return new LogicalConstant(leftVal == rightVal);
    }

    @Override
    public String nonConstantString() {
        String retString = "(";
        if(left.constantRepresentation() == null){
            retString += left.nonConstantString();
        }else{
            retString += left.constantRepresentation().toString();
        }
        retString += " <-> ";
        if(right.constantRepresentation() == null){
            retString += right.nonConstantString();
        }else{
            retString += right.constantRepresentation().toString();
        }
        retString += ")";
        return retString;
    }

    @Override
    public Iff generate(LogicalOperation[] ops) {
        if(ops.length < 2){
            throw new IllegalArgumentException("Implies must have two arguments.");
        }
        return new Iff(ops[0], ops[1]);
    }
}
