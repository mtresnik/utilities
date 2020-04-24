package com.resnik.util.math.symbo.logic.operations;

import com.resnik.util.logger.Log;
import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.math.symbo.logic.LogicalConstant;
import com.resnik.util.math.symbo.logic.LogicalInterface;
import com.resnik.util.math.symbo.logic.LogicalVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public abstract class LogicalOperation<EVAL extends LogicalInterface>
        implements LogicalInterface<
        LogicalOperation, And,
        LogicalOperation, Or,
        LogicalOperation, Xor,
        Not,
        LogicalOperation, Implies>{


    protected LogicalOperation[] values;

    public LogicalOperation(LogicalOperation ... values) {
        this.values = values;
    }

    @Override
    public And and(LogicalOperation a1) {
        return new And(this, a1);
    }

    @Override
    public Or or(LogicalOperation a1) {
        return new Or(this, a1);
    }

    @Override
    public Xor xor(LogicalOperation a1) {
        return new Xor(this,a1);
    }

    @Override
    public Not not() {
        return new Not(this);
    }

    @Override
    public Implies implies(LogicalOperation a1){
        return new Implies(this, a1);
    }

    public boolean allConstants(){
        for(LogicalOperation elem : this.values){
            if(elem.allConstants() == false || elem.constantRepresentation() == null){
                return false;
            }
        }
        return true;
    }

    public abstract LogicalConstant constantRepresentation();

    public LogicalVariable[] getVariables(){
        LogicalVariable[] retArray;
        List<LogicalVariable> varList = new ArrayList();
        for (int i = 0; i < this.values.length; i++) {
            LogicalOperation currOp = this.values[i];
            if (currOp.allConstants()) {
                continue;
            }
            LogicalVariable[] allVars = currOp.getVariables();
            for (LogicalVariable currVar : allVars) {
                if (varList.contains(currVar) == false) {
                    varList.add(currVar);
                }
            }
        }
        retArray = varList.toArray(new LogicalVariable[varList.size()]);
        Arrays.sort(retArray, new Comparator<LogicalVariable>() {
            @Override
            public int compare(LogicalVariable t, LogicalVariable t1) {
                return t.name.compareTo(t1.name);
            }
        });
        return retArray;
    }

    public boolean containsVar(LogicalVariable variable){
        if(this instanceof LogicalVariable){
            return this.equals(variable);
        }
        for(LogicalOperation curr : this.values){
            if(curr.equals(variable)){
                return true;
            }
        }
        return false;
    }

    public LogicalOperation[] varOperations(LogicalVariable variable){
        LogicalOperation[] retArray;
        List<LogicalOperation> validOperationList = new ArrayList<>();
        for(LogicalOperation curr : this.values){
            if(curr instanceof LogicalConstant || curr.allConstants()){
                continue;
            }
            if(curr.containsVar(variable)){
                validOperationList.add(curr);
            }
        }
        if(validOperationList.isEmpty()){
            return new LogicalOperation[]{};
        }
        retArray = new LogicalOperation[validOperationList.size()];
        retArray = validOperationList.toArray(retArray);
        return retArray;
    }

    public LogicalOperation substitute(LogicalVariable variable, boolean value){
        return this.substitute(variable, new LogicalConstant(value));
    }

    public LogicalOperation substitute(LogicalVariable variable, LogicalOperation operation){
        if (this.containsVar(variable) == false) {
            return this;
        }
        if(this instanceof LogicalVariable && this.equals(variable)){
            return operation;
        }
        LogicalOperation[] newValues = new LogicalOperation[this.values.length];
        for (int i = 0; i < this.values.length; i++) {
            LogicalOperation currVal = this.values[i];
            LogicalOperation replacement = currVal.substitute(variable, operation);
            newValues[i] = replacement;
        }
        return this.generate(newValues);
    }


    public abstract String nonConstantString();


    public static LogicalOperation[] formatSuper2(LogicalOperation o1, LogicalOperation o2, LogicalOperation ... o_n){
        LogicalOperation[] retArray = new LogicalOperation[2 + o_n.length];
        retArray[0] = o1;
        retArray[1] = o2;
        int count = 2;
        for(int i = count; i < o_n.length + count; i++){
            retArray[i] = o_n[i - count];
        }
        return retArray;
    }

    public abstract <T> T generate(LogicalOperation[] ops);

    public LogicalOperation[] getValues() {
        return values;
    }

    @Override
    public String toString(){
        if(this.allConstants()){
            return this.constantRepresentation().nonConstantString();
        }
        return this.nonConstantString();
    }

}
