package com.resnik.util.math.symbo.algebra.parse;

import com.resnik.util.text.Token;

import java.util.List;

public class SymbolicTokenSet {

    public int startIndex = -1;
    public int endIndex = -1;
    public SymbolicTokenSetType type;
    public List<Token<SymbolicTokenType>> tokens;
    public String rep;

    public SymbolicTokenSet(int firstIndex, int secondIndex, SymbolicTokenSetType type, List<Token<SymbolicTokenType>> tokens) {
        this.startIndex = firstIndex;
        this.endIndex = secondIndex;
        this.type = type;
        this.tokens = tokens;
    }

    @Override
    public String toString(){
        return "[" + type + ":(" + startIndex + "," + endIndex + ")"+ (rep != null ? ":" + rep : "") + "]";
    }

}
