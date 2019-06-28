package util.math.symbo;

import util.math.Bounds;
import util.math.ComplexNumber;

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
    protected String nonConstantString() {
        return this.name +  (index_variable != null ? "_" + index_variable.toString() : "");
    }

//    @Override
//    public ComplexNumber evaluate(ComplexNumber t){
//        throw new UnsupportedOperationException("Evaluation on Vectors is not supported.");
//    }

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
//        System.out.println("sum:" + sum);
        Operation retVal = sum.divide(new Constant(this.values.length));
//        System.out.println("n:" + this.values.length);
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
        System.out.println("s1:" + s1);
        Operation c1 = s1.evaluateBounds(
                this.getBounds()
        ); // SUM
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
    
    
    
}
