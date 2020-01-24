package com.resnik.util.files.json;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.LinkedHashMap;

public abstract class JSONElement<T> extends LinkedHashMap<String, Object> {

    protected T rep;
    protected String name;

    public JSONElement(String name, T rep){
        this.rep = rep;
        this.name = name;
    }

    public static JSONElement decide(Object obj){
        return decide(null, obj);
    }

    public static JSONElement decide(String name, Object obj){
        if(obj instanceof JSONArray){
            return new JSONArrayElement(name == null ? "\"array\"" : name, (JSONArray) obj);
        }else if (obj instanceof JSONObject){
            return new JSONObjectElement(name == null ? "\"object\"" : name, (JSONObject) obj);
        }else if(obj instanceof JSONElement){
            JSONElement ret = (JSONElement) obj;
            if(name != null){
                ret.name = name;
            }
            return ret;
        }
        return new JSONValueElement(name == null ? "\"value\"" : name, obj);
    }

    public abstract JSONObjectElement toJSONObjectElement();


}
