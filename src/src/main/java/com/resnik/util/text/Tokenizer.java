package com.resnik.util.text;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface Tokenizer<TOKEN_TYPE> {

    List<Token<TOKEN_TYPE>> tokenize(String inputString, Map<String, TOKEN_TYPE> preMap);

    default List<Token<TOKEN_TYPE>> tokenize(String inputString){
        return tokenize(inputString, Collections.EMPTY_MAP);
    }
}
