package util.math.symbo;

import util.math.ComplexNumber;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import util.math.matrices;

public class Polynomial2d extends Polynomial {

    private Polynomial2d(Multiplication[] o_n) {
        super(o_n);
    }

    public Polynomial2d(Constant[] coefficients, Constant[] exponents) {
        this(generateProducts(coefficients, exponents));
    }

    public Polynomial2d(double[] coefficients, double[] exponents) {
        this(generateProducts(Constant.parseArray(coefficients), Constant.parseArray(exponents)));
    }

    public Polynomial2d(Variable var1, Constant[] coefficients) {
        this(generateProducts(var1, coefficients));
    }

    public Polynomial2d(Constant[] coefficients) {
        this(generateProducts(coefficients));
    }

    public Polynomial2d(ComplexNumber[] coefficients) {
        this(generateProducts(Constant.parseArray(coefficients)));
    }

    public Polynomial2d(double[] coefficients) {
        this(generateProducts(Constant.parseArray(coefficients)));
    }

    public static Multiplication[] generateProducts(Variable var, Constant[] coefficients, Constant[] exponents) {
        List<Pair<Constant, Constant>> pairList = new ArrayList();
        for (int i = 0; i < coefficients.length; i++) {
            Pair<Constant, Constant> currPair = new Pair(coefficients[i], exponents[i]);
            pairList.add(currPair);
        }
//        System.out.println(pairList);
        Multiplication[] retArray = new Multiplication[pairList.size()];
        for (int i = 0; i < retArray.length; i++) {
            Pair<Constant, Constant> currPair = pairList.get(i);
            Multiplication tempProduct = new Multiplication(
                    new Constant(currPair.getKey().value),
                    Power.var(var, currPair.getValue().value));
//            System.out.println(Arrays.toString(tempProduct.values));
            retArray[i] = tempProduct;
        }
//        System.out.println("retArray:" + Arrays.toString(retArray));
        return retArray;
    }

    public static Multiplication[] generateProducts(Constant[] coefficients, Constant[] exponents) {
        return generateProducts(Variable.X, coefficients, exponents);
    }

    public static Multiplication[] generateProducts(Constant[] coefficients) {
        double[] exponents = matrices.generateSequenceDouble(coefficients.length - 1, -1, -1);
        Constant[] exponent_constants = Constant.parseArray(exponents);
//        System.out.println("exponent_constants:" + Arrays.toString(exponent_constants));
        Multiplication[] retProducts = generateProducts(coefficients, exponent_constants);
//        System.out.println("retProducts:" + Arrays.toString(retProducts));
        return retProducts;
    }

    public static Multiplication[] generateProducts(Variable var1, Constant[] coefficients) {
        double[] exponents = matrices.generateSequenceDouble(coefficients.length - 1, -1, -1);
        Constant[] exponent_constants = Constant.parseArray(exponents);
        return generateProducts(var1, coefficients, exponent_constants);

    }

    public boolean isRoot(double t){
        return this.evaluateReal(t).equals(ComplexNumber.ZERO);
    }

    @Override
    public Polynomial2d generate(Multiplication[] p_n) {
        return new Polynomial2d(p_n);
    }
    
    
}
