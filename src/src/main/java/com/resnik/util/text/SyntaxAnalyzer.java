package com.resnik.util.text;

import com.resnik.util.math.symbo.parse.SymbolicTokenType;

import java.util.List;
import java.util.Map;

public abstract class SyntaxAnalyzer<TOKENIZER extends Tokenizer<TOKEN_TYPE>, TOKEN_TYPE, RET> {

    protected TOKENIZER tokenizer;
    protected Map<String, TOKEN_TYPE> repMap;

    public SyntaxAnalyzer(TOKENIZER tokenizer, Map<String, TOKEN_TYPE> repMap) {
        this.tokenizer = tokenizer;
        this.repMap = repMap;
    }

    public RET analyze(String inputString) throws SyntaxException{
        return analyze(tokenizer.tokenize(inputString, repMap));
    }

    public abstract RET analyze(List<Token<TOKEN_TYPE>> inputList) throws SyntaxException;

    public abstract void justifySyntax(List<Token<TOKEN_TYPE>> inputList) throws SyntaxException;

}
