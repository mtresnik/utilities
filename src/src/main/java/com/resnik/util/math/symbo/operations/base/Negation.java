package com.resnik.util.math.symbo.operations.base;

import com.resnik.util.math.symbo.ComplexNumber;
import com.resnik.util.math.symbo.operations.Constant;
import com.resnik.util.math.symbo.operations.Operation;
import com.resnik.util.math.symbo.operations.Variable;


public class Negation extends Operation {

    private Operation elem;

    public Negation(Operation elem) {
        super(elem);
        this.elem = elem;
    }

    @Override
    public String nonConstantString() {
        return "(-1.0)*" + elem.toString();
    }

    @Override
    public Operation getDerivative(Variable dVar) {
        return new Negation(elem.getDerivative(dVar));
    }
    
    public Negation generate(Operation[] ops){
        if(ops.length == 0){
            return new Negation(Constant.ONE);
        }
        return new Negation(ops[0]);
    }

    @Override
    public Constant constantRepresentation() {
        if (allConstants() == false) {
            return Constant.NaN;
        }
        ComplexNumber c1 = elem.constantRepresentation().getValue().scale(-1);
        return new Constant(c1);
    }

    @Override
    public Operation getIntegral(Variable dVar) {
        return new Negation(elem.getIntegral(dVar));
    }


}
