package com.resnik.util.math.symbo.operations.functions;

import com.resnik.util.math.symbo.ComplexNumber;
import com.resnik.util.math.symbo.operations.Constant;
import com.resnik.util.math.symbo.operations.IntegrationException;
import com.resnik.util.math.symbo.operations.Operation;
import com.resnik.util.math.symbo.operations.Variable;
import com.resnik.util.math.symbo.operations.base.Division;
import com.resnik.util.math.symbo.operations.base.Multiplication;
import com.resnik.util.math.symbo.operations.base.Subtraction;

public class Log extends Operation {

    private Operation base, inside;

    public Log(Operation base, Operation inside) {
        super(base, inside);
        this.base = base;
        this.inside = inside;
    }

    public Log(Operation[] ops) {
        super(formatInput(ops));
        this.base = this.values[0];
        this.inside = this.values[1];
    }

    public static Operation[] formatInput(Operation[] ops) {
        Operation op1, op2;
        if (ops.length == 0) {
            op1 = Constant.E;
            op2 = Constant.E;
        } else if (ops.length == 1) {
            op1 = ops[0];
            op2 = ops[0];
        } else {
            op1 = ops[0];
            op2 = ops[1];
        }
        return new Operation[]{op1, op2};
    }

    @Override
    public String nonConstantString() {
        return "log" + "(" + base.toString() + ", " + inside.toString() + ")";
    }

//    @Override
//    public ComplexNumber evaluate(ComplexNumber x) {
//        ComplexNumber b = base.evaluate(x), a = inside.evaluate(x);
//        ComplexNumber a_c = null, b_c = null;
//        ComplexNumber numerator = ComplexNumber.complexLn(a_c);
//        ComplexNumber denominator = ComplexNumber.complexLn(b_c);
//        return numerator.divide(denominator);
//    }

    @Override
    public Operation getDerivative(Variable dVar) {
        if (base instanceof Constant) {
            Constant b_c = (Constant) base;
            Division retDiv;
            Operation a1x = inside.getDerivative(dVar);
            Operation ln_b = ln(base);
            Operation den = new Multiplication(ln_b, inside);
            retDiv = new Division(a1x, den);
            return retDiv;
        }
        Operation num = ln(inside);
        Operation den = ln(base);
        return new Division(num, den).getDerivative(dVar);
    }

    public static Log ln(Operation inside) {
        return new Log(new Constant(Math.E), inside) {

            @Override
            public String nonConstantString() {
                return "ln(" + inside + ")";
            }
        };
    }
    
    public Log generate(Operation[] ops){
        return new Log(ops);
    }

    @Override
    public Constant constantRepresentation() {
        if(allConstants() == false){
            return Constant.NaN;
        }
        ComplexNumber numerator = ComplexNumber.complexLn(inside.constantRepresentation().getValue());
        ComplexNumber denominator = ComplexNumber.complexLn(base.constantRepresentation().getValue());
        return new Constant(numerator.divide(denominator));
    }

    @Override
    public Operation getIntegral(Variable dVar) {
        if(base instanceof Constant && inside.equals(dVar)){
            return new Multiplication(dVar, new Subtraction(this, Constant.ONE));
        }
        throw IntegrationException.LOG_EXCEPTION;
    }

}
