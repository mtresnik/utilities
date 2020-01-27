package com.resnik.util.math.symbo.parse;

import com.resnik.util.math.symbo.operations.Constant;
import com.resnik.util.math.symbo.operations.Operation;
import com.resnik.util.math.symbo.operations.Variable;
import com.resnik.util.math.symbo.parse.intermediate.*;
import com.resnik.util.objects.RangeUtils;
import com.resnik.util.text.SyntaxAnalyzer;
import com.resnik.util.text.SyntaxException;
import com.resnik.util.text.Token;

import java.util.*;

public class SymbolicSyntaxAnalyzer extends SyntaxAnalyzer<SymbolicTokenizer, SymbolicTokenType, Operation> {

    public SymbolicSyntaxAnalyzer(){
        this(SymbolicTokenType.DEFAULT_FUNCTIONS);
    }

    public SymbolicSyntaxAnalyzer(Map<String, SymbolicTokenType> repMap){
        this(new SymbolicTokenizer(), repMap);
    }

    public SymbolicSyntaxAnalyzer(SymbolicTokenizer tokenizer, Map<String, SymbolicTokenType> repMap) {
        super(tokenizer, repMap);
    }

    @Override
    public Operation analyze(List<Token<SymbolicTokenType>> inputList) throws SyntaxException{
        justifySyntax(inputList);
        IntermediateOperation intermediateOperation = genIntermediateOperation(inputList);
        if(intermediateOperation != null){
            return intermediateOperation.compile().substitute(Variable.I, Constant.I);
        }
        return null;
    }

    public static List<SymbolicTokenSet> genTokenSets(List<Token<SymbolicTokenType>> inputList){
        List<SymbolicTokenSet> parenthesesSets = genParenthesesSets(inputList);
        List<SymbolicTokenSet> functionSets = genFunctionSets(parenthesesSets, inputList);
        List<SymbolicTokenSet> variableSets = genVariableSets(functionSets, inputList);
        List<SymbolicTokenSet> numberSets = genNumberSets(variableSets, inputList);
        return numberSets;
    }

    public static List<SymbolicTokenSet> genParenthesesSets(List<Token<SymbolicTokenType>> inputList){
        List<SymbolicTokenSet> retList = new ArrayList<>();
        List<Token<SymbolicTokenType>> inner = new ArrayList<>();
        int balance = 0;
        int startIndex = -1;
        for (int i = 0; i < inputList.size(); i++) {
            Token<SymbolicTokenType> token = inputList.get(i);
            if(token.type == SymbolicTokenType.OPEN_PARENTHESES){
                balance--;
            }else if(token.type == SymbolicTokenType.CLOSED_PARENTHESES){
                balance++;
            }
            if(balance == -1 && token.type == SymbolicTokenType.OPEN_PARENTHESES){
                startIndex = i;
            }
            if(balance == 0 && token.type == SymbolicTokenType.CLOSED_PARENTHESES){
                for(int j = startIndex + 1; j < i; j++){
                    inner.add(inputList.get(j));
                }
                retList.add(new SymbolicTokenSet(startIndex, i, SymbolicTokenSetType.PARENTHESES,  inner));
                startIndex = -1;
                inner = new ArrayList<>();
            }
        }
        return retList;
    }

    public static List<SymbolicTokenSet> genFunctionSets(List<SymbolicTokenSet> curr, List<Token<SymbolicTokenType>> inputList){
        List<SymbolicTokenSet> clone = new ArrayList<>(curr);
        List<SymbolicTokenSet> retList = new ArrayList<>(curr);
        for(int i = 0; i < inputList.size(); i++){
            if(indexProcessedToken(i, curr)){
                continue;
            }
            Token<SymbolicTokenType> token = inputList.get(i);
            if(token.type != SymbolicTokenType.FUNCTION){
                continue;
            }
            SymbolicTokenSet found = null;
            int expectedIndex = i + 1;
            for(SymbolicTokenSet tokenSet : clone){
                if(tokenSet.type != SymbolicTokenSetType.PARENTHESES){
                    continue;
                }
                if(tokenSet.startIndex == expectedIndex){
                    found = tokenSet;
                    break;
                }
            }
            if(found == null){
                throw new SyntaxException("Missing argument(s) for function:" + token.rep);
            }
            clone.remove(found);
            retList.remove(found);
            SymbolicTokenSet function = new SymbolicTokenSet(i, found.endIndex, SymbolicTokenSetType.FUNCTION, found.tokens);
            function.rep = token.rep;
            retList.add(function);
        }
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        return retList;
    }

