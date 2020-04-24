package com.resnik.util.math.symbo;

import com.resnik.util.logger.Log;
import com.resnik.util.math.MatrixUtils;
import com.resnik.util.math.plot.points.Point2d;
import com.resnik.util.math.symbo.algebra.Algebraic;
import com.resnik.util.math.symbo.algebra.ComplexNumber;
import com.resnik.util.math.symbo.algebra.interpolation.Regression;
import com.resnik.util.math.symbo.algebra.operations.*;
import com.resnik.util.math.symbo.algebra.operations.base.*;
import com.resnik.util.math.symbo.algebra.operations.bulk.Sigma;
import com.resnik.util.math.symbo.algebra.operations.polynomials.Polynomial2d;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TestSymbo {

    public static final String TAG = TestSymbo.class.getSimpleName();

    @Test
    public void testSymbo(){
        // 5x^2 + 4x + 3
        Operation poly1 =
                new Constant(5).multiply(Variable.X.pow(Constant.TWO))
                        .add(new Constant(4).multiply(Variable.X))
                        .add(new Constant(3));
        Log.v(TAG,poly1);
        Operation derivative1 = poly1.getDerivative(Variable.X);
        System.out.printf("%s\n\n",derivative1);

        // (2i+5)x + 4
        Operation poly2 = new Constant(new ComplexNumber(5,2)).multiply(Variable.X).add(new Constant(4));
        Log.v(TAG,poly2);
        Operation derivative2 = poly2.getDerivative(Variable.X);
        System.out.printf("%s\n\n",derivative2);

        Operation euler = Constant.E.pow(Constant.PI.multiply(Constant.I));
        Log.v(TAG,euler);
    }

    @Test
    public void testAdd(){
        Operation poly1 = Variable.X.add(Variable.Y).add(Variable.Z);
        Operation poly2 = new Parentheses(Variable.Y.add(Variable.X)).add(new Parentheses(Variable.Z));
        Log.v(TAG,poly1);
        Log.v(TAG,poly2);
        Log.v(TAG,poly1.equals(poly2));
    }

    @Test
    public void testSub() {
        Variable x = Variable.X, y = Variable.Y;
        Addition poly1 = new Addition(new Multiplication(x, new Constant(4)), new Multiplication(y, new Constant(3)));
        Log.v(TAG,poly1);
        Operation poly2 = poly1.substitute(x, Power.var(x, x));
        Log.v(TAG,poly2);
        Log.v(TAG,poly2.getDerivativeX());
        Operation o1 = poly1.evaluate(x, new Constant(0.0));
        Log.v(TAG,o1);
    }

    @Test
    public void testMatrix() {
        Variable x = Variable.X, y = Variable.Y;
        Addition poly1 = new Addition(x, y);

        Variable[] abcd = Variable.genVariables("a", "b", "c", "d");
        Algebraic[][] matTest = new Algebraic[][]{
                {abcd[0], abcd[1]},
                {abcd[2], abcd[3]}
        };
        Algebraic det = MatrixUtils.det(matTest);
        Log.v(TAG,det);

    }

    @Test
    public void testDerivatives() {
        Variable f = new Variable("f"), g = new Variable("g");
        Addition testAdd = new Addition(f, g);
        Subtraction testSub = new Subtraction(f, g);
        Multiplication testMult = new Multiplication(f, g);
        Division testDiv = new Division(f, g);
        Power testPow = new Power(f, g);

        Log.v(TAG,"Addition:" + testAdd);
        Log.v(TAG,"derivative:" + testAdd.getDerivativeX());
        Log.v(TAG,"derivative^2:" + testAdd.getDerivativeX(2));
        Log.v(TAG,"derivative_f:" + testAdd.getDerivativeX(2).getDerivative(f));
        Log.v(TAG,"");
        Log.v(TAG,"Subtraction:" + testSub);
        Log.v(TAG,"derivative:" + testSub.getDerivativeX());
        Log.v(TAG,"derivative^2:" + testSub.getDerivativeX(2));
        Log.v(TAG,"");
        Log.v(TAG,"Multiplication:" + testMult);
        Log.v(TAG,"derivative:" + testMult.getDerivativeX());
        Log.v(TAG,"derivative^2:" + testMult.getDerivativeX(2));
        Log.v(TAG,"");
        Log.v(TAG,"Division:" + testDiv);
        Log.v(TAG,"derivative:" + testDiv.getDerivativeX());
        Log.v(TAG,"");
        Log.v(TAG,"Power:" + testPow);
        Log.v(TAG,"derivative:" + testPow.getDerivativeX());
    }

    public static void testLinearRegressionPlot() {
        Vector x = new Vector("x", Variable.I, 0);
        Operation o = x.average();
        Log.v(TAG,"average:" + o);
        Power p = new Power(x, new Constant(2));
        Sigma s = new Sigma(p, Variable.I);
        Log.v(TAG,s.evaluateBoundsToString(x.getBounds()));
        Point2d[] points = Point2d.parsePoints(
                12.4, 11.2,
                14.3, 12.5,
                14.5, 12.7,
                14.9, 13.1,
                16.1, 14.1,
                16.9, 14.8,
                16.5, 14.4,
                15.4, 13.4,
                17, 14.9,
                17.9, 15.6,
                18.8, 16.4,
                20.3, 17.7,
                22.4, 19.6,
                19.4, 16.9,
                15.5, 14,
                16.7, 14.6,
                17.3, 15.1,
                18.4, 16.1,
                19.2, 16.8,
                17.4, 15.2,
                19.5, 17,
                19.7, 17.2,
                21.2, 18.6
        );
        Regression r = new Regression(1, points);
        r.plot();
        Vector[] x_y = Point2d.toVectors(points);
        Log.v(TAG,Arrays.toString(points));
        Log.v(TAG,Arrays.toString(x_y[0].getValues()));
        Log.v(TAG,Arrays.toString(x_y[1].getValues()));
        Log.v(TAG,"poly:" + r.polynomial);
        Log.v(TAG,"r^2:" + r.rSquared());
    }

    @Test
    public void testRoots() {
        Polynomial2d.SquaredPolynomial polynomial = new Polynomial2d.SquaredPolynomial(new Constant(1), new Constant(6), new Constant(-40));
        Log.v(TAG,Arrays.toString(polynomial.findRoots()));
        Log.v(TAG,polynomial.getFactors());
        Log.v(TAG,polynomial.factoredForm());
        Equation tempEq = new Equation(polynomial, Constant.ZERO);
        Log.v(TAG,Equation.solutions(tempEq, 10));
    }

    @Test
    public void testEquation() {
        Variable x = Variable.X, y = Variable.Y;
        Addition poly1 = new Addition(new Power(x, Constant.TWO), new Power(y, Constant.TWO));
        Operation poly2 = new Constant(0);
        Equation equation = new Equation(poly1, poly2);
        Log.v(TAG,Equation.solutions(equation, 3));
    }

    @Test
    public void testEquationSatisfies(){
        Equation e = Equation.parse("y = 2x ");
        List<Map<Variable, Double>> res = e.satisfies10();
        Log.v(TAG,res);
    }

    public static void testEquationPlot(){
        Operation poly = (Variable.X.pow(Constant.TWO).add(Variable.Y.pow(Constant.TWO)).add(Constant.ONE)).multiply(new Constant(0.05));
        Operation poly2 = Variable.X.add(Variable.Y.multiply(Variable.Y).multiply(Constant.NEGATIVE_ONE)).subtract(new Constant(3));
        Equation tempEq = new Equation(poly, poly2);
        tempEq.solveAndPlot();
    }

    @Test
    public void testVector() {
        Vector v = new Vector("v", new Constant(2), new Constant(3));
        Vector u = new Vector("u", new Constant(4), new Constant(5));
        Log.v(TAG,Arrays.toString(v.getValues()));
        Log.v(TAG,v.magnitude());
        Log.v(TAG,v.dot(v));
        Log.v(TAG,v.dot(u));
    }

    public static void main(String[] args){
        testEquationPlot();
//        testLinearRegressionPlot();
    }

}
