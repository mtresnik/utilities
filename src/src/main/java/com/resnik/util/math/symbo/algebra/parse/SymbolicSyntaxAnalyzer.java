package com.resnik.util.math.symbo.algebra.parse;

import com.resnik.util.math.symbo.algebra.operations.Constant;
import com.resnik.util.math.symbo.algebra.operations.Operation;
import com.resnik.util.math.symbo.algebra.operations.Variable;
import com.resnik.util.math.symbo.algebra.parse.intermediate.*;
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
        SymbolicIntermediateOperation symbolicIntermediateOperation = genIntermediateOperation(inputList);
        if(symbolicIntermediateOperation != null){
            return symbolicIntermediateOperation.compile().substitute(Variable.I, Constant.I);
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

    public static SymbolicIntermediateOperation genIntermediateOperation(List<Token<SymbolicTokenType>> inputList){
        List<SymbolicTokenSet> tokenSets = genTokenSets(inputList);
        List<SymbolicIntermediateOperation> symbolicIntermediateOperations = genIntermediateOperations(tokenSets);
        List<SymbolicIntermediateOperation> operators = generateIntermediateOperators(symbolicIntermediateOperations, tokenSets, inputList);

        if(operators.size() == 1){
            return operators.get(0);
        }
        return null;
    }

    public static List<SymbolicIntermediateOperation> genIntermediateOperations(List<SymbolicTokenSet> curr){
        List<SymbolicIntermediateOperation> retList = new ArrayList<>();
        for(SymbolicTokenSet symbolicTokenSet : curr){
            switch (symbolicTokenSet.type){
                case NUMBER:
                    retList.add(new SymbolicIntermediateNumber(symbolicTokenSet.startIndex, symbolicTokenSet.endIndex, symbolicTokenSet.tokens.get(0)));
                    break;
                case VARIABLE:
                    retList.add(new SymbolicIntermediateVariable(symbolicTokenSet.startIndex, symbolicTokenSet.endIndex, symbolicTokenSet.tokens.get(0)));
                    break;
                case PARENTHESES:
                    retList.add(new SymbolicIntermediateParentheses(symbolicTokenSet.startIndex, symbolicTokenSet.endIndex, genIntermediateOperation(symbolicTokenSet.tokens)));
                    break;
                case FUNCTION:
                    retList.add(new SymbolicIntermediateFunction(symbolicTokenSet.startIndex, symbolicTokenSet.endIndex, genIntermediateOperation(symbolicTokenSet.tokens), symbolicTokenSet.rep));
                    break;
                default:
            }
        }
        return retList;
    }

    public static List<SymbolicIntermediateOperation> generateIntermediateOperators(List<SymbolicIntermediateOperation> curr, List<SymbolicTokenSet> tokenSets, List<Token<SymbolicTokenType>> inputList){
        List<SymbolicIntermediateOperation> ident = generateIdentities(curr, tokenSets, inputList);
        List<SymbolicIntermediateOperation> powers = generatePowerOperators(ident, tokenSets, inputList);
        List<SymbolicIntermediateOperation> mulDiv = generateMultDivOperators(powers, tokenSets, inputList);
        List<SymbolicIntermediateOperation> addSub = generateAddSub(mulDiv, tokenSets, inputList);
        return addSub;
    }

    public static List<SymbolicIntermediateOperation> generateIdentities(List<SymbolicIntermediateOperation> curr, List<SymbolicTokenSet> tokenSets, List<Token<SymbolicTokenType>> inputList){
        List<SymbolicIntermediateOperation> clone = new ArrayList<>(curr);
        List<SymbolicIntermediateOperation> retList = new ArrayList<>();
        for (int i = 0; i < inputList.size(); i++) {
            Token<SymbolicTokenType> token = inputList.get(i);
            if(indexProcessedOperation(i, clone)){
                continue;
            }
            if(token.type != SymbolicTokenType.OPERATOR){
                continue;
            }
            if(token.rep.equals(SymbolicTokenizer.PLUS)){
                SymbolicIntermediateOperation left = left(i, clone);
                SymbolicIntermediateOperation right = right(i, clone);
                if(right == null){
                    throw new SyntaxException("PLUS requires a right element.");
                }
                if(left == null){
                    clone.remove(right);
                    clone.add(new SymbolicIntermediateIdentity(i, right.endIndex, right));
                }
            }else if(token.rep.equals(SymbolicTokenizer.MINUS)){
                SymbolicIntermediateOperation left = left(i, clone);
                SymbolicIntermediateOperation right = right(i, clone);
                if(right == null){
                    throw new SyntaxException("SUB requires a right element.");
                }
                if(left == null){
                    clone.remove(right);
                    clone.add(new SymbolicIntermediateNegation(i, right.endIndex, right));
                }
            }
        }
        retList = new ArrayList<>(clone);
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        return retList;
    }

    public static List<SymbolicIntermediateOperation> generatePowerOperators(List<SymbolicIntermediateOperation> curr, List<SymbolicTokenSet> tokenSets, List<Token<SymbolicTokenType>> inputList){
        List<SymbolicIntermediateOperation> clone = new ArrayList<>(curr);
        List<SymbolicIntermediateOperation> retList = new ArrayList<>();
        for (int i = 0; i < inputList.size(); i++) {
            Token<SymbolicTokenType> token = inputList.get(i);
            if(indexProcessedOperation(i, clone)){
                continue;
            }
            if(token.type != SymbolicTokenType.OPERATOR){
                continue;
            }
            if(token.rep.equals(SymbolicTokenizer.POW)){
                SymbolicIntermediateOperation left = left(i, clone);
                SymbolicIntermediateOperation right = right(i, clone);
                if(left == null || right == null){
                    throw new SyntaxException("POW requires both left and right elements.");
                }
                clone.remove(left);
                clone.remove(right);
                clone.add(new SymbolicIntermediatePower(left.startIndex, right.endIndex, left, right));
            }
        }
        retList = new ArrayList<>(clone);
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        return retList;
    }

    public static List<SymbolicIntermediateOperation> generateMultDivOperators(List<SymbolicIntermediateOperation> curr, List<SymbolicTokenSet> tokenSets, List<Token<SymbolicTokenType>> inputList){
        List<SymbolicIntermediateOperation> clone = new ArrayList<>(curr);
        List<SymbolicIntermediateOperation> retList = new ArrayList<>();
        for (int i = 0; i < inputList.size(); i++) {
            Token<SymbolicTokenType> token = inputList.get(i);
            if(indexProcessedOperation(i, clone)){
                continue;
            }
            if(token.type != SymbolicTokenType.OPERATOR){
                continue;
            }
            if(token.rep.equals(SymbolicTokenizer.MULT)){
                SymbolicIntermediateOperation left = left(i, clone);
                SymbolicIntermediateOperation right = right(i, clone);
                if(left == null || right == null){
                    throw new SyntaxException("MULT requires both left and right elements.");
                }
                clone.remove(left);
                clone.remove(right);
                clone.add(new SymbolicIntermediateMultiplication(left.startIndex, right.endIndex, left, right));
            }else if(token.rep.equals(SymbolicTokenizer.DIVIDE)){
                SymbolicIntermediateOperation left = left(i, clone);
                SymbolicIntermediateOperation right = right(i, clone);
                if(left == null || right == null){
                    throw new SyntaxException("DIV requires both left and right elements.");
                }
                clone.remove(left);
                clone.remove(right);
                clone.add(new SymbolicIntermediateDivision(left.startIndex, right.endIndex, left, right));
            }
        }
        retList = new ArrayList<>(clone);
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        return retList;
    }

    public static List<SymbolicIntermediateOperation> generateAddSub(List<SymbolicIntermediateOperation> curr, List<SymbolicTokenSet> tokenSets, List<Token<SymbolicTokenType>> inputList){
        List<SymbolicIntermediateOperation> clone = new ArrayList<>(curr);
        List<SymbolicIntermediateOperation> retList = new ArrayList<>();
        for (int i = 0; i < inputList.size(); i++) {
            Token<SymbolicTokenType> token = inputList.get(i);
            if(indexProcessedOperation(i, clone)){
                continue;
            }
            if(token.type != SymbolicTokenType.OPERATOR){
                continue;
            }
            if(token.rep.equals(SymbolicTokenizer.PLUS)){
                SymbolicIntermediateOperation left = left(i, clone);
                SymbolicIntermediateOperation right = right(i, clone);
                if(right == null){
                    throw new SyntaxException("PLUS requires a right element.");
                }
                if(left != null){
                    clone.remove(left);
                    clone.remove(right);
                    clone.add(new SymbolicIntermediateAddition(left.startIndex, right.endIndex, left, right));
                }
            }else if(token.rep.equals(SymbolicTokenizer.MINUS)){
                SymbolicIntermediateOperation left = left(i, clone);
                SymbolicIntermediateOperation right = right(i, clone);
                if(right == null){
                    throw new SyntaxException("SUB requires a right element.");
                }
                if(left != null){
                    clone.remove(left);
                    clone.remove(right);
                    clone.add(new SymbolicIntermediateSubtraction(left.startIndex, right.endIndex, left, right));
                }
            }
        }
        retList = new ArrayList<>(clone);
        retList.sort(Comparator.comparingInt(a -> a.startIndex));
        return retList;
    }

    public static SymbolicIntermediateOperation left(int i, List<SymbolicIntermediateOperation> tokenList){
        for(SymbolicIntermediateOperation tokenSet : tokenList){
            if(RangeUtils.inRange(i - 1, tokenSet.startIndex, tokenSet.endIndex)){
                return tokenSet;
            }
        }
        return null;
    }

    public static SymbolicIntermediateOperation right(int i, List<SymbolicIntermediateOperation> tokenList){
        for(SymbolicIntermediateOperation tokenSet : tokenList){
            if(RangeUtils.inRange(i + 1, tokenSet.startIndex, tokenSet.endIndex)){
                return tokenSet;
            }
        }
        return null;
    }

    public static boolean indexProcessedOperation(int i, List<SymbolicIntermediateOperation> opList){
        for(SymbolicIntermediateOperation t : opList){
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
