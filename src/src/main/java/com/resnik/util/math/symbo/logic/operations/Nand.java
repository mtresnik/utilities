package com.resnik.util.math.symbo.logic.operations;

import com.resnik.util.math.symbo.logic.LogicalConstant;

public class Nand extends LogicalOperation{

    private LogicalOperation left, right;

    public Nand(LogicalOperation left, LogicalOperation right){
        super(left, right);
        this.left = left;
        this.right = right;
    }

    @Override
    public LogicalConstant constantRepresentation() {
        if(allConstants() == false){
            return null;
        }
        boolean leftVal = left.constantRepresentation().getValue();
        boolean rightVal = right.constantRepresentation().getValue();
        return new LogicalConstant(!(leftVal && rightVal));
    }


    @Override
    public String nonConstantString() {
        String retString = "(";
        if(left.constantRepresentation() == null){
            retString += left.nonConstantString();
        }else{
            retString += left.constantRepresentation().toString();
        }
        retString += " !& ";
        if(right.constantRepresentation() == null){
            retString += right.nonConstantString();
        }else{
            retString += right.constantRepresentation().toString();
        }
        retString += ")";
        return retString;
    }

    @Override
    public Nand generate(LogicalOperation[] ops) {
        if(ops.length < 2){
            throw new IllegalArgumentException("Nand must have two arguments.");
        }
        return new Nand(ops[0], ops[1]);
    }

}
