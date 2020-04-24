package com.resnik.util.math.symbo.algebra.parse;

import com.resnik.util.objects.RangeUtils;
import com.resnik.util.text.StringUtils;
import com.resnik.util.text.Token;
import com.resnik.util.text.TokenizationException;
import com.resnik.util.text.Tokenizer;

import java.util.*;

public class SymbolicTokenizer implements Tokenizer<SymbolicTokenType> {

    public static final String ALL_NUMBERS = "0123456789";
    public static final char DECIMAL = '.';
    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String MULT = "*";
    public static final String DIVIDE = "/";
    public static final String POW = "^";
    public static final String OPERATORS = PLUS + MINUS + MULT + DIVIDE + POW;
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
    public List<Token<SymbolicTokenType>> tokenize(String inputString, Map<String, SymbolicTokenType> functions) {
        inputString = preProcess(inputString);
        List<Token<SymbolicTokenType>> numbers = tokenizeNumbers(inputString);
        List<Token<SymbolicTokenType>> operators = tokenizeOperators(numbers, inputString);
        List<Token<SymbolicTokenType>> parentheses = tokenizeParentheses(operators, inputString);
        List<Token<SymbolicTokenType>> text = tokenizeText(parentheses, inputString);
        List<Token<SymbolicTokenType>> funcs = tokenizeFunctions(text, functions);
        List<Token<SymbolicTokenType>> vars = tokenizeVariables(funcs, functions);
        return postProcess(vars);
    }

    public static List<Token<SymbolicTokenType>> postProcess(List<Token<SymbolicTokenType>> inputList){
        List<Token<SymbolicTokenType>> currList = justifyMult(inputList);
        List<Token<SymbolicTokenType>> collapsed = collapseSigns(currList);
        while (collapsed.size() != currList.size()){
            currList = collapsed;
            collapsed = collapseSigns(currList);
        }
        return currList;
    }

    public static List<Token<SymbolicTokenType>> justifyMult(List<Token<SymbolicTokenType>> inputList){
        List<Token<SymbolicTokenType>> retList = new ArrayList<>();
        for (int i = 0; i < inputList.size(); i++) {
            Token<SymbolicTokenType> curr = inputList.get(i);
            Token<SymbolicTokenType> next = (i < inputList.size() - 1 ? inputList.get(i + 1) : null);
            retList.add(curr);
            if((curr.type == SymbolicTokenType.NUMBER || curr.type == SymbolicTokenType.VARIABLE || curr.type == SymbolicTokenType.CLOSED_PARENTHESES)
                    && next != null && next.type != SymbolicTokenType.OPERATOR && next.type != SymbolicTokenType.CLOSED_PARENTHESES){
                retList.add(Token.nullIndex(SymbolicTokenType.OPERATOR, MULT));
            }
        }
        return retList;
    }

    public static List<Token<SymbolicTokenType>> collapseSigns(List<Token<SymbolicTokenType>> inputList){
        List<Token<SymbolicTokenType>> retList = new ArrayList<>();
        for (int i = 0; i < inputList.size(); i++) {
            Token<SymbolicTokenType> curr = inputList.get(i);
            Token<SymbolicTokenType> next = (i < inputList.size() - 1 ? inputList.get(i + 1) : null);
            if(curr.type == SymbolicTokenType.OPERATOR && next != null && next.type == SymbolicTokenType.OPERATOR){
                if((curr.rep.equals(PLUS) || curr.rep.equals(MINUS)) && (next.rep.equals(PLUS) || next.rep.equals(MINUS)) ){
                    if(curr.rep.equals(next.rep)){
                        retList.add(Token.nullIndex(SymbolicTokenType.OPERATOR, PLUS));
                    }else{
                        retList.add(Token.nullIndex(SymbolicTokenType.OPERATOR, MINUS));
                    }
                    i++;
                    continue;
                }
            }
            retList.add(curr);
        }
        return retList;
    }

