package com.resnik.util.math.symbo.logic.parse;

import com.resnik.util.math.symbo.algebra.parse.intermediate.SymbolicIntermediateOperation;
import com.resnik.util.math.symbo.logic.operations.LogicalOperation;
import com.resnik.util.math.symbo.logic.parse.intermediate.*;
import com.resnik.util.objects.RangeUtils;
import com.resnik.util.text.SyntaxAnalyzer;
import com.resnik.util.text.SyntaxException;
import com.resnik.util.text.Token;

import java.util.*;

public class LogicalSyntaxAnalyzer extends SyntaxAnalyzer<LogicalTokenizer, LogicalTokenType, LogicalOperation> {


    public LogicalSyntaxAnalyzer() {
        super(new LogicalTokenizer(), Collections.EMPTY_MAP);
    }

    public LogicalSyntaxAnalyzer(Map<String, LogicalTokenType> repMap) {
        super(new LogicalTokenizer(), repMap);
    }

    public LogicalSyntaxAnalyzer(LogicalTokenizer tokenizer, Map<String, LogicalTokenType> repMap) {
        super(tokenizer, repMap);
    }

    @Override
    public LogicalOperation analyze(List<Token<LogicalTokenType>> inputList) throws SyntaxException {
        justifySyntax(inputList);
        LogicalIntermediateOperation intermediateOperation = genIntermediateOperation(inputList);
        if(intermediateOperation != null){
            LogicalOperation operation = intermediateOperation.compile();
            return operation;
        }
        return null;
    }

    private static LogicalIntermediateOperation genIntermediateOperation(List<Token<LogicalTokenType>> inputList){
        List<LogicalTokenSet> tokenSets = genTokenSets(inputList);
        List<LogicalIntermediateOperation> logicalIntermediateOperations = genIntermediateOperations(tokenSets);
        List<LogicalIntermediateOperation> operators = generateIntermediateOperators(logicalIntermediateOperations, tokenSets, inputList);
        if(operators.size() == 1){
            return operators.get(0);
        }
        return null;
    }

    private static List<LogicalIntermediateOperation> genIntermediateOperations(List<LogicalTokenSet> curr){
        List<LogicalIntermediateOperation> retList = new ArrayList<>();
        for(LogicalTokenSet symbolicTokenSet : curr){
            switch (symbolicTokenSet.type){
                case BOOLEAN:
                    retList.add(new LogicalIntermediateValue(symbolicTokenSet.startIndex, symbolicTokenSet.endIndex, symbolicTokenSet.tokens.get(0)));
                    break;
                case VARIABLE:
                    retList.add(new LogicalIntermediateVariable(symbolicTokenSet.startIndex, symbolicTokenSet.endIndex, symbolicTokenSet.tokens));
                    break;
                case PARENTHESES:
                    retList.add(new LogicalIntermediateParentheses(symbolicTokenSet.startIndex, symbolicTokenSet.endIndex, genIntermediateOperation(symbolicTokenSet.tokens)));
                    break;
                default:
            }
        }
        return retList;
    }

    private static List<LogicalIntermediateOperation> generateIntermediateOperators(List<LogicalIntermediateOperation> curr, List<LogicalTokenSet> tokenSets, List<Token<LogicalTokenType>> inputList){
        List<LogicalIntermediateOperation> nots = generateNot(curr, tokenSets, inputList);
        List<LogicalIntermediateOperation> nands = generateNand(nots, tokenSets, inputList);
        List<LogicalIntermediateOperation> ands = generateAnd(nands, tokenSets, inputList);
        List<LogicalIntermediateOperation> xors = generateXor(ands, tokenSets, inputList);
        List<LogicalIntermediateOperation> ors = generateOr(xors, tokenSets, inputList);
        List<LogicalIntermediateOperation> implies = generateImplies(ors, tokenSets, inputList);;
        return implies;
    }


    private static List<LogicalIntermediateOperation> generateNot(List<LogicalIntermediateOperation> curr, List<LogicalTokenSet> tokenSets, List<Token<LogicalTokenType>> inputList){
        List<LogicalIntermediateOperation> clone = new ArrayList<>(curr);
        List<LogicalIntermediateOperation> retList = new ArrayList<>();
        for (int i = 0; i < inputList.size(); i++) {
            Token<LogicalTokenType> token = inputList.get(i);
            if(indexProcessedOperation(i, clone)){
                continue;
            }
            if(token.type != LogicalTokenType.OPERATOR){
                continue;
            }
            if(token.rep.equals(LogicalTokenizer.NOT)){
                LogicalIntermediateOperation right = right(i, clone);
                if(right == null){
                    throw new SyntaxException("NEGATION requires a right element.");
                }
                clone.remove(right);
                clone.add(new LogicalIntermediateNegation(i, right.endIndex, right));
            }
        }
        retList = new ArrayList<>(clone);
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        return retList;
    }

