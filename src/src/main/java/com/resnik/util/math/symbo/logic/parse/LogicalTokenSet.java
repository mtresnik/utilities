package com.resnik.util.math.symbo.logic.parse;

import com.resnik.util.text.Token;

import java.util.List;

public class LogicalTokenSet {

    public int startIndex = -1;
    public int endIndex = -1;
    public LogicalTokenSetType type;
    public List<Token<LogicalTokenType>> tokens;
    public String rep;

    public LogicalTokenSet(int firstIndex, int secondIndex, LogicalTokenSetType type, List<Token<LogicalTokenType>> tokens) {
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