    public List<Token<SymbolicTokenType>> tokenizeNumbers(String inputString){
        final String VALID = ALL_NUMBERS + DECIMAL;
        char[] arrayRep = inputString.toCharArray();
        List<Token<SymbolicTokenType>> retList = new ArrayList<>();
        String accumulate = "";
        for (int i = 0; i < arrayRep.length; i++) {
            if(arrayRep[i] == DECIMAL && accumulate.contains(DECIMAL+"")){
                throw new TokenizationException("Invalid number formatting.");
            }
            if(!VALID.contains(arrayRep[i]+"")){
                if(accumulate.isEmpty()){
                    continue;
                }
                int start = i - accumulate.length();
                int end = i - 1;
                Token<SymbolicTokenType> number = new Token<>(start, end, SymbolicTokenType.NUMBER, accumulate);
                retList.add(number);
                accumulate = "";
                continue;
            }
            accumulate += arrayRep[i];
        }
        if(!accumulate.isEmpty()){
            int start = arrayRep.length - accumulate.length();
            int end = arrayRep.length - 1;
            Token<SymbolicTokenType> number = new Token<>(start, end, SymbolicTokenType.NUMBER, accumulate);
            retList.add(number);
        }
        return retList;
    }

    public static boolean indexProcessed(int i, List<Token<SymbolicTokenType>> tokenList){
        for(Token t : tokenList){
            if(RangeUtils.inRange(i, t.startIndex, t.endIndex)){
                return true;
            }
        }
        return false;
    }

    public List<Token<SymbolicTokenType>> tokenizeOperators(List<Token<SymbolicTokenType>> tokenList, String inputString){
        List<Token<SymbolicTokenType>> retList = new ArrayList<>(tokenList);
        char[] arrayRep = inputString.toCharArray();
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        for(int i = 0; i < arrayRep.length; i++){
            if(indexProcessed(i, retList)){
                continue;
            }
            if(OPERATORS.contains(arrayRep[i]+"")){
                Token<SymbolicTokenType> operator = Token.singleIndex(i, SymbolicTokenType.OPERATOR, arrayRep[i]);
                retList.add(operator);
            }
        }
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        return retList;
    }

    public List<Token<SymbolicTokenType>> tokenizeParentheses(List<Token<SymbolicTokenType>> tokenList, String inputString){
        List<Token<SymbolicTokenType>> retList = new ArrayList<>(tokenList);
        char[] arrayRep = inputString.toCharArray();
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        for(int i = 0; i < arrayRep.length; i++){
            if(indexProcessed(i, retList)){
                continue;
            }
            if(arrayRep[i] == OPEN_PARENTHESES || arrayRep[i] == CLOSED_PARENTHESES){
                Token<SymbolicTokenType> parenthesis = Token.singleIndex(i, (arrayRep[i] == OPEN_PARENTHESES ? SymbolicTokenType.OPEN_PARENTHESES : SymbolicTokenType.CLOSED_PARENTHESES), arrayRep[i]);
                retList.add(parenthesis);
            }
        }
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        return retList;
    }

    public List<Token<SymbolicTokenType>> tokenizeText(List<Token<SymbolicTokenType>> tokenList, String inputString){
        List<Token<SymbolicTokenType>> retList = new ArrayList<>(tokenList);
        char[] arrayRep = inputString.toCharArray();
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        String accumulated = "";
        for(int i = 0; i < arrayRep.length; i++){
            if(indexProcessed(i, retList)){
                if(!accumulated.isEmpty()){
                    int start = i - accumulated.length();
                    int end = i - 1;
                    Token<SymbolicTokenType> text = new Token<>(start, end, SymbolicTokenType.TEXT, accumulated);
                    retList.add(text);
                }
                accumulated = "";
                continue;
            }

            accumulated += arrayRep[i];
        }
        if(!accumulated.isEmpty()){
            int start = arrayRep.length - accumulated.length();
            int end = arrayRep.length - 1;
            Token<SymbolicTokenType> text = new Token<>(start, end, SymbolicTokenType.TEXT, accumulated);
            retList.add(text);
        }
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        return retList;
    }

