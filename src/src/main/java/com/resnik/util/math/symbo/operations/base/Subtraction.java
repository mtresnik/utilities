package com.resnik.util.math.symbo.operations.base;


import com.resnik.util.math.symbo.ComplexNumber;
import com.resnik.util.math.symbo.operations.Constant;
import com.resnik.util.math.symbo.operations.Operation;
import com.resnik.util.math.symbo.operations.Variable;

import java.util.ArrayList;
import java.util.List;

public class Subtraction extends Operation {

    private Operation o_1, o_2;
    private Operation[] rem;

    public Subtraction(Operation o_1, Operation o_2, Operation... rem) {
        super(formatSuper2(o_1, o_2, rem));
        this.o_1 = o_1;
        this.o_2 = o_2;
        this.rem = rem;
    }
    
    public Subtraction(Operation[] ops){
        super(formatInput(ops));
        this.o_1 = this.values[0];
        this.o_2 = this.values[1];
        this.rem = popTwo(this.values);
    }
    
    
    public static Operation[] popTwo(Operation[] ops){
        if(ops.length <= 2){
            return new Operation[]{};
        }
        Operation[] retArray = new Operation[ops.length - 2];
        for (int i = 2; i < ops.length; i++) {
            retArray[i - 2] = ops[i];
        }
        return retArray;
    }
    
    public static Operation[] formatInput(Operation[] ops) {
        if (ops.length == 0) {
            return new Operation[]{Constant.ONE, Constant.ZERO};
        } else if (ops.length == 1) {
            return new Operation[]{ops[0], Constant.ZERO};
        } else {
            return ops;
        }
    }
    

    @Override
    public String nonConstantString() {
        String retString = "(";
        List<Operation> nonZeroList = new ArrayList();
        boolean allConstants = true;
        for (Operation o : this.values) {
            if (o instanceof Constant) {
                if (((Constant) o).getValue().equals(ComplexNumber.ZERO)) {
                    continue;
                }
            } else {
                allConstants = false;
            }
            nonZeroList.add(o);
        }
        if (nonZeroList.isEmpty()) {
            return Constant.ZERO.toString();
        }
        for (int i = 0; i < nonZeroList.size(); i++) {
            retString += nonZeroList.get(i).toString();
            if (i < nonZeroList.size() - 1) {
                retString += " - ";
            }
        }
        if (allConstants) {
            retString = "" + evaluateReal(0);
        }
        return retString + ")";
    }


    @Override
    public Operation getDerivative(Variable dVar) {
        Operation d_1 = this.values[0].getDerivative(dVar);
        Operation d_2 = this.values[1].getDerivative(dVar);
        Operation[] remainder = (this.values.length > 2 ? new Operation[this.values.length - 2] : new Operation[]{});
        for (int i = 2; i < this.values.length; i++) {
            remainder[i - 2] = this.values[i].getDerivative(dVar);
        }
        return new Subtraction(d_1, d_2, remainder);
    }
    
    public Subtraction generate(Operation[] ops){
        return new Subtraction(ops);
    }

    @Override
    public Constant constantRepresentation() {
        if(allConstants() == false){
            return Constant.NaN;
        }
        Constant retConstant = this.o_1.constantRepresentation();
        for (int i = 1; i < this.values.length; i++) {
            retConstant = new Constant(retConstant.getValue().subtract(this.values[i].constantRepresentation().getValue()));
        }
        return retConstant;
    }

    @Override
    public Operation getIntegral(Variable dVar) {
        Operation d_1 = this.values[0].getIntegral(dVar);
        Operation d_2 = this.values[1].getIntegral(dVar);
        Operation[] remainder = (this.values.length > 2 ? new Operation[this.values.length - 2] : new Operation[]{});
        for (int i = 2; i < this.values.length; i++) {
            remainder[i - 2] = this.values[i].getIntegral(dVar);
        }
        return new Subtraction(d_1, d_2, remainder);
    }

}
