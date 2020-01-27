package com.resnik.util.text;

import com.sun.javaws.exceptions.InvalidArgumentException;

public class SyntaxException extends IllegalArgumentException {

    public SyntaxException(String message) {
        super(message);
    }

}
