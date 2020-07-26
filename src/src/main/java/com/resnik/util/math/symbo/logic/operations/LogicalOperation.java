package com.resnik.util.math.symbo.logic.operations;

import com.resnik.util.math.symbo.logic.LogicalConstant;
import com.resnik.util.math.symbo.logic.LogicalInterface;
import com.resnik.util.math.symbo.logic.LogicalVariable;

import java.util.*;

public abstract class LogicalOperation<EVAL extends LogicalInterface>
        implements LogicalInterface<
        LogicalOperation, And,
        LogicalOperation, Or,
        LogicalOperation, Xor,
        LogicalNegation,
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
    public LogicalNegation not() {
        return new LogicalNegation(this);
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


    public String getStateMapString(){
        Map<Map<LogicalVariable, Boolean>, Boolean> stateMap = getStateMap();
        String retString = "\n" + this.toString() + "\n";
        retString += Arrays.toString(this.getVariables()) + "\n";
        for(Map.Entry<Map<LogicalVariable, Boolean>, Boolean> entry : stateMap.entrySet()){
            String lineString = "";
            for(Map.Entry<LogicalVariable, Boolean> subEntry : entry.getKey().entrySet()){
                boolean val = subEntry.getValue();
                if(val){
                    lineString += "1";
                }else {
                    lineString += "0";
                }
                lineString += "\t";
            }
            lineString += "=" + (entry.getValue() ? "1" : "0");
            retString += lineString + "\n";
        }
        return retString;
    }

    public Map<Map<LogicalVariable, Boolean>, Boolean> getStateMap(){
        Map<Map<LogicalVariable, Boolean>, Boolean> retMap = new LinkedHashMap<>();
        LogicalVariable[] variables = getVariables();
        if(variables.length == 0){
            return retMap;
        }
        int numIterations = (int) Math.pow(2, variables.length);
        boolean[] testSet = new boolean[variables.length];
        for(int count = 0; count <= numIterations; count++){
            LogicalOperation currOp = this;
            Map<LogicalVariable, Boolean> innerMap = new LinkedHashMap<>();
            for(int varIndex = 0; varIndex < variables.length; varIndex++){
                LogicalVariable logicalVariable = variables[varIndex];
                boolean value = testSet[varIndex];
                currOp = currOp.substitute(logicalVariable, value);
                innerMap.put(logicalVariable, value);
            }
            LogicalConstant constant = currOp.constantRepresentation();
            if(constant == null){
                System.out.println("null constant for:" + currOp);
            }
            retMap.put(innerMap, constant.getValue());
            String bitRep = Integer.toString(count, 2);
            while(bitRep.length() < testSet.length){
                bitRep = "0" + bitRep;
            }
            for(int varAssign = 0; varAssign < testSet.length; varAssign++){
                char c = bitRep.charAt(varAssign);
                testSet[varAssign] = c != '0';
            }

        }
        return retMap;
    }

    public LogicalVariable[] getVariables(){
        LogicalVariable[] retArray;
        List<LogicalVariable> varList = new ArrayList();
        for (int i = 0; i < this.values.length; i++) {
            LogicalOperation currOp = this.values[i];
            LogicalVariable[] allVars = currOp.getVariables();
            for (LogicalVariable currVar : allVars) {
                if (!varList.contains(currVar)) {
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
        for(LogicalVariable variable1 : this.getVariables()){
            if(variable.equals(variable1)){
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
