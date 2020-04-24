package com.resnik.util.math.symbo.algebra.operations.bulk;

import com.resnik.util.math.symbo.algebra.Algebraic;
import com.resnik.util.math.symbo.algebra.Bounds;
import com.resnik.util.math.symbo.algebra.ComplexNumber;
import com.resnik.util.math.symbo.algebra.operations.Constant;
import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.math.symbo.algebra.operations.Variable;
import com.resnik.util.math.symbo.algebra.operations.base.Addition;

public class Sigma extends Operation {

    public Operation inside;
    public Variable index_variable;

    public Sigma(Operation inside, Variable index_variable) {
        super(inside);
        this.inside = inside;
        this.index_variable = index_variable;
    }

    public Addition evaluateBounds(Bounds b) {
        Operation[] opList = new Operation[(int) (b.max.real - b.min.real + 1)];
        for (double index = b.min.real; index <= b.max.real; index++) {
            int opIndex = (int) index;
            Operation currEval = inside.substitute(index_variable, new Constant(ComplexNumber.a(index)));
            opList[opIndex] = currEval;
        }
        return new Addition(opList);
    }

    @Override
    public String nonConstantString() {
        return Variable.SIGMA + "_" + index_variable + " (" + inside + ")";
    }

    @Override
    public Sigma getDerivative(Variable dVar) {
        return new Sigma(inside.getDerivative(dVar), this.index_variable);
    }

    @Override
    public Sigma generate(Operation[] newValues) {
        return new Sigma(newValues[0], this.index_variable);
    }

    @Override
    public Constant constantRepresentation() {
        return Constant.NaN;
    }

    @Override
    public Sigma evaluate(Variable var, Algebraic t) {
        Operation superResult = super.evaluate(var, t);
        return (Sigma) superResult;
    }

    public String evaluateBoundsToString(Bounds b) {
        return Variable.SIGMA + "_" + index_variable + " (" + inside + ") [" + b.min + " to " + b.max + "] = " + this.evaluateBounds(b);
    }

    @Override
    public Operation getIntegral(Variable dVar) {
        return new Sigma(inside.getIntegral(dVar), this.index_variable);
    }


}
