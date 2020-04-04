package com.resnik.util.math.symbo.operations.polynomials;

import com.resnik.util.math.symbo.ComplexNumber;
import java.util.ArrayList;
import java.util.List;

import com.resnik.util.math.symbo.operations.Constant;
import com.resnik.util.math.symbo.operations.interfaces.Factorable;
import com.resnik.util.math.symbo.operations.interfaces.Rootable;
import com.resnik.util.math.symbo.operations.Variable;
import com.resnik.util.math.symbo.operations.Operation;
import com.resnik.util.math.symbo.operations.base.Multiplication;
import com.resnik.util.math.symbo.operations.base.Power;
import com.resnik.util.math.symbo.operations.base.Subtraction;
import com.resnik.util.objects.arrays.ArrayUtils;
import javafx.util.Pair;
import com.resnik.util.objects.structures.CountList;
import com.resnik.util.objects.structures.CountObject;

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
//        Log.v(TAG,pairList);
        Multiplication[] retArray = new Multiplication[pairList.size()];
        for (int i = 0; i < retArray.length; i++) {
            Pair<Constant, Constant> currPair = pairList.get(i);
            Multiplication tempProduct = new Multiplication(
                    new Constant(currPair.getKey().getValue()),
                    Power.var(var, currPair.getValue().getValue()));
//            Log.v(TAG,Arrays.toString(tempProduct.values));
            retArray[i] = tempProduct;
        }
//        Log.v(TAG,"retArray:" + Arrays.toString(retArray));
        return retArray;
    }

    public static Multiplication[] generateProducts(Constant[] coefficients, Constant[] exponents) {
        return generateProducts(Variable.X, coefficients, exponents);
    }

    public static Multiplication[] generateProducts(Constant[] coefficients) {
        double[] exponents = ArrayUtils.generateSequenceDouble(coefficients.length - 1, -1, -1);
        Constant[] exponent_constants = Constant.parseArray(exponents);
//        Log.v(TAG,"exponent_constants:" + Arrays.toString(exponent_constants));
        Multiplication[] retProducts = generateProducts(coefficients, exponent_constants);
//        Log.v(TAG,"retProducts:" + Arrays.toString(retProducts));
        return retProducts;
    }

    public static Multiplication[] generateProducts(Variable var1, Constant[] coefficients) {
        double[] exponents = ArrayUtils.generateSequenceDouble(coefficients.length - 1, -1, -1);
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

    public static class SquaredPolynomial extends Polynomial2d implements Rootable, Factorable {

        public final Constant a,b,c;

        public SquaredPolynomial(Constant a, Constant b, Constant c) {
            super(new Constant[]{a,b,c});
            if(a.equals(Constant.ZERO)){
                throw new IllegalArgumentException("First coefficient cannot be zero.");
            }
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public Constant determinant(){
            return this.b.multiply(b).subtract(new Constant(4).multiply(a).multiply(c)).constantRepresentation();
        }

        @Override
        public Constant[] findRoots() {
            Constant negative_b = this.b.multiply(Constant.NEGATIVE_ONE).constantRepresentation();
            Constant two_a = this.a.multiply(Constant.TWO).constantRepresentation();
            Constant firstRoot = negative_b.add(this.determinant().pow(Constant.ONE_HALF)).divide(two_a).constantRepresentation();
            Constant secondRoot = negative_b.subtract(this.determinant().pow(Constant.ONE_HALF)).divide(two_a).constantRepresentation();
            if(firstRoot.equals(secondRoot)){
                return new Constant[]{firstRoot};
            }
            return new Constant[]{firstRoot, secondRoot};
        }

        @Override
        public CountList<Operation> getFactors() {
            Constant[] roots = this.findRoots();
            CountList<Operation> retList = new CountList<>();
            for(Constant root : roots){
                retList.addElement(new Subtraction(Variable.X, root));
            }
            return retList;
        }

        @Override
        public Multiplication factoredForm() {
            CountList<Operation> factors = this.getFactors();
            Multiplication retMultiplication = null;
            List<Operation> operations = new ArrayList<>();
            for(CountObject<Operation> countObject : factors){
                if(countObject.getAmount() == 1){
                    operations.add(countObject.getElement());
                    continue;
                }
                operations.add(countObject.getElement().pow(new Constant(countObject.getAmount())));
            }
            retMultiplication = this.a.multiply(operations.get(0));
            if(operations.size() > 1){
                retMultiplication = retMultiplication.multiply(operations.get(1));
            }
            return retMultiplication;
        }
    }
    
}
