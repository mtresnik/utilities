package com.resnik.util.math.symbo.algebra.operations.functions;

import com.resnik.util.math.symbo.algebra.operations.Constant;
import com.resnik.util.math.symbo.algebra.operations.IntegrationException;
import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.math.symbo.algebra.operations.Variable;

public class ArcCosine extends Operation {

    public Operation inside;

    public ArcCosine(Operation inside) {
        super(inside);
        this.inside = inside;
    }

    @Override
    public String nonConstantString() {
        return "acos(" + inside + ")";
    }

    @Override
    public Operation getDerivative(Variable dVar) {
        throw IntegrationException.TRIG_EXCEPTION;
    }

    @Override
    public ArcCosine generate(Operation[] newValues) {
        if(newValues.length == 0){
            return new ArcCosine(Variable.X);
        }
        return new ArcCosine(newValues[0]);
    }

    @Override
    public Constant constantRepresentation() {
        double inside_c = inside.constantRepresentation().getValue().real;
        return new Constant(Math.acos(inside_c));
    }

    @Override
    public Operation getIntegral(Variable dVar) {
        throw IntegrationException.TRIG_EXCEPTION;
    }

}
