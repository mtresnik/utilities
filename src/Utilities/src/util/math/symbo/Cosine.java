package util.math.symbo;

import util.math.ComplexNumber;

public class Cosine extends Operation {

    public Operation inside;

    public Cosine(Operation inside) {
        super(inside);
        this.inside = inside;
    }

    @Override
    protected String nonConstantString() {
        return "cos(" + inside + ")";
    }

//    @Override
//    public ComplexNumber evaluate(ComplexNumber t) {
//        ComplexNumber eval = inside.evaluate(t);
//        return ComplexNumber.cos(eval);
//
//    }

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
        return new Constant(ComplexNumber.cos(this.inside.constantRepresentation().value));
    }

    @Override
    public Operation getIntegral(Variable dVar) {
        if(inside.equals(dVar)){
            return new Sine(dVar);
        }
        throw IntegrationException.TRIG_EXCEPTION;
    }

}
