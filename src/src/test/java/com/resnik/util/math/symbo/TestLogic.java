package com.resnik.util.math.symbo;

import com.resnik.util.logger.Log;
import com.resnik.util.math.symbo.logic.LogicalConstant;
import com.resnik.util.math.symbo.logic.LogicalVariable;
import com.resnik.util.math.symbo.logic.operations.LogicalOperation;
import com.resnik.util.math.symbo.logic.parse.LogicalSyntaxAnalyzer;
import com.resnik.util.math.symbo.logic.parse.LogicalTokenizer;
import org.junit.Test;

public class TestLogic {

    public static final String TAG = TestLogic.class.getSimpleName();

    @Test
    public void testLogicalTokenizer(){
        String testString = "!ab!c";
        LogicalTokenizer tokenizer = new LogicalTokenizer();
        System.out.println(tokenizer.tokenize(testString));
    }

    @Test
    public void testLogicalSyntax(){
        LogicalSyntaxAnalyzer analyzer = new LogicalSyntaxAnalyzer();
        LogicalOperation operation = analyzer.analyze("true or false");
        Log.v(TAG, operation);
        operation = analyzer.analyze("a && b");
        Log.v(TAG, operation);
        System.out.println(analyzer.analyze("a || b"));
        operation = analyzer.analyze("!ab!c or abc implies c & d NAND e");
        System.out.println(operation);
    }

    @Test
    public void testOr(){
        LogicalVariable a = LogicalVariable.A;
        LogicalVariable b = LogicalVariable.B;
        LogicalOperation operation = a.or(b);
        Log.v(TAG, operation);
        LogicalOperation next = operation.substitute(a, LogicalConstant.TRUE);
        LogicalOperation after = next.substitute(b, LogicalConstant.FALSE);
        Log.v(TAG, next);
        Log.v(TAG, after);
        Log.v(TAG, a.substitute(a, LogicalConstant.TRUE));
    }

    @Test
    public void testAnd(){
        LogicalVariable a = LogicalVariable.A;
        LogicalVariable b = LogicalVariable.B;
        LogicalOperation operation = a.and(b);
        Log.v(TAG, operation);
        LogicalOperation next = operation.substitute(a, LogicalConstant.TRUE);
        Log.v(TAG, next);
        LogicalOperation after = next.substitute(b, LogicalConstant.FALSE);
        Log.v(TAG, after);
    }

    @Test
    public void testXor(){
        LogicalVariable a = LogicalVariable.A;
        LogicalVariable b = LogicalVariable.B;
        LogicalOperation operation = a.xor(b);
        Log.v(TAG, operation);
        LogicalOperation next = operation.substitute(a, LogicalConstant.TRUE);
        LogicalOperation after = next.substitute(b, LogicalConstant.FALSE);
        Log.v(TAG, next);
        Log.v(TAG, after);
        next = operation.substitute(a, LogicalConstant.FALSE);
        after = next.substitute(b, LogicalConstant.TRUE);
        Log.v(TAG, next);
        Log.v(TAG, after);
        next = operation.substitute(a, LogicalConstant.TRUE);
        after = next.substitute(b, LogicalConstant.TRUE);
        Log.v(TAG, next);
        Log.v(TAG, after);
        next = operation.substitute(a, LogicalConstant.FALSE);
        after = next.substitute(b, LogicalConstant.FALSE);
        Log.v(TAG, next);
        Log.v(TAG, after);
    }

    @Test
    public void testNot(){
        LogicalVariable a = LogicalVariable.A;
        LogicalOperation operation = a.not();
        Log.v(TAG, operation);
        LogicalOperation next = operation.substitute(a, LogicalConstant.TRUE);
        Log.v(TAG, next);
        next = operation.substitute(a, LogicalConstant.FALSE);
        Log.v(TAG, next);
    }

    @Test
    public void testImplies(){
        LogicalVariable a = LogicalVariable.A;
        LogicalVariable b = LogicalVariable.B;
        LogicalOperation operation = a.implies(b);
        LogicalOperation next = operation.substitute(a, LogicalConstant.TRUE);
        LogicalOperation after = next.substitute(b, LogicalConstant.TRUE);
        Log.v(TAG, operation);
        Log.v(TAG, next);
        Log.v(TAG, after);
    }

    @Test
    public void testIff(){
        LogicalVariable a = LogicalVariable.A;
        LogicalVariable b = LogicalVariable.B;
        LogicalOperation operation = a.iff(b);
        Log.v(TAG, operation);
        Log.v(TAG, operation.getStateMapString());
        operation = operation.substitute(b, a);
        Log.v(TAG, operation);
        Log.v(TAG, operation.constantRepresentation());
        Log.v(TAG, operation.getStateMapString());
    }

    @Test
    public void testEval(){
        LogicalVariable a = LogicalVariable.A;
        LogicalVariable b = LogicalVariable.B;
        LogicalVariable c = LogicalVariable.C;
        LogicalOperation operation = a.implies(b.or(c));
        LogicalVariable[] variables = operation.getVariables();
        Log.v(TAG, variables);
        Log.v(TAG, operation.getStateMap());
        Log.v(TAG, operation.getStateMapString());
    }


}
