package com.resnik.util.math.symbo.operations.base;

import com.resnik.util.math.symbo.operations.Constant;
import com.resnik.util.math.symbo.operations.Variable;
import com.resnik.util.math.symbo.operations.Operation;
import com.resnik.util.math.symbo.parse.ParseTree;

// Operation wrapper
public class Parentheses extends Operation {

    private Operation elem;

    public Parentheses(Operation elem) {
        super(elem);
        this.elem = elem;
    }

    @Override
    public String nonConstantString() {
        return "(" + elem.toString() + ")";
    }

    @Override
    public Operation getDerivative(Variable dVar) {
        return elem.getDerivative(dVar);
    }

    public Parentheses generate(Operation[] ops){
        if(ops.length == 0){
            return new Parentheses(Constant.ONE);
        }
        return new Parentheses(ops[0]);
    }

    @Override
    public Constant constantRepresentation() {
        if (elem.allConstants() == false) {
            return Constant.NaN;
        }
        return elem.constantRepresentation();
    }

    public Operation unWrap(){
        Operation curr = this;
        while(curr instanceof Parentheses){
            curr = ((Parentheses) curr).elem;
        }
        return curr;
    }

    @Override
    public Operation getIntegral(Variable dVar) {
        return elem.getIntegral(dVar);
    }

}
