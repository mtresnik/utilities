package com.resnik.util.math.symbo.algebra.operations.base;

import com.resnik.util.math.symbo.algebra.ComplexNumber;
import com.resnik.util.math.symbo.algebra.operations.Constant;
import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.math.symbo.algebra.operations.Variable;


public class SymbolicNegation extends Operation {

    private Operation elem;

    public SymbolicNegation(Operation elem) {
        super(elem);
        this.elem = elem;
    }

    @Override
    public String nonConstantString() {
        return "(-1.0)*" + elem.toString();
    }

    @Override
    public Operation getDerivative(Variable dVar) {
        return new SymbolicNegation(elem.getDerivative(dVar));
    }
    
    public SymbolicNegation generate(Operation[] ops){
        if(ops.length == 0){
            return new SymbolicNegation(Constant.ONE);
        }
        return new SymbolicNegation(ops[0]);
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
        return new SymbolicNegation(elem.getIntegral(dVar));
    }


}
