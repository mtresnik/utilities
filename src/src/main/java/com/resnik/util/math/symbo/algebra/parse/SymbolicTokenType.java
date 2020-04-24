package com.resnik.util.math.symbo.algebra.parse;

import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.math.symbo.algebra.operations.functions.FunctionBuilder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public enum SymbolicTokenType {
    OPERATOR, NUMBER,
    OPEN_PARENTHESES, CLOSED_PARENTHESES,
    TEXT, FUNCTION, VARIABLE;

    public static Map<String, SymbolicTokenType> DEFAULT_FUNCTIONS = new LinkedHashMap<>();
    static{
        for(Map.Entry<String, Function<Void, Operation>> entry : FunctionBuilder.generationMap.entrySet()){
            DEFAULT_FUNCTIONS.put(entry.getKey(), FUNCTION);
        }
    }

}