    public static List<SymbolicTokenSet> genVariableSets(List<SymbolicTokenSet> curr, List<Token<SymbolicTokenType>> inputList){
        List<SymbolicTokenSet> retList = new ArrayList<>(curr);
        for(int i = 0; i < inputList.size(); i++){
            if(indexProcessedToken(i, curr)){
                continue;
            }
            Token<SymbolicTokenType> token = inputList.get(i);
            if(token.type != SymbolicTokenType.VARIABLE){
                continue;
            }
            SymbolicTokenSet variable = new SymbolicTokenSet(i,i,SymbolicTokenSetType.VARIABLE,Collections.singletonList(token));
            variable.rep = token.rep;
            retList.add(variable);
        }
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        return retList;
    }

    public static List<SymbolicTokenSet> genNumberSets(List<SymbolicTokenSet> curr, List<Token<SymbolicTokenType>> inputList){
        List<SymbolicTokenSet> retList = new ArrayList<>(curr);
        for(int i = 0; i < inputList.size(); i++){
            if(indexProcessedToken(i, curr)){
                continue;
            }
            Token<SymbolicTokenType> token = inputList.get(i);
            if(token.type != SymbolicTokenType.NUMBER){
                continue;
            }
            SymbolicTokenSet number = new SymbolicTokenSet(i,i,SymbolicTokenSetType.NUMBER,Collections.singletonList(token));
            number.rep = token.rep;
            retList.add(number);
        }
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        return retList;
    }

    public static IntermediateOperation genIntermediateOperation(List<Token<SymbolicTokenType>> inputList){
        List<SymbolicTokenSet> tokenSets = genTokenSets(inputList);
        List<IntermediateOperation> intermediateOperations = genIntermediateOperations(tokenSets);
        List<IntermediateOperation> operators = generateIntermediateOperators(intermediateOperations, tokenSets, inputList);

        if(operators.size() == 1){
            return operators.get(0);
        }
        return null;
    }

    public static List<IntermediateOperation> genIntermediateOperations(List<SymbolicTokenSet> curr){
        List<IntermediateOperation> retList = new ArrayList<>();
        for(SymbolicTokenSet symbolicTokenSet : curr){
            switch (symbolicTokenSet.type){
                case NUMBER:
                    retList.add(new IntermediateNumber(symbolicTokenSet.startIndex, symbolicTokenSet.endIndex, symbolicTokenSet.tokens.get(0)));
                    break;
                case VARIABLE:
                    retList.add(new IntermediateVariable(symbolicTokenSet.startIndex, symbolicTokenSet.endIndex, symbolicTokenSet.tokens.get(0)));
                    break;
                case PARENTHESES:
                    retList.add(new IntermediateParentheses(symbolicTokenSet.startIndex, symbolicTokenSet.endIndex, genIntermediateOperation(symbolicTokenSet.tokens)));
                    break;
                case FUNCTION:
                    retList.add(new IntermediateFunction(symbolicTokenSet.startIndex, symbolicTokenSet.endIndex, genIntermediateOperation(symbolicTokenSet.tokens), symbolicTokenSet.rep));
                    break;
                default:
            }
        }
        return retList;
    }

