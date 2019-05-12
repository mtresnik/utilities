package util.math.symbo;

import util.math.ComplexNumber;


public class Division extends Operation {

    private Operation numerator, denominator;
    private Operation[] rem;

    public Division(Operation numerator, Operation denominator, Operation... rem) {
        super(formatSuper2(numerator, denominator, rem));
        if(numerator == null || denominator == null){
            throw new IllegalArgumentException("numerator:" + numerator + "\tdenominator:" + denominator);
        }
        this.numerator = numerator;
        this.denominator = denominator;
        this.rem = rem;
    }
    
    public Division(Operation[] ops) {
        super(formatInput(ops));
        this.numerator = this.values[0];
        this.denominator = this.values[1];
        this.rem = popTwo(this.values);
    }
    
    public static Operation[] popTwo(Operation[] ops){
        if(ops.length <= 2){
            return new Operation[]{};
        }
        Operation[] retArray = new Operation[ops.length - 2];
        for (int i = 2; i < ops.length; i++) {
            retArray[i - 2] = ops[i];
        }
        return retArray;
    }
    
    public static Operation[] formatInput(Operation[] ops) {
        if (ops.length == 0) {
            return new Operation[]{Constant.ONE, Constant.ONE};
        } else if (ops.length == 1) {
            return new Operation[]{Constant.ONE, ops[0]};
        } else {
            return ops;
        }
    }
    

    @Override
    public String nonConstantString() {
        return "((" + numerator.toString() + ") / (" + new Multiplication(formatSuper1(denominator, rem)).toString() + "))";
    }

//    @Override
//    public ComplexNumber evaluate(ComplexNumber x) {
//        ComplexNumber fx = numerator.evaluate(x);
//        ComplexNumber gx = new Multiplication(formatSuper1(denominator, rem)).evaluate(x);
//        if (gx.equals(ComplexNumber.ZERO)) {
//            return ComplexNumber.a(Double.NaN);
//        }
//        return fx.divide(gx);
//    }

    @Override
    public Operation getDerivative(Variable dVar) {
        Operation fx = numerator;
        Operation f1x = numerator.getDerivative(dVar);
        Operation gx = new Multiplication(formatSuper1(denominator, rem));
        Operation g1x = gx.getDerivative(dVar);

        Operation p1 = new Multiplication(f1x, gx);
        Operation p2 = new Multiplication(fx, g1x);
        Operation newNumerator = new Subtraction(p1, p2);
        Operation newDenominator = new Power(gx, new Constant(2));
        return new Division(newNumerator, newDenominator);
    }
    
    public Division generate(Operation[] ops){
        return new Division(ops);
    }

    @Override
    public Constant constantRepresentation() {
        if(allConstants() == false){
            return Constant.NaN;
        }
        Operation fx = numerator;
        Operation gx = new Multiplication(formatSuper1(denominator, rem));
        ComplexNumber c_val = fx.constantRepresentation().value.divide(gx.constantRepresentation().value);
        return new Constant(c_val);
    }

}
