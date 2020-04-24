package com.resnik.util.math.symbo.algebra.operations.functions;

import com.resnik.util.math.symbo.algebra.operations.Constant;
import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.math.symbo.algebra.operations.Variable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class FunctionBuilder {


    public static Map<String, Function<Void, Operation>> generationMap = new LinkedHashMap<>();
    public static Map<String, Variable[]> paramsMap = new LinkedHashMap<>();

    public static Operation substitute(String name){
        return substitute(name, new Operation[0]);
    }

    public static Operation substitute(String name, Operation[] args){
        if(!generationMap.containsKey(name)){
            throw new IllegalArgumentException("Function definition doesn't exist:" + name);
        }
        if(!paramsMap.containsKey(name)){
            throw new IllegalArgumentException("Parameters don't exist:" + name);
        }
        if(paramsMap.get(name).length != args.length){
            throw new IllegalArgumentException("Mismatched parameters for function:" + name);
        }
        Operation operation = generationMap.get(name).apply(null);
        Variable[] params = paramsMap.get(name);
        for (int i = 0; i < args.length; i++) {
            operation = operation.substitute(params[i], args[i]);
        }
        return operation;
    }

    public static void generate(String name, Variable[] params, Function<Void,Operation> inner){
        if(generationMap.containsKey(name)){
            return;
        }
        if(inner.apply(null).getVariables().length != params.length){
            throw new IllegalArgumentException("Please define inner variables for function:" + name);
        }
        generationMap.put(name, inner);
        paramsMap.put(name, params);
    }

    static{
        generate("sin", new Variable[]{Variable.X}, (v) -> new Sine(Variable.X));
        generate("cos", new Variable[]{Variable.X}, (v) ->new Cosine(Variable.X));
        Variable base = new Variable("BASE");
        generate("log_", new Variable[]{base, Variable.X}, (v) ->new Log(base, Variable.X));
        generate("ln", new Variable[]{Variable.X}, (v) ->Log.ln(Variable.X));
        generate("log", new Variable[]{Variable.X}, (v) ->new Log(Constant.TEN, Variable.X));
        generate("abs", new Variable[]{Variable.X}, (v) ->new AbsoluteValue(Variable.X));
        generate("sqrt", new Variable[]{Variable.X}, (v) -> Variable.X.pow(new Constant(0.5)));
    }

}
