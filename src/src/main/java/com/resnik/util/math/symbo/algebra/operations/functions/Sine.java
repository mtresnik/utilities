package com.resnik.util.math.symbo.algebra.operations.functions;

import com.resnik.util.math.symbo.algebra.ComplexNumber;
import com.resnik.util.math.symbo.algebra.operations.Constant;
import com.resnik.util.math.symbo.algebra.operations.IntegrationException;
import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.math.symbo.algebra.operations.Variable;
import com.resnik.util.math.symbo.algebra.operations.base.Multiplication;
import com.resnik.util.math.symbo.algebra.operations.base.SymbolicNegation;


public class Sine extends Operation {

    public Operation inside;

    public Sine(Operation inside) {
        super(inside);
        this.inside = inside;
    }
    
    @Override
    public String nonConstantString() {
        return "sin(" + inside + ")";
    }

    @Override
    public Operation getDerivative(Variable dVar) {
        Operation firstTerm = new Cosine(inside);
        Operation secondTerm = inside.getDerivative(dVar);
        return new Multiplication(firstTerm, secondTerm);
    }

    @Override
    public Sine generate(Operation[] newValues) {
        if(newValues.length == 0){
            return new Sine(Variable.X);
        }
        return new Sine(newValues[0]);
    }

    @Override
    public Constant constantRepresentation() {
        if(allConstants() == false){
            return Constant.NaN;
        }
        ComplexNumber inside_c = inside.constantRepresentation().getValue();
        return new Constant(ComplexNumber.sin(inside_c));
    }

    @Override
    public Operation getIntegral(Variable dVar) {
        if(inside.equals(dVar)){
            Operation firstTerm = new Cosine(inside);
            return new SymbolicNegation(firstTerm);
        }
        throw IntegrationException.TRIG_EXCEPTION;
    }

}
