package com.resnik.util.math.symbo;

import com.resnik.util.logger.Log;
import com.resnik.util.math.symbo.operations.Constant;
import com.resnik.util.math.symbo.operations.Equation;
import com.resnik.util.math.symbo.operations.Operation;
import com.resnik.util.math.symbo.operations.Variable;
import com.resnik.util.math.symbo.parse.ParseException;
import com.resnik.util.math.symbo.parse.ParseString;
import com.resnik.util.math.symbo.parse.SymbolicSyntaxAnalyzer;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.resnik.util.math.symbo.parse.ParseString.balancedParenthesesCheck;


public class TestParse {

    public static final String TAG = TestParse.class.getSimpleName();

    @Test
    public void testConstant(){
        Log.v(TAG,Constant.parse("2.0 + 5i"));
    }

    @Test
    public void testSyntaxTree(){
        SymbolicSyntaxAnalyzer analyzer = new SymbolicSyntaxAnalyzer();
        // TODO : Support |x| --> abs(x)
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
        Log.v(TAG,ComplexNumber.parse("2.0 + 5*i"));
        Log.v(TAG,ComplexNumber.parse("5*i - 2"));
        balancedParenthesesCheck("(a)()");
    }

}
