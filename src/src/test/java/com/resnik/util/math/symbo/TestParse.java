package com.resnik.util.math.symbo;

import com.resnik.util.logger.Log;
import com.resnik.util.math.symbo.algebra.ComplexNumber;
import com.resnik.util.math.symbo.algebra.operations.Constant;
import com.resnik.util.math.symbo.algebra.operations.Equation;
import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.math.symbo.algebra.parse.SymbolicSyntaxAnalyzer;
import com.resnik.util.math.symbo.logic.operations.LogicalOperation;
import com.resnik.util.math.symbo.logic.parse.LogicalSyntaxAnalyzer;
import com.resnik.util.math.symbo.logic.parse.LogicalTokenizer;
import org.junit.Test;

import java.util.Arrays;

import static com.resnik.util.math.symbo.algebra.parse.ParseString.balancedParenthesesCheck;


public class TestParse {

    public static final String TAG = TestParse.class.getSimpleName();

    @Test
    public void testConstant(){
        Log.v(TAG,Constant.parse("2.0 + 5i"));
    }

    @Test
    public void testSyntaxTree(){
        SymbolicSyntaxAnalyzer analyzer = new SymbolicSyntaxAnalyzer();
        Operation operation = analyzer.analyze("sin(2x)+ (5/7)x + abs(20x)");
        Log.v(TAG,operation);
        Log.v(TAG,operation.getDerivativeX());
    }

    @Test
    public void testEquation(){
        Equation equation = Equation.parse("y=mx + b");
        Log.v(TAG,equation);
    }

    public static void main(String[] args) {
        Log.v(TAG, ComplexNumber.parse("2.0 + 5*i"));
        Log.v(TAG,ComplexNumber.parse("5*i - 2"));
        balancedParenthesesCheck("(a)()");
    }

}
