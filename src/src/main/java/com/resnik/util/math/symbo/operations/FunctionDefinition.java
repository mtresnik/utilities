package com.resnik.util.math.symbo.operations;

import java.util.LinkedHashMap;
import java.util.Map;

public class FunctionDefinition extends Operation{

    public static final Map<String, FunctionDefinition> ALL_DEFINED_FUNCTIONS = new LinkedHashMap();

    public final String name;
    public final Operation operation;

    public FunctionDefinition(String name, Operation operation){
        this.name = name;
        if(ALL_DEFINED_FUNCTIONS.containsKey(name)){
            System.err.println("Function with the name:" + name + " already exists, using preexisting operation instead of parameter.");
            this.operation = ALL_DEFINED_FUNCTIONS.get(name).operation;
        }else{
            this.operation = operation;
            ALL_DEFINED_FUNCTIONS.put(name, this);
        }
    }

    @Override
    public Constant constantRepresentation() {
        return operation.constantRepresentation();
    }

    @Override
    public String nonConstantString() {
        return operation.nonConstantString();
    }

    @Override
    public Operation getDerivative(Variable dVar) {
        return operation.getDerivative(dVar);
    }

    @Override
    public Operation getIntegral(Variable dVar) {
        return operation.getDerivative(dVar);
    }

    @Override
    public Object generate(Operation[] newValues) {
        return new FunctionDefinition(name, newValues[0]);
    }
}
