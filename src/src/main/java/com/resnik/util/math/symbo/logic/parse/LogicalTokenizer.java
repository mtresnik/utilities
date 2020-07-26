package com.resnik.util.math.symbo.logic.parse;

import com.resnik.util.math.symbo.algebra.parse.SymbolicTokenType;
import com.resnik.util.objects.RangeUtils;
import com.resnik.util.text.Token;
import com.resnik.util.text.TokenizationException;
import com.resnik.util.text.Tokenizer;

import java.util.*;

public class LogicalTokenizer implements Tokenizer<LogicalTokenType> {

    public static final String TRUE = "true";
    public static final String FALSE = "false";

    public static final String OR = "+";
    public static final String OR0 = "|";
    public static final String OR1 = "or";
    public static final String OR2 = "OR";
    public static final String OR_DOUBLE = "||";
    public static final String OR_OPS = OR + OR0 + OR1 + OR2 + OR_DOUBLE;
    public static final List<String> ALL_OR = new ArrayList<>(Arrays.asList(OR, OR0, OR1, OR2, OR_DOUBLE));

    public static final String AND = "*";
    public static final String AND0 = "&";
    public static final String AND1 = "and";
    public static final String AND2 = "AND";
    public static final String AND_DOUBLE = "&&";
    public static final String AND_OPS = AND + AND0 + AND1 + AND2 + AND_DOUBLE;
    public static final List<String> ALL_AND = new ArrayList<>(Arrays.asList(AND, AND0, AND1, AND2, AND_DOUBLE));

    public static final String NOT = "!";
    public static final String NOT0 = "-";
    public static final String NOT1 = "not";
    public static final String NOT2 = "NOT";
    public static final String NOT3 = "negate";
    public static final String NOT4 = "NEGATE";
    public static final String NOT_OPS = NOT + NOT0 + NOT1 + NOT2 + NOT3 + NOT4;
    public static final List<String> ALL_NOT = new ArrayList<>(Arrays.asList(NOT, NOT0, NOT1, NOT2, NOT3, NOT4));

    public static final String NAND = "!&";
    public static final String NAND0 = "nand";
    public static final String NAND1 = "NAND";
    public static final String NAND_OPS = NAND + NAND0 + NAND1;
    public static final List<String> ALL_NAND = new ArrayList<>(Arrays.asList(NAND, NAND0, NAND1));

    public static final String XOR = "xor";
    public static final String XOR0 = "XOR";
    public static final String XOR1 = "@";
    public static final String XOR_OPS = XOR + XOR0 + XOR1;
    public static final List<String> ALL_XOR = new ArrayList<>(Arrays.asList(XOR, XOR0, XOR1));

    public static final String IMPLIES = "->";
    public static final String IMPLIES0 = "=>";
    public static final String IMPLIES1 = "-->";
    public static final String IMPLIES2 = "==>";
    public static final String IMPLIES3 = "implies";
    public static final String IMPLIES4 = "IMPLIES";
    public static final String IMPLIES_OPS = IMPLIES + IMPLIES0 + IMPLIES1 + IMPLIES2 + IMPLIES3 + IMPLIES4;
    public static final List<String> ALL_IMPLIES = new ArrayList<>(Arrays.asList(IMPLIES, IMPLIES0, IMPLIES1, IMPLIES2, IMPLIES3, IMPLIES4));

    public static final String OPERATORS = OR_OPS + AND_OPS + NOT_OPS + NAND_OPS + XOR_OPS + IMPLIES_OPS;
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

