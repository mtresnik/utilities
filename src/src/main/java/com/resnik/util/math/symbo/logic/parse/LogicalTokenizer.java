package com.resnik.util.math.symbo.logic.parse;

import com.resnik.util.text.Token;
import com.resnik.util.text.TokenizationException;
import com.resnik.util.text.Tokenizer;

import java.util.List;
import java.util.Map;

public class LogicalTokenizer implements Tokenizer<LogicalTokenType> {

    public static final char OPEN_PARENTHESES = '(';
    public static final char CLOSED_PARENTHESES = ')';

    public static String preProcess(String inputString){
        int balance = 0;
        for(char c : inputString.toCharArray()){
            if(c == OPEN_PARENTHESES){
                balance--;
            }else if(c == CLOSED_PARENTHESES){
                balance++;
            }
        }
        if(balance != 0){
            throw new TokenizationException("Imbalanced number of parentheses.");
        }
        return inputString
                .replace(" ", "")
                .replaceAll("\\(\\)", "");
    }

    @Override
    public List<Token<LogicalTokenType>> tokenize(String inputString, Map<String, LogicalTokenType> preMap) {
        inputString = preProcess(inputString);
        return null;
    }

}
