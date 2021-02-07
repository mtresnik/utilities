package com.resnik.util.math.symbo.algebra.operations.functions;

import com.resnik.util.math.symbo.algebra.operations.Constant;
import com.resnik.util.math.symbo.algebra.operations.IntegrationException;
import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.math.symbo.algebra.operations.Variable;

public class ArcSine extends Operation {

    public Operation inside;

    public ArcSine(Operation inside) {
        super(inside);
        this.inside = inside;
    }

    @Override
    public String nonConstantString() {
        return "asin(" + inside + ")";
    }

    @Override
    public Operation getDerivative(Variable dVar) {
        throw IntegrationException.TRIG_EXCEPTION;
    }

    @Override
    public ArcSine generate(Operation[] newValues) {
        if(newValues.length == 0){
            return new ArcSine(Variable.X);
        }
        return new ArcSine(newValues[0]);
    }

    @Override
    public Constant constantRepresentation() {
        double inside_c = inside.constantRepresentation().getValue().real;
        return new Constant(Math.asin(inside_c));
    }

    @Override
    public Operation getIntegral(Variable dVar) {
        throw IntegrationException.TRIG_EXCEPTION;
    }

}