    public static boolean indexProcessed(int i, List<Token<LogicalTokenType>> tokenList){
        for(Token t : tokenList){
            if(RangeUtils.inRange(i, t.startIndex, t.endIndex)){
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Token<LogicalTokenType>> tokenize(String inputString, Map<String, LogicalTokenType> preMap) {
        inputString = preProcess(inputString);
        List<Token<LogicalTokenType>> values = tokenizeValues(inputString);
        List<Token<LogicalTokenType>> operators = tokenizeOperators(values, inputString);
        List<Token<LogicalTokenType>> parentheses = tokenizeParentheses(operators, inputString);
        List<Token<LogicalTokenType>> text = tokenizeText(parentheses, inputString);
        List<Token<LogicalTokenType>> variables = tokenizeVariables(text, inputString);
        return postProcess(variables);
    }

    public static List<Token<LogicalTokenType>> postProcess(List<Token<LogicalTokenType>> inputList){
        List<Token<LogicalTokenType>> currList = justifyAnd(inputList);
        return currList;
    }

    public static List<Token<LogicalTokenType>> justifyAnd(List<Token<LogicalTokenType>> inputList){
        List<Token<LogicalTokenType>> retList = new ArrayList<>();
        for (int i = 0; i < inputList.size(); i++) {
            Token<LogicalTokenType> curr = inputList.get(i);
            Token<LogicalTokenType> next = (i < inputList.size() - 1 ? inputList.get(i + 1) : null);
            retList.add(curr);
            if((curr.type == LogicalTokenType.VALUE || curr.type == LogicalTokenType.VARIABLE || curr.type == LogicalTokenType.CLOSED_PARENTHESES)
                    && next != null && (next.type != LogicalTokenType.OPERATOR || (next.rep.equals(NOT))) && next.type != LogicalTokenType.CLOSED_PARENTHESES){
                retList.add(Token.nullIndex(LogicalTokenType.OPERATOR, AND));
            }
        }
        return retList;
    }


    private static List<Integer> getIndices(String inputString, String keyword){
        List<Integer> retList = new ArrayList<>();
        int index = inputString.indexOf(keyword);
        while (index >=0){
            retList.add(index);
            index = inputString.indexOf(keyword, index+keyword.length());
        }
        return retList;
    }

    private static List<Token<LogicalTokenType>> tokenizeValues(String inputString){
        List<Token<LogicalTokenType>> retList = new ArrayList<>();
        List<Integer> trueIndices = getIndices(inputString.toLowerCase(), TRUE);
        List<Integer> falseIndices = getIndices(inputString.toLowerCase(), FALSE);
        for(Integer start : trueIndices){
            int end = start + TRUE.length() - 1;
            Token<LogicalTokenType> number = new Token<>(start, end, LogicalTokenType.VALUE, TRUE);
            retList.add(number);
        }
        for(Integer start : falseIndices){
            int end = start + FALSE.length() - 1;
            Token<LogicalTokenType> number = new Token<>(start, end, LogicalTokenType.VALUE, FALSE);
            retList.add(number);
        }
        Collections.sort(retList, Comparator.comparingInt(a -> a.startIndex));
        return retList;
    }


    private static List<Token<LogicalTokenType>> tokenizeOperators(List<Token<LogicalTokenType>> values, String inputString){
        // NAND
        // IMPLIES
        // NOT
        // AND
        // XOR
        // OR
        List<Token<LogicalTokenType>> nandList = tokenizeNand(values, inputString);
        List<Token<LogicalTokenType>> impList = tokenizeImplies(nandList, inputString);
        List<Token<LogicalTokenType>> notList = tokenizeNot(impList, inputString);
        List<Token<LogicalTokenType>> andList = tokenizeAnd(notList, inputString);
        List<Token<LogicalTokenType>> xorList = tokenizeXor(andList, inputString);
        List<Token<LogicalTokenType>> orList = tokenizeOr(xorList, inputString);
        Collections.sort(orList, Comparator.comparingInt(a -> a.startIndex));

        return orList;
    }

    private static List<Token<LogicalTokenType>> tokenizeXor(List<Token<LogicalTokenType>> inputList, String inputString){
        return tokenizeGeneral(inputList, inputString, ALL_XOR, XOR);
    }

    private static List<Token<LogicalTokenType>> tokenizeOr(List<Token<LogicalTokenType>> inputList, String inputString){
        return tokenizeGeneral(inputList, inputString, ALL_OR, OR);
    }

    private static List<Token<LogicalTokenType>> tokenizeNot(List<Token<LogicalTokenType>> inputList, String inputString){
        return tokenizeGeneral(inputList, inputString, ALL_NOT, NOT);
    }

    private static List<Token<LogicalTokenType>> tokenizeNand(List<Token<LogicalTokenType>> inputList, String inputString){
        return tokenizeGeneral(inputList, inputString, ALL_NAND, NAND);
    }

    private static List<Token<LogicalTokenType>> tokenizeImplies(List<Token<LogicalTokenType>> inputList, String inputString){
        return tokenizeGeneral(inputList, inputString, ALL_IMPLIES, IMPLIES);
    }


    public static boolean intersects(int start, int end, List<Token<LogicalTokenType>> inputList){
        for(Token<LogicalTokenType> token : inputList){
            if(RangeUtils.inRange(token.startIndex, start, end) ||
                    RangeUtils.inRange(token.endIndex, start, end) ||
                    RangeUtils.inRange(start, token.startIndex, token.endIndex)||
                    RangeUtils.inRange(end, token.startIndex, token.endIndex)){
                return true;
            }
        }
        return false;
    }

    public static boolean intersects(int q, List<Token<LogicalTokenType>> inputList){
        for(Token<LogicalTokenType> token : inputList){
            if(RangeUtils.inRange(q, token.startIndex, token.endIndex)){
                return true;
            }
        }
        return false;
    }


    private static List<Token<LogicalTokenType>> tokenizeAnd(List<Token<LogicalTokenType>> inputList, String inputString){
        return tokenizeGeneral(inputList, inputString, ALL_AND, AND);
    }

    private static List<Token<LogicalTokenType>> tokenizeGeneral(List<Token<LogicalTokenType>> inputList, String inputString, Collection<String> iter, String base){
        List<Token<LogicalTokenType>> retList = new ArrayList<>(inputList);
        List<String> maxSet = new ArrayList<>(iter);
        Collections.sort(maxSet, (s, t1) -> Integer.compare(t1.length(), s.length()));
        for(String testString : maxSet){
            List<Integer> testIndices = getIndices(inputString.toLowerCase(), testString);
            for(int start : testIndices){
                int end = start + testString.length() - 1;
                if(intersects(start, retList) || intersects(end, retList)){
                    continue;
                }
                retList.add(new Token<>(start, end, LogicalTokenType.OPERATOR, base));
            }
        }
        return retList;
    }


    public List<Token<LogicalTokenType>> tokenizeParentheses(List<Token<LogicalTokenType>> tokenList, String inputString){
        List<Token<LogicalTokenType>> retList = new ArrayList<>(tokenList);
        char[] arrayRep = inputString.toCharArray();
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        for(int i = 0; i < arrayRep.length; i++){
            if(indexProcessed(i, retList)){
                continue;
            }
            if(arrayRep[i] == OPEN_PARENTHESES || arrayRep[i] == CLOSED_PARENTHESES){
                Token<LogicalTokenType> parenthesis = Token.<LogicalTokenType>singleIndex(i, (arrayRep[i] == OPEN_PARENTHESES ? LogicalTokenType.OPEN_PARENTHESES : LogicalTokenType.CLOSED_PARENTHESES), arrayRep[i]);
                retList.add(parenthesis);
            }
        }
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        return retList;
    }

    public List<Token<LogicalTokenType>> tokenizeText(List<Token<LogicalTokenType>> tokenList, String inputString){
        List<Token<LogicalTokenType>> retList = new ArrayList<>(tokenList);
        char[] arrayRep = inputString.toCharArray();
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        String accumulated = "";
        for(int i = 0; i < arrayRep.length; i++){
            if(indexProcessed(i, retList)){
                if(!accumulated.isEmpty()){
                    int start = i - accumulated.length();
                    int end = i - 1;
                    Token<LogicalTokenType> text = new Token<>(start, end, LogicalTokenType.TEXT, accumulated);
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
            Token<LogicalTokenType> text = new Token<>(start, end, LogicalTokenType.TEXT, accumulated);
            retList.add(text);
        }
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        return retList;
    }

    public List<Token<LogicalTokenType>> tokenizeVariables(List<Token<LogicalTokenType>> tokenList, String inputString){
        List<Token<LogicalTokenType>> retList = new ArrayList<>();
        for (int i = 0; i < tokenList.size(); i++) {
            Token<LogicalTokenType> curr = tokenList.get(i);
            if(curr.type != LogicalTokenType.TEXT){
                retList.add(curr);
                continue;
            }
            for(int c = 0; c < curr.rep.length(); c++){
                char currChar = curr.rep.charAt(c);
                String currCharRep = Character.toString(currChar);
                int currIndex = curr.startIndex + c;
                Token<LogicalTokenType> var = new Token<>(currIndex, currIndex, LogicalTokenType.VARIABLE, currCharRep);
                retList.add(var);
            }
        }
        return retList;
    }






}
