package com.resnik.util.text;

import java.util.Arrays;

public class Token<TOKEN_TYPE> {

    public int startIndex, endIndex;
    public TOKEN_TYPE type;
    public String rep;

    public Token(int startIndex, int endIndex, TOKEN_TYPE type, String rep) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.type = type;
        this.rep = rep;
    }

    public Token(int startIndex, int endIndex, TOKEN_TYPE type) {
        this(startIndex, endIndex, type, null);
    }

    public static <TOKEN_TYPE>Token<TOKEN_TYPE> singleIndex(int index, TOKEN_TYPE type){
        return new Token(index, index, type);
    }

    public static <TOKEN_TYPE>Token<TOKEN_TYPE> nullIndex(TOKEN_TYPE type){
        return singleIndex(-1, type);
    }

    public static <TOKEN_TYPE>Token<TOKEN_TYPE> nullIndex(TOKEN_TYPE type, String rep){
        return singleIndex(-1, type, rep);
    }

    public static <TOKEN_TYPE>Token<TOKEN_TYPE> singleIndex(int index, TOKEN_TYPE type, char rep){
        return new Token(index, index, type, Character.toString(rep));
    }

    public static <TOKEN_TYPE>Token<TOKEN_TYPE> singleIndex(int index, TOKEN_TYPE type, String rep){
        return new Token(index, index, type, rep);
    }

    public Token<TOKEN_TYPE> convert(TOKEN_TYPE t){
        return new Token<>(startIndex, endIndex, t, rep);
    }

    public String toString(){
        return "[" + type +":"+ rep +Arrays.toString(new int[]{startIndex, endIndex}) + "]";
    }
}
