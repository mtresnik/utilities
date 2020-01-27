package com.resnik.util.math.symbo.operations;

import com.resnik.util.math.symbo.Bounds;
import com.resnik.util.math.symbo.ComplexNumber;
import com.resnik.util.math.symbo.operations.base.Addition;
import com.resnik.util.math.symbo.operations.base.Multiplication;
import com.resnik.util.math.symbo.operations.base.Power;
import com.resnik.util.math.symbo.operations.bulk.Sigma;

public class Vector extends Operation<Constant> {

    protected String name; 
    protected Variable index_variable;

    public Variable getIndex_variable() {
        return index_variable;
    }

    public void setIndex_variable(Variable index_variable) {
        this.index_variable = index_variable;
    }

    public Vector(String name, Variable index_variable, Operation... values) {
        super(values);
        this.name = name;
        this.index_variable = index_variable;
    }
    
    public Vector(String name, Operation... values) {
        super(values);
        this.name = name;
        this.index_variable = null;
    }
    
    public Vector(String name, double... values) {
        this(name, null,values);
    }
    
    public Vector(String name, Variable index_variable, double ... values) {
        this(name, index_variable, Constant.parseArray(values));
    }

    @Override
    public String nonConstantString() {
        if(this.name == null){
            String retString = "<";
            for (int i = 0; i < this.values.length; i++) {
                retString += this.values[i];
                if(i < this.values.length - 1){
                    retString += ",";
                }
            }
            retString += ">";
            return retString;
        }
        return this.name +  (index_variable != null ? "_" + index_variable.toString() : "");
    }

    @Override
    public Operation getDerivative(Variable dVar) {
        Operation[] d_dVar = new Operation[this.values.length];
        for(int i = 0; i < this.values.length; i++){
            d_dVar[i] = this.values[i].getDerivative(dVar);
        }
        return new Vector(this.name+ "'", this.index_variable, d_dVar);
    }

    @Override
    public Vector generate(Operation[] newValues) {
        return new Vector(this.name, this.index_variable, newValues);
    }

    @Override
    public Operation evaluate(Constant c){
        double real = c.value.real;
        return this.evaluateReal(real);
    }
    
    @Override
    public Operation substitute(Variable var, Operation o) {
        if(var.equals(this.index_variable) && o.allConstants()){
            Constant c1 = o.constantRepresentation();
            return this.evaluate(c1);
        }
        return super.substitute(var, o);
    }
    
    @Override
    public Operation evaluateReal(double d){
        int cast = (int) d;
        if(cast < 0 || cast >= this.values.length){
            return null;
        }
        return this.values[cast];
    }
    
    @Override
    public boolean allConstants() {
        return false;
    }

    @Override
    public Constant constantRepresentation() {
        return Constant.NaN;
    }

    public Bounds getBounds(){
        return new Bounds(0, this.values.length - 1);
    }
    
    public Operation average(){
        Operation sum = this.sum();
        Operation retVal = sum.divide(new Constant(this.values.length));
        return retVal;
    }
    
    public Operation sum(){
        return sumPowReal(1);
    }
    
    public Operation sumPow(Operation o){
        if(this.index_variable == null){
            this.index_variable = new Variable("TEMP");
        }
        Power p = new Power(this, o);
        Sigma s1 = new Sigma(p, this.index_variable);
        Operation c1 = s1.evaluateBounds(
                this.getBounds()
        );
        if(this.index_variable.getName().equals("TEMP")){
            this.index_variable = null;
        }
        return c1;
    }
    
    public Operation sumPowComplex(ComplexNumber c){
        return sumPow(new Constant(c));
    }
    
    public Operation sumPowReal(double r){
        return sumPowComplex(ComplexNumber.a(r));
    }

    public boolean containsVar(Variable var){
        return super.containsVar(var) || var.equals(index_variable);
    }
    
    public int size(){
        return this.values.length;
    }

    @Override
    public Operation getIntegral(Variable dVar) {
        Operation[] d_dVar = new Operation[this.values.length];
        for(int i = 0; i < this.values.length; i++){
            d_dVar[i] = this.values[i].getIntegral(dVar);
        }
        return new Vector("INT" + this.name, this.index_variable, d_dVar);
    }

    // Not the same as the dot product because of complex numbers
    public Operation magnitude(){
        Operation[] sizes = new Operation[this.getValues().length];
        for(int i = 0; i < sizes.length; i++){
            Operation currOp = null;
            if(this.getValues()[i].allConstants()){
                Constant rep = this.getValues()[i].constantRepresentation();
                currOp = new Constant(rep.value.conjugate().multiply(rep.value));
            }else{
                currOp = new Multiplication(this.getValues()[i], this.getValues()[i]);
            }
            sizes[i] = currOp;
        }
        Addition innerSum = new Addition(sizes);
        return new Power(innerSum, new Constant(0.5));
    }

    public Operation dot(Vector other){
        if(this.size() != other.size()){
            throw new IllegalArgumentException("Vectors can only be dotted with the same size.");
        }
        Operation[] sizes = new Operation[this.getValues().length];
        for(int i = 0; i < sizes.length; i++){
            Operation currOp = new Multiplication(this.getValues()[i], other.getValues()[i]);
            sizes[i] = currOp;
        }
        return new Addition(sizes);
    }

    
    
}
