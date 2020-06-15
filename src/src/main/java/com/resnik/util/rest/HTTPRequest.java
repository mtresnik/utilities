package com.resnik.util.rest;

import com.resnik.util.logger.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public abstract class HTTPRequest {

    final String url;
    final String type;
    final Map<String, String> arguments;
    final Map<String, String> headers;

    public HTTPRequest(String url, String type) {
        this.url = url;
        this.type = type;
        this.arguments = new LinkedHashMap<>();
        this.headers = new LinkedHashMap<>();
    }

    public static GETRequest get(String url){
        return new GETRequest(url);
    }

    public static POSTRequest post(String url){
        return new POSTRequest(url);
    }

    public HTTPResponse send() throws IOException{
        HttpURLConnection connection = this.open();
        if(this instanceof POSTRequest){
            connection.setDoInput(true);
        }
        connection.setDoOutput(true);
        connection = this.setArguments(connection);
        int responseCode = connection.getResponseCode();
        List<String> lines = getOutput(connection);
        return new HTTPResponse(lines, responseCode);
    }

    HttpURLConnection open() throws IOException {
        HttpURLConnection ret = (HttpURLConnection) new URL(url).openConnection();
        ret.setRequestMethod(type);
        ret.setRequestProperty("User-Agent", "mresnik");
        ret.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        for(Map.Entry<String,String> entry : headers.entrySet()){
            ret.setRequestProperty(entry.getKey(), entry.getValue());
        }
        return ret;
    }

    HttpURLConnection setArguments(HttpURLConnection httpClient) throws IOException {
        if(arguments.isEmpty()){
            return httpClient;
        }
        String urlParameters = "";
        int count = 0;
        for(Map.Entry<String, String> entry : arguments.entrySet()){
            urlParameters += entry.getKey() + "=" + entry.getValue();
            if(count < arguments.entrySet().size() - 1){
                urlParameters += "&";
            }
            count++;
        }
        try (DataOutputStream wr = new DataOutputStream(httpClient.getOutputStream())) {
            wr.writeBytes(urlParameters);
            wr.flush();
        }
        return httpClient;
    }

    static List<String> getOutput(HttpURLConnection httpClient) throws IOException {
        List<String> response = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                response.add(line);
            }
        }
        return response;
    }

    public Map<String, String> getArguments() {
        return arguments;
    }

    public void setArguments(Map<String, String> arguments){
        this.arguments.clear();;
        this.arguments.putAll(arguments);
    }

    public HTTPRequest putArgument(String key, Object value){
        this.arguments.put(key, Objects.toString(value));
        return this;
    }

    public HTTPRequest putHeader(String key, Object value){
        this.headers.put(key, Objects.toString(value));
        return this;
    }

}