    public List<Token<SymbolicTokenType>> tokenizeFunctions(List<Token<SymbolicTokenType>> tokenList, Map<String, SymbolicTokenType> functions){
        List<Token<SymbolicTokenType>> retList = new ArrayList<>();
        for (int i = 0; i < tokenList.size(); i++) {
            Token<SymbolicTokenType> curr = tokenList.get(i);
            Token<SymbolicTokenType> next = (i < tokenList.size() - 1 ? tokenList.get(i + 1) : null);
            if(curr.type != SymbolicTokenType.TEXT){
                retList.add(curr);
                continue;
            }
            if(next != null && next.type == SymbolicTokenType.OPEN_PARENTHESES){
                String innerFunc = null;
                for(Map.Entry<String, SymbolicTokenType> func : functions.entrySet()){
                    if(func.getValue() != SymbolicTokenType.FUNCTION){
                        continue;
                    }
                    if(func.getKey().length() > curr.rep.length()){
                        continue;
                    }
                    if(curr.rep.endsWith(func.getKey())){
                        innerFunc = func.getKey();
                        break;
                    }
                }
                if(innerFunc != null){
                    int endIndex = curr.rep.indexOf(innerFunc);
                    if(endIndex != 0){
                        String newRep = curr.rep.substring(0, endIndex);
                        Token<SymbolicTokenType> rem = Token.nullIndex(SymbolicTokenType.TEXT, newRep);
                        retList.add(rem);
                    }
                    Token<SymbolicTokenType> function = Token.nullIndex(SymbolicTokenType.FUNCTION, innerFunc);
                    retList.add(function);
                }else{
                    Token<SymbolicTokenType> rem = curr.convert(SymbolicTokenType.TEXT);
                    retList.add(rem);
                }
            }else{
                Token<SymbolicTokenType> rem = curr.convert(SymbolicTokenType.TEXT);
                retList.add(rem);
            }

        }
        return retList;
    }

    public static List<Token<SymbolicTokenType>> maxVariables(String inputString, Map<String, SymbolicTokenType> variableMap){
        List<Token<SymbolicTokenType>> retList = new ArrayList<>();
        String max = null;
        int maxCount = -1;
        for(Map.Entry<String, SymbolicTokenType> varEntry : variableMap.entrySet()){
            if(varEntry.getValue() != SymbolicTokenType.VARIABLE){
                continue;
            }
            if(inputString.contains(varEntry.getKey()) && varEntry.getKey().length() > maxCount){
                max = varEntry.getKey();
                maxCount = max.length();
            }
        }
        String[] rem = StringUtils.findRem(inputString, max);
        if(max == null || rem.length == 0){
            retList.add(Token.nullIndex(SymbolicTokenType.VARIABLE, inputString));
            return retList;
        }
        if(rem.length == 1){
            if(inputString.indexOf(max) == 0){
                retList.add(Token.nullIndex(SymbolicTokenType.VARIABLE, max));
                List<Token<SymbolicTokenType>> RHS = maxVariables(rem[0], variableMap);
                retList.addAll(RHS);
                return retList;
            }
            List<Token<SymbolicTokenType>> LHS = maxVariables(rem[0], variableMap);
            retList.addAll(LHS);
            retList.add(Token.nullIndex(SymbolicTokenType.VARIABLE, max));
            return retList;
        }
        if(rem.length == 2){
            List<Token<SymbolicTokenType>> LHS = maxVariables(rem[0], variableMap);
            List<Token<SymbolicTokenType>> RHS = maxVariables(rem[1], variableMap);
            retList.addAll(LHS);
            retList.add(Token.nullIndex(SymbolicTokenType.VARIABLE, max));
            retList.addAll(RHS);
            return retList;
        }
        retList.add(Token.nullIndex(SymbolicTokenType.VARIABLE, inputString));
        return retList;
    }

    public List<Token<SymbolicTokenType>> tokenizeVariables(List<Token<SymbolicTokenType>> tokenList, Map<String, SymbolicTokenType> variableMap){
        List<Token<SymbolicTokenType>> retList = new ArrayList<>();
        for (int i = 0; i < tokenList.size(); i++) {
            Token<SymbolicTokenType> curr = tokenList.get(i);
            if(curr.type != SymbolicTokenType.TEXT){
                retList.add(curr);
                continue;
            }
            retList.addAll(maxVariables(curr.rep, variableMap));
        }
        return retList;
    }













}
