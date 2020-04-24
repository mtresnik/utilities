package com.resnik.util.math.symbo.algebra.operations.functions;

import com.resnik.util.math.symbo.algebra.ComplexNumber;
import com.resnik.util.math.symbo.algebra.operations.Constant;
import com.resnik.util.math.symbo.algebra.operations.IntegrationException;
import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.math.symbo.algebra.operations.Variable;
import com.resnik.util.math.symbo.algebra.operations.base.Multiplication;
import com.resnik.util.math.symbo.algebra.operations.base.Negation;

public class Cosine extends Operation {

    public Operation inside;

    public Cosine(Operation inside) {
        super(inside);
        this.inside = inside;
    }

    @Override
    public String nonConstantString() {
        return "cos(" + inside + ")";
    }

    @Override
    public Operation getDerivative(Variable dVar) {
        Operation firstTerm = new Negation(new Sine(inside));
        Operation secondTerm = inside.getDerivative(dVar);
        return new Multiplication(firstTerm, secondTerm);
    }

    @Override
    public Cosine generate(Operation[] newValues) {
        if (newValues.length == 0) {
            return new Cosine(Variable.X);
        }
        return new Cosine(newValues[0]);
    }

    @Override
    public Constant constantRepresentation() {
        if(allConstants() == false){
            return Constant.NaN;
        }
        return new Constant(ComplexNumber.cos(this.inside.constantRepresentation().getValue()));
    }

    @Override
    public Operation getIntegral(Variable dVar) {
        if(inside.equals(dVar)){
            return new Sine(dVar);
        }
        throw IntegrationException.TRIG_EXCEPTION;
    }
}
