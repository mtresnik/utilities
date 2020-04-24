package com.resnik.util.math.symbo.logic.parse;

public class LogicalParseString {

    public static void balancedParenthesesCheck(String parseVal) throws LogicalParseException {
        parseVal = parseVal.replaceAll(" ","");
        int parentheses = 0;
        for(char c : parseVal.toCharArray()){
            if(c == '('){
                parentheses--;
            }
            if(c == ')'){
                if(parentheses == 0){
                    throw new LogicalParseException("Imbalanced parentheses.");
                }
                parentheses++;
            }
        }
        if(parentheses != 0){
            throw new LogicalParseException("Imbalanced parentheses.");
        }
    }

}
