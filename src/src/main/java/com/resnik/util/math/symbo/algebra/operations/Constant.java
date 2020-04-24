package com.resnik.util.math.symbo.algebra.operations;

import com.resnik.util.math.symbo.algebra.ComplexNumber;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

import com.resnik.util.math.symbo.algebra.Algebraic;
import com.resnik.util.math.symbo.algebra.operations.base.Multiplication;

public class Constant extends Operation<Algebraic> {

    public static final Constant ZERO = new Constant(ComplexNumber.ZERO), 
            ONE = new Constant(ComplexNumber.ONE),
            NEGATIVE_ONE = new Constant(ComplexNumber.NEGATIVE_ONE),
            TWO = new Constant(ComplexNumber.TWO),
            ONE_HALF = new Constant(ComplexNumber.ONE_HALF),
            TEN = new Constant(ComplexNumber.TEN),
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

    public static final Comparator<Constant> REAL_COMPARATOR = (a,b) -> ComplexNumber.REAL_COMPARATOR.compare(a.value,b.value);
    public static final Comparator<Constant> IMAGINARY_COMPARATOR = (a,b) -> ComplexNumber.IMAGINARY_COMPARATOR.compare(a.value,b.value);
    public static final Comparator<Constant> R_COMPARATOR = (a,b) -> ComplexNumber.REAL_COMPARATOR.compare(a.value,b.value);

    @Override
    public Constant getDerivative(Variable dVar) {
        return new Constant(ComplexNumber.ZERO);
    }

    @Override
    public String nonConstantString() {
        if(this.value.equals(ComplexNumber.PI)){
            return "π";
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

    @Override
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

    @Override
    public Operation getIntegral(Variable dVar) {
        return new Multiplication(new Constant(this.getValue()), dVar);
    }

    private static void imaginaryCheck(Constant one, Constant two){
        if(one.value.imaginary != 0.0 || two.value.imaginary != 0.0){
            throw new IllegalStateException("Cannot compare complex numbers this way.");
        }
    }

    public boolean lessThan(Constant other){
        imaginaryCheck(this, other);
        return this.value.real < other.value.real;
    }

    public boolean lessThanOrEqualTo(Constant other){
        if(this.equals(other)){
            return true;
        }
        imaginaryCheck(this, other);
        return this.value.real <= other.value.real;
    }

    public boolean greaterThan(Constant other){
        imaginaryCheck(this, other);
        return this.value.real > other.value.real;
    }

    public boolean greaterThanOrEqualTo(Constant other){
        if(this.equals(other)){
            return true;
        }
        imaginaryCheck(this, other);
        return this.value.real >= other.value.real;
    }

    public boolean equalTo(Constant other){
        return this.equals(other);
    }

    public boolean isInteger(){
        return this.value.isInteger();
    }

    public static Constant parse(String strVal){
        ComplexNumber c = ComplexNumber.parse(strVal);
        return new Constant(c);
    }

    public static List<Constant> fromComplexNumberList(List<ComplexNumber> inputList){
        List<Constant> retList = new ArrayList<>();
        inputList.forEach((elem)->{retList.add(new Constant(elem));});
        return retList;
    }

    public static List<Constant> loadFromTxt(String fileLocation) throws FileNotFoundException{
        File file = new File(fileLocation);
        if(!file.exists()) throw new FileNotFoundException("File:" + file.getAbsolutePath() + " cannot be found.");
        List<Constant> retList = new ArrayList<>();
        Scanner sc = new Scanner(file);
        while(sc.hasNext()){
            String line = sc.nextLine();
            retList.add(Constant.parse(line));
        }
        sc.close();
        return retList;
    }

    public static boolean saveToTxt(List<Constant> input, String fileLocation) throws FileNotFoundException {
        File file = new File(fileLocation);
        PrintWriter pw = new PrintWriter(file);
        for(Constant c : input){
            pw.println(c.toString());
        }
        pw.close();
        return true;
    }

}
