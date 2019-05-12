package util.math.symbo;

import util.math.ComplexNumber;
import java.util.Objects;
import util.math.Algebraic;

public class Constant extends Operation<Algebraic> {

    public static final Constant ZERO = new Constant(ComplexNumber.ZERO), 
            ONE = new Constant(ComplexNumber.ONE), 
            TWO = new Constant(ComplexNumber.TWO),
            I = new Constant(ComplexNumber.I),
            PI = new Constant(Math.PI), 
            E = new Constant(Math.E),
            NaN = new Constant(ComplexNumber.NaN);

    protected final ComplexNumber value;

    public Constant(double real_value) {
        this.value = ComplexNumber.a(real_value);
    }

    public Constant(ComplexNumber value) {
        this.value = value;
    }

//    @Override
//    public ComplexNumber evaluate(ComplexNumber x) {
//        return this.value;
//    }

    @Override
    public Constant getDerivative(Variable dVar) {
        return new Constant(ComplexNumber.ZERO);
    }

    @Override
    public String nonConstantString() {
        if(this.value.equals(ComplexNumber.PI)){
            return "PI";
        }else if(this.value.equals(ComplexNumber.E)){
            return "e";
        }
        return this.value.toString();
    }
    
    @Override
    public boolean allConstants() {
        return true;
    }

    public static Constant[] parseArray(double[] input) {
        Constant[] retArray = new Constant[input.length];
        for (int i = 0; i < input.length; i++) {
            retArray[i] = new Constant(input[i]);
        }
        return retArray;
    }
    
    public static Constant[] parseArray(ComplexNumber[] input) {
        Constant[] retArray = new Constant[input.length];
        for (int i = 0; i < input.length; i++) {
            retArray[i] = new Constant(input[i]);
        }
        return retArray;
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
        final Constant other = (Constant) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }
    
    public Constant generate(Operation[] ops){
        return new Constant(this.value);
    }
    
    @Override
    public Variable[] getVariables(){
        return new Variable[]{};
    }

    @Override
    public Constant constantRepresentation() {
        return this;
    }

    public ComplexNumber getValue() {
        return value;
    }

    
}
