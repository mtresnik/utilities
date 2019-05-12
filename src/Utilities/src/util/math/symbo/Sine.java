package util.math.symbo;

import util.math.ComplexNumber;


public class Sine extends Operation{

    public Operation inside;

    public Sine(Operation inside) {
        super(inside);
        this.inside = inside;
    }
    
    @Override
    protected String nonConstantString() {
        return "sin(" + inside + ")";
    }

//    @Override
//    public ComplexNumber evaluate(ComplexNumber t) {
//        ComplexNumber eval = inside.evaluate(t);
//        return ComplexNumber.sin(eval);
//    }

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
        ComplexNumber inside_c = inside.constantRepresentation().value;
        return new Constant(ComplexNumber.sin(inside_c));
    }

}