    public static List<IntermediateOperation> generateIntermediateOperators(List<IntermediateOperation> curr, List<SymbolicTokenSet> tokenSets, List<Token<SymbolicTokenType>> inputList){
        List<IntermediateOperation> ident = generateIdentities(curr, tokenSets, inputList);
        List<IntermediateOperation> powers = generatePowerOperators(ident, tokenSets, inputList);
        List<IntermediateOperation> mulDiv = generateMultDivOperators(powers, tokenSets, inputList);
        List<IntermediateOperation> addSub = generateAddSub(mulDiv, tokenSets, inputList);
        return addSub;
    }

    public static List<IntermediateOperation> generateIdentities(List<IntermediateOperation> curr, List<SymbolicTokenSet> tokenSets, List<Token<SymbolicTokenType>> inputList){
        List<IntermediateOperation> clone = new ArrayList<>(curr);
        List<IntermediateOperation> retList = new ArrayList<>();
        for (int i = 0; i < inputList.size(); i++) {
            Token<SymbolicTokenType> token = inputList.get(i);
            if(indexProcessedOperation(i, clone)){
                continue;
            }
            if(token.type != SymbolicTokenType.OPERATOR){
                continue;
            }
            if(token.rep.equals(SymbolicTokenizer.PLUS)){
                IntermediateOperation left = left(i, clone);
                IntermediateOperation right = right(i, clone);
                if(right == null){
                    throw new SyntaxException("PLUS requires a right element.");
                }
                if(left == null){
                    clone.remove(right);
                    clone.add(new IntermediateIdentity(i, right.endIndex, right));
                }
            }else if(token.rep.equals(SymbolicTokenizer.MINUS)){
                IntermediateOperation left = left(i, clone);
                IntermediateOperation right = right(i, clone);
                if(right == null){
                    throw new SyntaxException("SUB requires a right element.");
                }
                if(left == null){
                    clone.remove(right);
                    clone.add(new IntermediateNegation(i, right.endIndex, right));
                }
            }
        }
        retList = new ArrayList<>(clone);
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        return retList;
    }

    public static List<IntermediateOperation> generatePowerOperators(List<IntermediateOperation> curr, List<SymbolicTokenSet> tokenSets, List<Token<SymbolicTokenType>> inputList){
        List<IntermediateOperation> clone = new ArrayList<>(curr);
        List<IntermediateOperation> retList = new ArrayList<>();
        for (int i = 0; i < inputList.size(); i++) {
            Token<SymbolicTokenType> token = inputList.get(i);
            if(indexProcessedOperation(i, clone)){
                continue;
            }
            if(token.type != SymbolicTokenType.OPERATOR){
                continue;
            }
            if(token.rep.equals(SymbolicTokenizer.POW)){
                IntermediateOperation left = left(i, clone);
                IntermediateOperation right = right(i, clone);
                if(left == null || right == null){
                    throw new SyntaxException("POW requires both left and right elements.");
                }
                clone.remove(left);
                clone.remove(right);
                clone.add(new IntermediatePower(left.startIndex, right.endIndex, left, right));
            }
        }
        retList = new ArrayList<>(clone);
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        return retList;
    }

    public static List<IntermediateOperation> generateMultDivOperators(List<IntermediateOperation> curr, List<SymbolicTokenSet> tokenSets, List<Token<SymbolicTokenType>> inputList){
        List<IntermediateOperation> clone = new ArrayList<>(curr);
        List<IntermediateOperation> retList = new ArrayList<>();
        for (int i = 0; i < inputList.size(); i++) {
            Token<SymbolicTokenType> token = inputList.get(i);
            if(indexProcessedOperation(i, clone)){
                continue;
            }
            if(token.type != SymbolicTokenType.OPERATOR){
                continue;
            }
            if(token.rep.equals(SymbolicTokenizer.MULT)){
                IntermediateOperation left = left(i, clone);
                IntermediateOperation right = right(i, clone);
                if(left == null || right == null){
                    throw new SyntaxException("MULT requires both left and right elements.");
                }
                clone.remove(left);
                clone.remove(right);
                clone.add(new IntermediateMultiplication(left.startIndex, right.endIndex, left, right));
            }else if(token.rep.equals(SymbolicTokenizer.DIVIDE)){
                IntermediateOperation left = left(i, clone);
                IntermediateOperation right = right(i, clone);
                if(left == null || right == null){
                    throw new SyntaxException("DIV requires both left and right elements.");
                }
                clone.remove(left);
                clone.remove(right);
                clone.add(new IntermediateDivision(left.startIndex, right.endIndex, left, right));
            }
        }
        retList = new ArrayList<>(clone);
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        return retList;
    }

