package util.math.symbo;

import java.util.Objects;
import util.structures.CountList;
import util.structures.CountObject;

public class Variable extends Operation {

    public static final Variable X = new Variable("x");
    public static final Variable Y = new Variable("y");
    public static final Variable W = new Variable("w");
    public static final Variable T = new Variable("t");
    public static final Variable I = new Variable("i");
    public static final Variable N = new Variable("n");
    public static final Variable THETA = new Variable("\u03F4");
    public static final Variable SIGMA = new Variable("\u03A3");
    

    public final String name;
    public Variable derivativeOf;
    public CountList<Variable> respectTo;

    public Variable(String name) {
        this.name = name;
        this.respectTo = new CountList<Variable>();
    }

//    @Override
//    public ComplexNumber evaluate(ComplexNumber t) {
//        return t;
//    }

    @Override
    public Operation getDerivative(Variable dVar) {
        if (this.equals(dVar)) {
            return Constant.ONE;
        }
        Variable d = Variable.genVariable("d");
        Variable derOf = (this.derivativeOf == null ? this : this.derivativeOf);
        CountList<Variable> newRespectTo = respectTo.clone();
        int sum = 0;
        if (derOf.equals(dVar)) {
            sum = -1;
        } else {
            newRespectTo.addCountObject(dVar);
        }

//        System.out.println("newRespectTo:" + newRespectTo);
        Operation[] denominatorArray = new Operation[newRespectTo.size()];
        for (int i = 0; i < denominatorArray.length; i++) {
            CountObject<Variable> currEntry = newRespectTo.get(i);
//            System.out.println("currEntry:" + currEntry);
            sum += currEntry.getAmount();
            denominatorArray[i] = new Multiplication(d, new Power(currEntry.getElement(), new Constant(currEntry.getAmount())));
        }
//        System.out.println(Arrays.toString(powerArray));
        Multiplication numerator = new Multiplication(new Power(d, new Constant(sum)), derOf);
//        System.out.println("numerator:" + numerator);
        Multiplication denominator = new Multiplication(denominatorArray);
//        System.out.println("denominator:" + denominator);
        Division div = new Division(numerator, denominator);
//        System.out.println("numerator:" + numerator + "\tdenominator:" + denominator);
        Variable retVar = new Variable(div.toString());
        retVar.derivativeOf = derOf;
        retVar.respectTo = newRespectTo;
        return retVar;
    }

    public boolean allConstants() {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Variable other = (Variable) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    public static Variable genVariable(String name) {
        if (name.equals(X.name)) {
            return X;
        }
        Variable retVar = new Variable(name);
        return retVar;
    }

    public Variable generate(Operation[] newValues) {
        return new Variable(this.name);
    }

    @Override
    protected String nonConstantString() {
        return this.name;
    }

    public static Variable[] genVariables(String... varNames) {
        Variable[] retArray = new Variable[varNames.length];
        for (int i = 0; i < varNames.length; i++) {
            String currString = varNames[i];
            retArray[i] = new Variable(currString);
        }
        return retArray;
    }

    @Override
    public Variable[] getVariables() {
        return new Variable[]{this};
    }

    @Override
    public Constant constantRepresentation() {
        return Constant.NaN;
    }

    public String getName() {
        return name;
    }

    @Override
    public Operation getIntegral(Variable dVar) {
        if(dVar.equals(this)){
            return new Power(this, Constant.ONE).getIntegral(dVar);
        }
        return new Multiplication(this, dVar);
    }
    
    

    
}
