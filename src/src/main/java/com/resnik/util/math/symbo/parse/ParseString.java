package com.resnik.util.math.symbo.parse;

import com.resnik.util.logger.Log;
import com.resnik.util.math.symbo.operations.Constant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseString {

    public static final String TAG = ParseString.class.getSimpleName();

    public static Constant parseConstant(String string) throws ParseException {
        string = string.replaceAll(" ", "");
        String pattern1 = "(\\d+(,\\d+)?(\\.\\d+)?)i";
        if(string.matches(pattern1)){
            Pattern pattern = Pattern.compile(pattern1);
            Matcher matcher = pattern.matcher(string);
            Log.v(TAG,matcher.groupCount());
        }
        return null;
    }

    public static void balancedParenthesesCheck(String parseVal) throws ParseException {
        parseVal = parseVal.replaceAll(" ","");
        int parentheses = 0;
        for(char c : parseVal.toCharArray()){
            if(c == '('){
                parentheses--;
            }
            if(c == ')'){
                if(parentheses == 0){
                    throw new ParseException("Imbalanced parentheses.");
                }
                parentheses++;
            }
        }
        if(parentheses != 0){
            throw new ParseException("Imbalanced parentheses.");
        }
    }


}
