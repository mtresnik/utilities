package com.resnik.util.math.symbo.algebra.operations.functions;

import com.resnik.util.math.symbo.algebra.ComplexNumber;
import com.resnik.util.math.symbo.algebra.operations.Constant;
import com.resnik.util.math.symbo.algebra.operations.IntegrationException;
import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.math.symbo.algebra.operations.Variable;
import com.resnik.util.math.symbo.algebra.operations.base.Multiplication;

public class ArcTan extends Operation {

    public Operation inside;

    public ArcTan(Operation inside) {
        super(inside);
        this.inside = inside;
    }

    @Override
    public String nonConstantString() {
        return "arctan(" + inside + ")";
    }

    @Override
    public Operation getDerivative(Variable dVar) {
        Operation firstTerm = Constant.ONE.divide(Constant.ONE.add(dVar.pow(Constant.TWO)));
        Operation secondTerm = inside.getDerivative(dVar);
        return new Multiplication(firstTerm, secondTerm);
    }

    @Override
    public ArcTan generate(Operation[] newValues) {
        if(newValues.length == 0){
            return new ArcTan(Variable.X);
        }
        return new ArcTan(newValues[0]);
    }

    @Override
    public Constant constantRepresentation() {
        double inside_c = inside.constantRepresentation().getValue().real;
        return new Constant(Math.atan(inside_c));
    }

    @Override
    public Operation getIntegral(Variable dVar) {
        if(inside.equals(dVar)){
            Operation front = dVar.multiply(new ArcTan(dVar));
            Operation back = new Log(Constant.E, Constant.ONE.add(dVar.pow(Constant.TWO))).divide(Constant.TWO);
            return front.subtract(back);
        }
        throw IntegrationException.TRIG_EXCEPTION;
    }
}