    private static List<LogicalIntermediateOperation> generateNand(List<LogicalIntermediateOperation> curr, List<LogicalTokenSet> tokenSets, List<Token<LogicalTokenType>> inputList){
        List<LogicalIntermediateOperation> clone = new ArrayList<>(curr);
        List<LogicalIntermediateOperation> retList = new ArrayList<>();
        for (int i = 0; i < inputList.size(); i++) {
            Token<LogicalTokenType> token = inputList.get(i);
            if(indexProcessedOperation(i, clone)){
                continue;
            }
            if(token.type != LogicalTokenType.OPERATOR){
                continue;
            }
            if(token.rep.equals(LogicalTokenizer.NAND)){
                LogicalIntermediateOperation left = left(i, clone);
                LogicalIntermediateOperation right = right(i, clone);
                if(left == null || right == null){
                    throw new SyntaxException("NAND requires both left and right elements.");
                }
                clone.remove(left);
                clone.remove(right);
                clone.add(new LogicalIntermediateNand(left.startIndex, right.endIndex, left, right));
            }
        }
        retList = new ArrayList<>(clone);
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        return retList;
    }

    private static List<LogicalIntermediateOperation> generateAnd(List<LogicalIntermediateOperation> curr, List<LogicalTokenSet> tokenSets, List<Token<LogicalTokenType>> inputList){
        List<LogicalIntermediateOperation> clone = new ArrayList<>(curr);
        List<LogicalIntermediateOperation> retList = new ArrayList<>();
        for (int i = 0; i < inputList.size(); i++) {
            Token<LogicalTokenType> token = inputList.get(i);
            if(indexProcessedOperation(i, clone)){
                continue;
            }
            if(token.type != LogicalTokenType.OPERATOR){
                continue;
            }
            if(token.rep.equals(LogicalTokenizer.AND)){
                LogicalIntermediateOperation left = left(i, clone);
                LogicalIntermediateOperation right = right(i, clone);
                if(left == null || right == null){
                    throw new SyntaxException("AND requires both left and right elements.");
                }
                clone.remove(left);
                clone.remove(right);
                clone.add(new LogicalIntermediateAnd(left.startIndex, right.endIndex, left, right));
            }
        }
        retList = new ArrayList<>(clone);
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        return retList;
    }

    private static List<LogicalIntermediateOperation> generateXor(List<LogicalIntermediateOperation> curr, List<LogicalTokenSet> tokenSets, List<Token<LogicalTokenType>> inputList){
        List<LogicalIntermediateOperation> clone = new ArrayList<>(curr);
        List<LogicalIntermediateOperation> retList = new ArrayList<>();
        for (int i = 0; i < inputList.size(); i++) {
            Token<LogicalTokenType> token = inputList.get(i);
            if(indexProcessedOperation(i, clone)){
                continue;
            }
            if(token.type != LogicalTokenType.OPERATOR){
                continue;
            }
            if(token.rep.equals(LogicalTokenizer.XOR)){
                LogicalIntermediateOperation left = left(i, clone);
                LogicalIntermediateOperation right = right(i, clone);
                if(left == null || right == null){
                    throw new SyntaxException("XOR requires both left and right elements.");
                }
                clone.remove(left);
                clone.remove(right);
                clone.add(new LogicalIntermediateXor(left.startIndex, right.endIndex, left, right));
            }
        }
        retList = new ArrayList<>(clone);
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        return retList;
    }

    private static List<LogicalIntermediateOperation> generateOr(List<LogicalIntermediateOperation> curr, List<LogicalTokenSet> tokenSets, List<Token<LogicalTokenType>> inputList){
        List<LogicalIntermediateOperation> clone = new ArrayList<>(curr);
        List<LogicalIntermediateOperation> retList = new ArrayList<>();
        for (int i = 0; i < inputList.size(); i++) {
            Token<LogicalTokenType> token = inputList.get(i);
            if(indexProcessedOperation(i, clone)){
                continue;
            }
            if(token.type != LogicalTokenType.OPERATOR){
                continue;
            }
            if(token.rep.equals(LogicalTokenizer.OR)){
                LogicalIntermediateOperation left = left(i, clone);
                LogicalIntermediateOperation right = right(i, clone);
                if(left == null || right == null){
                    throw new SyntaxException("OR requires both left and right elements.");
                }
                clone.remove(left);
                clone.remove(right);
                clone.add(new LogicalIntermediateOr(left.startIndex, right.endIndex, left, right));
            }
        }
        retList = new ArrayList<>(clone);
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        return retList;
    }

    private static List<LogicalIntermediateOperation> generateImplies(List<LogicalIntermediateOperation> curr, List<LogicalTokenSet> tokenSets, List<Token<LogicalTokenType>> inputList){
        List<LogicalIntermediateOperation> clone = new ArrayList<>(curr);
        List<LogicalIntermediateOperation> retList = new ArrayList<>();
        for (int i = 0; i < inputList.size(); i++) {
            Token<LogicalTokenType> token = inputList.get(i);
            if(indexProcessedOperation(i, clone)){
                continue;
            }
            if(token.type != LogicalTokenType.OPERATOR){
                continue;
            }
            if(token.rep.equals(LogicalTokenizer.IMPLIES)){
                LogicalIntermediateOperation left = left(i, clone);
                LogicalIntermediateOperation right = right(i, clone);
                if(left == null || right == null){
                    throw new SyntaxException("IMPLIES requires both left and right elements.");
                }
                clone.remove(left);
                clone.remove(right);
                clone.add(new LogicalIntermediateImplies(left.startIndex, right.endIndex, left, right));
            }
        }
        retList = new ArrayList<>(clone);
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        return retList;
    }

