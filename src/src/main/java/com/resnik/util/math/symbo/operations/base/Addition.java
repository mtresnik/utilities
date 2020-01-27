package com.resnik.util.math.symbo.operations.base;

import com.resnik.util.math.symbo.ComplexNumber;
import com.resnik.util.math.symbo.operations.Constant;
import com.resnik.util.math.symbo.operations.Operation;
import com.resnik.util.math.symbo.operations.Variable;

import java.util.ArrayList;
import java.util.List;

public class Addition<T extends Operation> extends Operation {

    public Addition(T o_1, T o_2, T... o_n) {
        super(formatSuper2(o_1, o_2, o_n));
    }

    public Addition(T[] o_n) {
        super(o_n);
    }

    @Override
    public Addition getDerivative(Variable dVar) {
        Operation d_1 = this.values[0].getDerivative(dVar);
        Operation d_2 = this.values[1].getDerivative(dVar);
        Operation[] remainder = (this.values.length > 2 ? new Operation[this.values.length - 2] : new Operation[]{});
        for (int i = 2; i < this.values.length; i++) {
            remainder[i - 2] = this.values[i].getDerivative(dVar);
        }
        return new Addition(d_1, d_2, remainder);
    }

    @Override
    public String nonConstantString() {
        String retString = "(";
        List<Operation> nonConstantList = new ArrayList();
        if (allConstants()) {
            return this.constantRepresentation().toString();
        }
        List<Constant> constantList = new ArrayList();
        for (Operation o : this.values) {
            if (o.toString().equals(Constant.ZERO.toString())) {
                continue;
            }
            if (o.allConstants() || o instanceof Constant) {
                constantList.add(o.constantRepresentation());
            } else {
                nonConstantList.add(o);
            }
        }
        if (nonConstantList.isEmpty() && constantList.isEmpty()) {
            return Constant.ZERO.toString();
        }
        for (int i = 0; i < nonConstantList.size(); i++) {
            retString += nonConstantList.get(i).toString();
            if (i < nonConstantList.size() - 1) {
                retString += " + ";
            }
        }
        if (constantList.isEmpty() == false) {
            ComplexNumber sum = ComplexNumber.ZERO;
            for (int i = 0; i < constantList.size(); i++) {
                sum = sum.add(constantList.get(i).getValue());
            }
            retString += " + " + new Constant(sum);
        }
        retString += ")";
        return retString;
    }

    public Addition generate(Operation[] ops) {
        return new Addition(ops);
    }

    @Override
    public Constant constantRepresentation() {
        if(allConstants() == false){
            return Constant.NaN;
        }
        Constant retConstant = Constant.ZERO;
        for (int i = 0; i < this.values.length; i++) {
            retConstant = new Constant(retConstant.getValue().add(this.values[i].constantRepresentation().getValue()));
        }
        return retConstant;
    }

    @Override
    public Operation getIntegral(Variable dVar) {
        Operation d_1 = this.values[0].getIntegral(dVar);
        Operation d_2 = this.values[1].getIntegral(dVar);
        Operation[] remainder = (this.values.length > 2 ? new Operation[this.values.length - 2] : new Operation[]{});
        for (int i = 2; i < this.values.length; i++) {
            remainder[i - 2] = this.values[i].getIntegral(dVar);
        }
        return new Addition(d_1, d_2, remainder);
    }

    public List<Operation> allValues(){
        List<Operation> retList = new ArrayList<>();
        for(Operation value : this.values){
            Operation temp = value;
            if(temp instanceof Parentheses){
                temp = ((Parentheses) temp).unWrap();
            }
            if(temp instanceof Addition){
                retList.addAll(((Addition) temp).allValues());
            }else{
                retList.add(temp);
            }
        }
        return retList;
    }

    public boolean equals(Object other){
        if(other == this) return true;
        if(!(other instanceof Addition)) return false;
        List<Operation> val1 = this.allValues();
        List<Operation> val2 = ((Addition) other).allValues();
        if(val1.size() != val2.size()) return false;
        for(Operation op : val1){
            if(!val2.contains(op)){
                return false;
            }
        }
        return true;
    }

}
