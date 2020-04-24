package com.resnik.util.math.symbo.algebra.operations.bulk;

import com.resnik.util.math.symbo.algebra.Algebraic;
import com.resnik.util.math.symbo.algebra.Bounds;
import com.resnik.util.math.symbo.algebra.ComplexNumber;
import com.resnik.util.math.symbo.algebra.operations.*;
import com.resnik.util.math.symbo.algebra.operations.base.Multiplication;

public class BulkProduct extends Operation {

    public Operation inside;
    public Variable index_variable;

    public BulkProduct(Operation inside, Variable index_variable) {
        super(inside);
        this.inside = inside;
        this.index_variable = index_variable;
    }

    public Multiplication evaluateBounds(Bounds b) {
        Operation[] opList = new Operation[(int) (b.max.real - b.min.real + 1)];
        for (double index = b.min.real; index <= b.max.real; index++) {
            int opIndex = (int) index;
            Operation currEval = inside.substitute(index_variable, new Constant(ComplexNumber.a(index)));
            opList[opIndex] = currEval;
        }
        return new Multiplication(opList);
    }

    @Override
    public Constant constantRepresentation() {
        return Constant.NaN;
    }

    @Override
    public String nonConstantString() {
        return Variable.CAPITAL_PI + "_" + index_variable + " (" + inside + ")";
    }

    @Override
    public Operation getDerivative(Variable dVar) {
        throw DerivativeException.PRODUCT_EXCEPTION;
    }

    public Operation getDerivativeBounds(Bounds b, Variable dVar){
        return this.evaluateBounds(b).getDerivative(dVar);
    }

    @Override
    public Operation getIntegral(Variable dVar) {
        throw IntegrationException.PRODUCT_EXCEPTION;
    }

    @Override
    public Object generate(Operation[] newValues) {
        return new BulkProduct(newValues[0], this.index_variable);
    }

    @Override
    public BulkProduct evaluate(Variable var, Algebraic t) {
        Operation superResult = super.evaluate(var, t);
        return (BulkProduct) superResult;
    }

    public String evaluateBoundsToString(Bounds b) {
        return Variable.CAPITAL_PI + "_" + index_variable + " (" + inside + ") [" + b.min + " to " + b.max + "] = " + this.evaluateBounds(b);
    }

}
