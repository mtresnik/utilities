package com.resnik.util.rest;

public class GETRequest extends HTTPRequest {

    private final static String TYPE = "GET";

    public GETRequest(String url) {
        super(url, TYPE);
    }

}
