package com.resnik.util.math.symbo.operations.functions;

import com.resnik.util.math.symbo.operations.Constant;
import com.resnik.util.math.symbo.operations.Operation;
import com.resnik.util.math.symbo.operations.Variable;

public class AbsoluteValue extends Operation {

    public Operation inside;

    public AbsoluteValue(Operation inside) {
        super(inside);
        this.inside = inside;
    }

    @Override
    public Constant constantRepresentation() {
        return new Constant(inside.constantRepresentation().getValue().r());
    }

    @Override
    public String nonConstantString() {
        return "|" + inside + "|";
    }

    @Override
    public Operation getDerivative(Variable dVar) {
        return inside.getDerivative(dVar).multiply(inside.divide(this));
    }

    @Override
    public Operation getIntegral(Variable dVar) {
        if(inside.getVariables().length > 1){
            return null;
        }
        if(!inside.getVariables()[0].equals(dVar)){
            return null;
        }
        return inside.multiply(this).divide(Constant.TWO);
    }

    @Override
    public Object generate(Operation[] newValues) {
        return new AbsoluteValue(newValues[0]);
    }
}