    private static List<LogicalTokenSet> genTokenSets(List<Token<LogicalTokenType>> inputList){
        List<LogicalTokenSet> parenthesesSets = genParenthesesSets(inputList);
        List<LogicalTokenSet> variableSets = genVariableSets(parenthesesSets, inputList);
        List<LogicalTokenSet> valueSets = genValueSets(variableSets, inputList);
        return valueSets;
    }

    public static List<LogicalTokenSet> genParenthesesSets(List<Token<LogicalTokenType>> inputList){
        List<LogicalTokenSet> retList = new ArrayList<>();
        List<Token<LogicalTokenType>> inner = new ArrayList<>();
        int balance = 0;
        int startIndex = -1;
        for (int i = 0; i < inputList.size(); i++) {
            Token<LogicalTokenType> token = inputList.get(i);
            if(token.type == LogicalTokenType.OPEN_PARENTHESES){
                balance--;
            }else if(token.type == LogicalTokenType.CLOSED_PARENTHESES){
                balance++;
            }
            if(balance == -1 && token.type == LogicalTokenType.OPEN_PARENTHESES){
                startIndex = i;
            }
            if(balance == 0 && token.type == LogicalTokenType.CLOSED_PARENTHESES){
                for(int j = startIndex + 1; j < i; j++){
                    inner.add(inputList.get(j));
                }
                retList.add(new LogicalTokenSet(startIndex, i, LogicalTokenSetType.PARENTHESES,  inner));
                startIndex = -1;
                inner = new ArrayList<>();
            }
        }
        return retList;
    }

    public static List<LogicalTokenSet> genVariableSets(List<LogicalTokenSet> curr, List<Token<LogicalTokenType>> inputList){
        List<LogicalTokenSet> retList = new ArrayList<>(curr);
        for(int i = 0; i < inputList.size(); i++){
            if(indexProcessedToken(i, curr)){
                continue;
            }
            Token<LogicalTokenType> token = inputList.get(i);
            if(token.type != LogicalTokenType.VARIABLE){
                continue;
            }
            LogicalTokenSet variable = new LogicalTokenSet(i,i,LogicalTokenSetType.VARIABLE, Collections.singletonList(token));
            variable.rep = token.rep;
            retList.add(variable);
        }
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        return retList;
    }

    public static List<LogicalTokenSet> genValueSets(List<LogicalTokenSet> curr, List<Token<LogicalTokenType>> inputList){
        List<LogicalTokenSet> retList = new ArrayList<>(curr);
        for(int i = 0; i < inputList.size(); i++){
            if(indexProcessedToken(i, curr)){
                continue;
            }
            Token<LogicalTokenType> token = inputList.get(i);
            if(token.type != LogicalTokenType.VALUE){
                continue;
            }
            LogicalTokenSet value = new LogicalTokenSet(i,i,LogicalTokenSetType.BOOLEAN, Collections.singletonList(token));
            value.rep = token.rep;
            retList.add(value);
        }
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        return retList;
    }

    public static LogicalIntermediateOperation left(int i, List<LogicalIntermediateOperation> tokenList){
        for(LogicalIntermediateOperation tokenSet : tokenList){
            if(RangeUtils.inRange(i - 1, tokenSet.startIndex, tokenSet.endIndex)){
                return tokenSet;
            }
        }
        return null;
    }

    public static LogicalIntermediateOperation right(int i, List<LogicalIntermediateOperation> tokenList){
        for(LogicalIntermediateOperation tokenSet : tokenList){
            if(RangeUtils.inRange(i + 1, tokenSet.startIndex, tokenSet.endIndex)){
                return tokenSet;
            }
        }
        return null;
    }

    public static boolean indexProcessedOperation(int i, List<LogicalIntermediateOperation> opList){
        for(LogicalIntermediateOperation t : opList){
            if(RangeUtils.inRange(i, t.startIndex, t.endIndex)){
                return true;
            }
        }
        return false;
    }


    public static boolean indexProcessedToken(int i, List<LogicalTokenSet> tokenList){
        for(LogicalTokenSet t : tokenList){
            if(RangeUtils.inRange(i, t.startIndex, t.endIndex)){
                return true;
            }
        }
        return false;
    }


    @Override
    public void justifySyntax(List<Token<LogicalTokenType>> inputList) throws SyntaxException {

    }

    public static void main(String[] args) {
        LogicalSyntaxAnalyzer analyzer = new LogicalSyntaxAnalyzer();
        String testString = "(trueorfalse  abc )!&!&&&&-->==>ANDNANDXORoror";
        analyzer.analyze(testString);
    }
}
