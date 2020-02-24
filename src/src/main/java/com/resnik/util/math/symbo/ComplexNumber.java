package com.resnik.util.math.symbo;

import com.resnik.util.math.symbo.operations.Constant;
import com.resnik.util.math.symbo.operations.Operation;
import com.resnik.util.math.symbo.parse.ParseException;
import com.resnik.util.math.symbo.parse.SymbolicSyntaxAnalyzer;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.resnik.util.math.MathUtils.sign;

public class ComplexNumber
        implements Algebraic<ComplexNumber, ComplexNumber, ComplexNumber, ComplexNumber, ComplexNumber, ComplexNumber, ComplexNumber, ComplexNumber, ComplexNumber, ComplexNumber> {

    private static final double PRECISION = Double.parseDouble("1.0E-12");
    public static final ComplexNumber ONE = ComplexNumber.a(1),
            NEGATIVE_ONE = ComplexNumber.a(-1),
            TWO = ComplexNumber.a(2),
            ONE_HALF = ComplexNumber.a(0.5),
            ZERO = ComplexNumber.a(0),
            TEN = ComplexNumber.a(10),
            I = ComplexNumber.b(1),
            PI = ComplexNumber.a(Math.PI),
            E = ComplexNumber.a(Math.E),
            NaN = ComplexNumber.ab(Double.NaN, Double.NaN);

    public double real, imaginary;

    public ComplexNumber(double real, double imaginary) {
        this.real = (Math.abs(real) < PRECISION ? 0.0 : real);
        this.imaginary = (Math.abs(imaginary) < PRECISION ? 0.0 : imaginary);
    }

    public static final Comparator<ComplexNumber> REAL_COMPARATOR = Comparator.comparingDouble(a -> a.real);
    public static final Comparator<ComplexNumber> IMAGINARY_COMPARATOR = Comparator.comparingDouble(a -> a.imaginary);
    public static final Comparator<ComplexNumber> R_COMPARATOR = Comparator.comparingDouble(ComplexNumber::r);

    public static ComplexNumber ab(double a, double b) {
        return new ComplexNumber(a, b);
    }

    public static ComplexNumber a(double a) {
        return new ComplexNumber(a, 0);
    }

    public static ComplexNumber b(double b) {
        return new ComplexNumber(0, b);
    }

    public static ComplexNumber rTheta(double r, double theta) {
        double a = r * Math.cos(theta);
        double b = r * Math.sin(theta);
        return ab(a, b);
    }

    public ComplexNumber conjugate() {
        return new ComplexNumber(this.real, this.imaginary * -1.0);
    }

    public double rSquared() {
        return this.multiply(this.conjugate()).real;
    }

    public double r() {
        return Math.sqrt(rSquared());
    }

    public double theta() {
        double r = this.r();
        if (r == 0.0) {
            return 0.0;
        }
        if (real == r && imaginary == 0.0) {
            return 0.0;
        }
        if (real == 0.0 && imaginary == r) {
            return Math.PI / 2;
        }
        if (real == -1.0 * r && imaginary == 0.0) {
            return Math.PI;
        }
        if (real == 0.0 && imaginary == -1.0 * r) {
            return 3 * Math.PI / 2;
        }
        return Math.atan(imaginary / real);
    }

    public String toStringRTheta() {
        return "(r=" + this.r() + ", theta=" + this.theta() + ")";
    }

    public ComplexNumber pow(double x) {
        return this.pow(ComplexNumber.a(x));
    }

    public ComplexNumber pow(ComplexNumber c2) {
        if (this.equals(ZERO) && ZERO.equals(c2)) {
            return ComplexNumber.ONE;
        }
        if (this.equals(ZERO)) {
            return ComplexNumber.ZERO;
        }
        ComplexNumber log = complexLn(this);
        ComplexNumber exponent = log.multiply(c2);

        return exp(exponent);
    }

    public ComplexNumber add(ComplexNumber c2) {
        double a_1 = this.real + c2.real;
        double b_1 = this.imaginary + c2.imaginary;
        return ComplexNumber.ab(a_1, b_1);
    }

    public ComplexNumber subtract(ComplexNumber c2) {
        double a_1 = this.real - c2.real;
        double b_1 = this.imaginary - c2.imaginary;
        return ComplexNumber.ab(a_1, b_1);
    }

    public ComplexNumber multiply(ComplexNumber c2) {
        double a_3 = this.real * c2.real - this.imaginary * c2.imaginary;
        double b_3 = this.real * c2.imaginary + c2.real * this.imaginary;
        return ComplexNumber.ab(a_3, b_3);
    }

    public ComplexNumber divide(ComplexNumber c2) {
        double r_2 = c2.rSquared();
        if (r_2 == 0.0) {
            throw new ArithmeticException("Argument 'c2' is 0");
        }
        ComplexNumber numerator = this.multiply(c2.conjugate());
        ComplexNumber retNumber = numerator.scale(1 / r_2);
        return retNumber;
    }

    public ComplexNumber scale(double s) {
        double a_1 = this.real * s;
        double b_1 = this.imaginary * s;
        return ComplexNumber.ab(a_1, b_1);
    }

    public boolean isReal(){
        return this.imaginary == 0.0;
    }

    public boolean isInteger(){
        if(!isReal()) return false;
        return this.real == Math.floor(this.real);
    }

    @Override
    public String toString() {
        double a_s = sign(real), b_s = sign(imaginary);
        if (a_s == 0 && b_s == 0) {
            return 0.0 + "";
        }
        if (a_s == 0) {
            return imaginary + "i";
        }
        if (b_s == 0) {
            return real + "";
        }
        String sign = " - ";
        if (b_s == 1.0) {
            sign = " + ";
        }
        return "(" + real + (b_s == 1.0 ? " + " : " - ") + Math.abs(imaginary) + "i" + ")";
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
        final ComplexNumber other = (ComplexNumber) obj;
        if (Double.doubleToLongBits(this.real) != Double.doubleToLongBits(other.real)) {
            return false;
        }
        if (Double.doubleToLongBits(this.imaginary) != Double.doubleToLongBits(other.imaginary)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(real, imaginary);
    }

    public static ComplexNumber exp(ComplexNumber z) {
        double a = z.real, b = z.imaginary;
        double firstTerm = Math.exp(a);
        ComplexNumber secondTerm = exp_i(b);
        ComplexNumber retNumber = secondTerm.scale(firstTerm);
        return retNumber;
    }

    public static ComplexNumber pow(double base, ComplexNumber z) {
        double a = z.real, b = z.imaginary;
        double scalar = Math.pow(base, -1.0 * b);
        ComplexNumber retVal = real_i(base, a).scale(scalar);
        return retVal;
    }

    public static ComplexNumber exp_i(double theta) {
        double a_1 = Math.cos(theta);
        double b_2 = Math.sin(theta);
        return ComplexNumber.ab(a_1, b_2);
    }

    public static ComplexNumber real_i(double real, double theta) {
        if (real == 0.0) {
            if (theta == 0.0) {
                return ComplexNumber.ONE;
            } else {
                return ComplexNumber.ZERO;
            }
        }
        ComplexNumber complexLog = complexLn(ComplexNumber.a(real));
        double a = complexLog.real;
        double b = complexLog.imaginary;
        double scalar = Math.exp(-1.0 * b);
        ComplexNumber retNumber = exp_i(a);
        retNumber = retNumber.scale(scalar);
        return retNumber;
    }

    public static ComplexNumber complexLn(ComplexNumber c1) {
        if (ComplexNumber.ZERO.equals(c1)) {
            throw new IllegalArgumentException("Cannot take the log of:" + c1);
        }
        double r = c1.r();
        double theta = c1.theta();
        double a_1 = Math.log(r);
        double b_1 = theta;
        return ComplexNumber.ab(a_1, b_1);
    }

    public static List<ComplexNumber> a(Collection<Double> reals) {
        List<ComplexNumber> retList = new ArrayList();
        Iterator<Double> realIterator = reals.iterator();
        while (realIterator.hasNext()) {
            Double curr = realIterator.next();
            retList.add(a(curr));
        }
        return retList;
    }

    public static ComplexNumber[] a(double... reals) {
        ComplexNumber[] retArray = new ComplexNumber[reals.length];
        for (int i = 0; i < reals.length; i++) {
            retArray[i] = ComplexNumber.a(reals[i]);
        }
        return retArray;
    }

    public static List<Algebraic> aAlgebraic(Collection<ComplexNumber> reals) {
        List<Algebraic> retList = new ArrayList();
        Iterator<ComplexNumber> realIterator = reals.iterator();
        while (realIterator.hasNext()) {
            ComplexNumber curr = realIterator.next();
            retList.add((Algebraic) curr);
        }
        return retList;
    }

    public static ComplexNumber cos(ComplexNumber z) {
        /*
        cos(z) = (1/2)(exp(i*z) + exp(-i*z))
         */
        ComplexNumber firstTerm = exp(z.multiply(I));
        ComplexNumber secondTerm = exp(z.multiply(I).scale(-1.0));
        ComplexNumber numerator = firstTerm.add(secondTerm);
        return numerator.scale(0.5);
    }

    public static ComplexNumber sin(ComplexNumber z) {
        /*
        sin(z) = (1/2i)(exp(i*z) - exp(-i*z))
         */
        ComplexNumber firstTerm = exp(z.multiply(I));
        ComplexNumber secondTerm = exp(z.multiply(I).scale(-1.0));
        ComplexNumber numerator = firstTerm.subtract(secondTerm);
        return numerator.scale(-0.5).multiply(I);
    }

    public static ComplexNumber parse(String parseVal) throws ParseException {
        SymbolicSyntaxAnalyzer analyzer = new SymbolicSyntaxAnalyzer();
        Operation operation = analyzer.analyze(parseVal);
        if(!operation.allConstants()){
            throw new ParseException("Variable input.");
        }
        return operation.constantRepresentation().getValue();
    }

    public static List<ComplexNumber> fromConstantList(List<Constant> inputList){
        List<ComplexNumber> retList = new ArrayList<>();
        inputList.forEach((elem)->{retList.add(elem.getValue());});
        return retList;
    }

    public static List<ComplexNumber> loadFromTxt(String fileLocation) throws FileNotFoundException {
        return fromConstantList(Constant.loadFromTxt(fileLocation));
    }

    public static boolean saveToTxt(List<ComplexNumber> input, String fileLocation) throws FileNotFoundException {
        return Constant.saveToTxt(Constant.fromComplexNumberList(input), fileLocation);
    }

}
