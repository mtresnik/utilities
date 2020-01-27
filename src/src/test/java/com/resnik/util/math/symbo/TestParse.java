package com.resnik.util.math.symbo;

import com.resnik.util.math.symbo.operations.Constant;
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

    @Test
    public void testConstant(){
        System.out.println(Constant.parse("2.0 + 5i"));
    }

    @Test
    public void testSyntaxTree(){
        SymbolicSyntaxAnalyzer analyzer = new SymbolicSyntaxAnalyzer();
        // TODO : Support |x| --> abs(x)
        Operation operation = analyzer.analyze("sin(2x)+ (5/7)x + abs(20x)");
        System.out.println(operation);
        System.out.println(operation.getDerivativeX());
    }

    public static void main(String[] args) {
        System.out.println(ComplexNumber.parse("2.0 + 5*i"));
        System.out.println(ComplexNumber.parse("5*i - 2"));
        balancedParenthesesCheck("(a)()");
    }



}
