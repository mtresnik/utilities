package com.resnik.util.math.symbo.algebra.parse.intermediate;

import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.math.symbo.algebra.operations.functions.FunctionBuilder;

public class IntermediateFunction extends IntermediateUnaryOperator {

    String name;

    public IntermediateFunction(int startIndex, int endIndex, IntermediateOperation operation, String name) {
        super(startIndex, endIndex, operation);
        this.name = name;
    }

    @Override
    public Operation compile() {
        return FunctionBuilder.substitute(name, new Operation[]{operation.compile()});
    }
}
