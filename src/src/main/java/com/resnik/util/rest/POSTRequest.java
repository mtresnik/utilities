package com.resnik.util.rest;

public class POSTRequest extends HTTPRequest {

    public static final String TYPE = "POST";

    public POSTRequest(String url) {
        super(url, TYPE);
    }

}