    public static List<IntermediateOperation> generateAddSub(List<IntermediateOperation> curr, List<SymbolicTokenSet> tokenSets, List<Token<SymbolicTokenType>> inputList){
        List<IntermediateOperation> clone = new ArrayList<>(curr);
        List<IntermediateOperation> retList = new ArrayList<>();
        for (int i = 0; i < inputList.size(); i++) {
            Token<SymbolicTokenType> token = inputList.get(i);
            if(indexProcessedOperation(i, clone)){
                continue;
            }
            if(token.type != SymbolicTokenType.OPERATOR){
                continue;
            }
            if(token.rep.equals(SymbolicTokenizer.PLUS)){
                IntermediateOperation left = left(i, clone);
                IntermediateOperation right = right(i, clone);
                if(right == null){
                    throw new SyntaxException("PLUS requires a right element.");
                }
                if(left != null){
                    clone.remove(left);
                    clone.remove(right);
                    clone.add(new IntermediateAddition(left.startIndex, right.endIndex, left, right));
                }
            }else if(token.rep.equals(SymbolicTokenizer.MINUS)){
                IntermediateOperation left = left(i, clone);
                IntermediateOperation right = right(i, clone);
                if(right == null){
                    throw new SyntaxException("SUB requires a right element.");
                }
                if(left != null){
                    clone.remove(left);
                    clone.remove(right);
                    clone.add(new IntermediateSubtraction(left.startIndex, right.endIndex, left, right));
                }
            }
        }
        retList = new ArrayList<>(clone);
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        return retList;
    }

    public static IntermediateOperation left(int i, List<IntermediateOperation> tokenList){
        for(IntermediateOperation tokenSet : tokenList){
            if(RangeUtils.inRange(i - 1, tokenSet.startIndex, tokenSet.endIndex)){
                return tokenSet;
            }
        }
        return null;
    }

    public static IntermediateOperation right(int i, List<IntermediateOperation> tokenList){
        for(IntermediateOperation tokenSet : tokenList){
            if(RangeUtils.inRange(i + 1, tokenSet.startIndex, tokenSet.endIndex)){
                return tokenSet;
            }
        }
        return null;
    }

    public static boolean indexProcessedOperation(int i, List<IntermediateOperation> opList){
        for(IntermediateOperation t : opList){
            if(RangeUtils.inRange(i, t.startIndex, t.endIndex)){
                return true;
            }
        }
        return false;
    }

    public static boolean indexProcessedToken(int i, List<SymbolicTokenSet> tokenList){
        for(SymbolicTokenSet t : tokenList){
            if(RangeUtils.inRange(i, t.startIndex, t.endIndex)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void justifySyntax(List<Token<SymbolicTokenType>> inputList) throws SyntaxException {
        justifySigns(inputList);
    }

    public void justifySigns(List<Token<SymbolicTokenType>> inputList) throws SyntaxException {
        for (int i = 0; i < inputList.size() - 1; i++) {
            Token<SymbolicTokenType> curr = inputList.get(i);
            if(curr.type != SymbolicTokenType.OPERATOR){
                continue;
            }
            if(curr.rep.equals(SymbolicTokenizer.MINUS) || curr.rep.equals(SymbolicTokenizer.PLUS)){
                Token<SymbolicTokenType> next = inputList.get(i + 1);
                String message = "Invalid syntax at:" + curr.toString() + "\t" + next.toString();
                if(next.type == SymbolicTokenType.CLOSED_PARENTHESES){
                    throw new SyntaxException(message);
                }
                if(next.type == SymbolicTokenType.OPERATOR){
                    throw new SyntaxException(message);
                }
            }
        }
    }

}
