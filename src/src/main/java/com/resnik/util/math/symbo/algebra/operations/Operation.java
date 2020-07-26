package com.resnik.util.math.symbo.algebra.operations;

import com.resnik.util.logger.Log;
import com.resnik.util.math.symbo.algebra.Algebraic;
import com.resnik.util.math.symbo.algebra.Bounds;
import com.resnik.util.math.symbo.algebra.ComplexNumber;
import com.resnik.util.math.symbo.algebra.operations.base.*;
import com.resnik.util.math.symbo.algebra.operations.bulk.Sigma;
import com.resnik.util.math.symbo.algebra.operations.functions.AbsoluteValue;
import com.resnik.util.math.symbo.algebra.parse.SymbolicSyntaxAnalyzer;

import java.util.*;

public abstract class Operation<EVAL extends Algebraic> 
        implements Algebraic<Operation, Addition, Operation, Subtraction, Operation, Multiplication, Operation, Division, Operation, Power> {

    public static final String TAG = Operation.class.getSimpleName();

    protected Operation[] values;

    protected Operation(Operation... values) {
        this.values = values;
    }

    public abstract Constant constantRepresentation();

    public boolean allConstants() {
        for (Operation elem : this.values) {
            if ((elem instanceof Constant || elem.allConstants()) == false) {
                return false;
            }
        }
        return true;
    }

    @Override
    public final String toString() {
        if (this.allConstants()) {
            return this.constantRepresentation().nonConstantString();
        }
        return this.nonConstantString();
    }

    public abstract String nonConstantString();

    public Operation evaluatePrint(Variable var, EVAL t) {
        Log.v(TAG,"Eval(var: " + var + " = " + t + ")");
        return Operation.this.evaluate(var, t);
    }

    public Operation evaluatePrint(Collection<Variable> var, Collection<Algebraic> t) {
        Iterator<Variable> varIter = var.iterator();
        Iterator<Algebraic> numberIter = t.iterator();
        while (varIter.hasNext() && numberIter.hasNext()) {
            Variable currVar = varIter.next();
            Algebraic currNumber = numberIter.next();
            Log.v(TAG,"Eval(var: " + currVar + " = " + currNumber + ")");
        }
        return Operation.this.evaluate(var, t);
    }

    public Operation evaluatePrintReal(Collection<Variable> var, Collection<Double> t) {
        Iterator<Variable> varIter = var.iterator();
        Iterator<Double> numberIter = t.iterator();
        while (varIter.hasNext() && numberIter.hasNext()) {
            Variable currVar = varIter.next();
            Double currNumber = numberIter.next();
            Log.v(TAG,"Eval(var: " + currVar + " = " + currNumber + ")");
        }
        return evaluateReal(var, t);
    }

    public Operation evaluateReal(Collection<Variable> var, Collection<Double> t) {
        return Operation.this.evaluate(var, ComplexNumber.aAlgebraic(ComplexNumber.a(t)));
    }

    public Operation evaluate(Collection<Variable> var, Collection<Algebraic> t) {
        Operation retOperation = this;
        Iterator<Variable> varIter = var.iterator();
        Iterator<Algebraic> numberIter = t.iterator();
        while (varIter.hasNext() && numberIter.hasNext()) {
            Variable currVar = varIter.next();
            Algebraic currNumber = numberIter.next();
            retOperation = retOperation.evaluate(currVar, currNumber);
        }
        return retOperation;
    }

    public Operation evaluate(Map<Variable, Algebraic> varMap) {
        return Operation.this.evaluate(varMap.keySet(), varMap.values());
    }

    public Operation evaluate(Variable var, EVAL t) {
        if (this.containsVar(var) == false) {
            return this;
        }
        if (this instanceof Variable && this.equals(var)) {
            if(t instanceof Operation){
                return (Operation) t;
            }
        }
        Operation[] newValues = new Operation[this.values.length];
        for (int i = 0; i < this.values.length; i++) {
            Operation currVal = this.values[i];
            Operation replacement = currVal.evaluate(var, t);
            newValues[i] = replacement;
        }
        return this.generate(newValues);
    }

    public Operation substitute(Variable var, Operation o) {
        if (this.containsVar(var) == false) {
            return this;
        }
        if (this instanceof Variable && this.equals(var)) {
            return o;
        }
        Operation[] newValues = new Operation[this.values.length];
        for (int i = 0; i < this.values.length; i++) {
            Operation currVal = this.values[i];
            Operation replacement = currVal.substitute(var, o);
            newValues[i] = replacement;
        }
        return this.generate(newValues);
    }

    public Operation evaluate(EVAL t){
        return this.evaluate(Variable.X, t);
    }

    public final Operation[] evaluateX(EVAL[] x_values) {
        Operation[] retArray = new Operation[x_values.length];
        for (int i = 0; i < x_values.length; i++) {
            retArray[i] = this.evaluate(x_values[i]);
        }
        return retArray;
    }

    public Operation evaluateReal(double real) {
        return this.evaluate( (EVAL)ComplexNumber.a(real));
    }

    public final Operation[] evaluateReal(double[] x_values) {
        Operation[] retArray = new Operation[x_values.length];
        for (int i = 0; i < x_values.length; i++) {
            retArray[i] = this.evaluateReal(x_values[i]);
        }
        return retArray;
    }

    public final Operation[] evaluatePrint(EVAL[] x_values) {
        Operation[] retArray = this.evaluateX(x_values);
        Log.v(TAG,"x:\t" + Arrays.toString(x_values));
        Log.v(TAG,"f(x):\t" + Arrays.toString(retArray));
        return retArray;
    }

    public static final double[] range(double start, double finish, int n) {
        double h = (finish - start) / n;
        double[] retArray = new double[n + 1];
        for (int i = 0; i <= n; i++) {
            retArray[i] = start + h * i;
        }
        return retArray;
    }

    public static Operation[] formatSuper1(Operation o_1, Operation... o_n) {
        Operation[] retArray = new Operation[1 + o_n.length];
        retArray[0] = o_1;
        int count = 1;
        for (int i = count; i < o_n.length + count; i++) {
            retArray[i] = o_n[i - count];
        }
        return retArray;
    }

    public static Operation[] formatSuper2(Operation o_1, Operation o_2, Operation... o_n) {
        Operation[] retArray = new Operation[1 + 1 + o_n.length];
        retArray[0] = o_1;
        retArray[1] = o_2;
        int count = 2;
        for (int i = count; i < o_n.length + count; i++) {
            retArray[i] = o_n[i - count];
        }
        return retArray;
    }

    public final Operation[] evaluateRangeReal(double start, double finish, int n) {
        double[] passArray = range(start, finish, n);
        return evaluateReal(passArray);
    }

    public final Operation getDerivativeX() {
        return getDerivative(Variable.X);
    }

    public final Operation getDerivativeX(int n) {
        Operation retDerivative = this;
        for (int i = 0; i < n; i++) {
            retDerivative = retDerivative.getDerivativeX();
        }
        return retDerivative;
    }

    public abstract Operation getDerivative(Variable dVar);

    public abstract Operation getIntegral(Variable dVar);
    
    public boolean containsVar(Variable var) {
        if (this instanceof Variable) {
//            Log.v(TAG,"instanceOf:" + this.toString());
            return var.equals(this);
        }
        return varOperations(var).length > 0;
    }

    public Operation[] varOperations(Variable var) {
        Operation[] retArray;
        List<Operation> validOperationsList = new ArrayList();
        for (Operation curr : this.values) {
            if (curr instanceof Constant || curr.allConstants()) {
//                Log.v(TAG,"all constants:" + curr);
                continue;
            }
            if (curr.containsVar(var)) {
                validOperationsList.add(curr);
            }
        }
        if (validOperationsList.isEmpty()) {
            return new Operation[]{};
        }
        retArray = new Operation[validOperationsList.size()];
        retArray = validOperationsList.toArray(retArray);
        return retArray;
    }

    public abstract <T> T generate(Operation[] newValues);

    public Operation[] getValues() {
        return values;
    }

    public Variable[] getVariables() {
        Variable[] retArray;
        List<Variable> varList = new ArrayList();
        for (int i = 0; i < this.values.length; i++) {
            Operation currOp = this.values[i];
            if (currOp.allConstants()) {
                continue;
            }
            Variable[] allVars = currOp.getVariables();
            for (Variable currVar : allVars) {
                if (varList.contains(currVar) == false) {
                    varList.add(currVar);
                }
            }
        }
        retArray = varList.toArray(new Variable[varList.size()]);
        Arrays.sort(retArray, new Comparator<Variable>() {
            @Override
            public int compare(Variable t, Variable t1) {
                return t.name.compareTo(t1.name);
            }
        });
        return retArray;
    }

    @Override
    public  Addition add(Operation a1) {
        return new Addition(this, a1);
    }

    @Override
    public Subtraction subtract(Operation a1) {
        return new Subtraction(this, a1);
    }

    @Override
    public Multiplication multiply(Operation a1) {
        return new Multiplication(this, a1);
    }

    @Override
    public Division divide(Operation a1) {
        return new Division(this, a1);
    }

    @Override
    public Power pow(Operation a1) {
        return new Power(this, a1);
    }

    public Power sqrt(){
        return pow(Constant.ONE_HALF);
    }

    public Operation evaluateAllSigmaBounds(Variable index_variable, Bounds b){
        Operation[] newOpList = new Operation[this.values.length];
        for (int i = 0; i < newOpList.length; i++) {
            if(this.values[i] instanceof Sigma){
                Sigma curr = (Sigma) this.values[i];
                if(curr.index_variable.equals(index_variable)){
                    newOpList[i] = curr.evaluateBounds(b);
                    continue;
                }
            }
            newOpList[i] = this.values[i];
        }
        return this.generate(newOpList);
    }

    public Operation wrap(){
        return new Parentheses(this);
    }

    public static Operation parse(String inputString){
        SymbolicSyntaxAnalyzer analyzer = new SymbolicSyntaxAnalyzer();
        return analyzer.analyze(inputString);
    }

    public AbsoluteValue abs(){
        return new AbsoluteValue(this);
    }

}
