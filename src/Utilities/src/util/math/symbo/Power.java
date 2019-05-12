package util.math.symbo;

import util.math.ComplexNumber;
import javafx.util.Pair;

public class Power extends Operation<Operation> {

    public Operation base, exponent;

    public Power(Operation base, Operation exponent) {
        super(base, exponent);
        this.base = base;
        this.exponent = exponent;
    }

    public Power(Operation... ops) {
        super(fomatInput(ops));
        this.base = this.values[0];
        this.exponent = this.values[1];
    }

    public static Operation[] fomatInput(Operation[] ops) {
        Operation op1, op2;
        if (ops.length == 0) {
            op1 = Constant.ONE;
            op2 = Constant.ONE;
        } else if (ops.length == 1) {
            op1 = ops[0];
            op2 = Constant.ONE;
        } else {
            op1 = ops[0];
            op2 = ops[1];
        }
        return new Operation[]{op1, op2};
    }

    @Override
    public boolean allConstants() {
        if(base instanceof Vector){
            return false;
        }
        if (exponent.allConstants() && exponent.constantRepresentation().equals(Constant.ZERO)) {
            return true;
        }
        return base.allConstants() && exponent.allConstants();
    }

    @Override
    public boolean containsVar(Variable var) {
        if (exponent.allConstants() && exponent.constantRepresentation().equals(Constant.ZERO)) {
            return false;
        }
        return super.containsVar(var);
    }

    @Override
    public String nonConstantString() {
        if (exponent instanceof Constant || exponent.allConstants()) {
            Constant e_c = exponent.constantRepresentation();
            if (e_c.value.equals(ComplexNumber.ONE)) {
                return base.toString();
            }
            if (e_c.value.equals(ComplexNumber.ZERO) && base instanceof Vector == false) {
                return Constant.ONE.toString();
            }
        }

        return base.toString() + "^(" + exponent.toString() + ")";
    }

//    @Override
//    public ComplexNumber evaluate(ComplexNumber x) {
//        ComplexNumber retVal = (base.evaluate(x)).pow(exponent.evaluate(x));
//        return retVal;
//    }

    @Override
    public Operation getDerivative(Variable dVar) {
        Operation fx = base;
        Operation gx = exponent;
        Operation f1x = base.getDerivative(dVar);
        Operation g1x = exponent.getDerivative(dVar);

        Operation left = new Multiplication(Log.ln(fx), g1x, new Power(fx, gx));
        Operation right = new Multiplication(new Power(fx, new Subtraction(gx, new Constant(1))), gx, f1x);
        if (g1x instanceof Constant) {
            Constant g1x_c = (Constant) g1x;
            if (g1x_c.value.equals(ComplexNumber.ZERO)) {
                return right;
            }
        }
        if (f1x instanceof Constant) {
            Constant f1x_c = (Constant) f1x;
            if (f1x_c.value.equals(ComplexNumber.ZERO)) {
                return left;
            }
        }

        return new Addition(left, right);
    }

    public static Power var(Variable v, Operation o) {
        return new Power(v, o);
    }

    public static Power var(Variable v, ComplexNumber c1) {
        return new Power(v, new Constant(c1));
    }

    public static Power var(Variable v, double d) {
        return new Power(v, new Constant(d));
    }

    public static Power x(Operation o) {
        return var(Variable.X, o);
    }

    public static Power x(double d) {
        return x(new Constant(d));
    }

    public static Power x() {
        return x(1);
    }

    @Override
    public Power generate(Operation... newValues) {
        return new Power(newValues);
    }

    @Override
    public Constant constantRepresentation() {
        if(allConstants() == false){
            return Constant.NaN;
        }
        if(exponent.constantRepresentation().equals(Constant.ZERO)){
            return Constant.ONE;
        }
        ComplexNumber c1 = base.constantRepresentation().value.pow(exponent.constantRepresentation().value);
        return new Constant(c1);
    }

}
