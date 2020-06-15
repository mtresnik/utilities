package com.resnik.util.serial.json;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSONArrayElement extends JSONElement<JSONArray> {

    public JSONArrayElement(String name, JSONArray rep) {
        super(replaceFirst(name), rep);
    }

    public static String replaceFirst(String name){
        if(name.charAt(0) == '_'){
            name.replaceFirst("_", "");
        }
        return name;
    }

    public String getChildName(int index){
        return name + "_" + index;
    }

    @Override
    public JSONObjectElement toJSONObjectElement() {
        JSONObject retJson = new JSONObject();
        int count = 0;
        for(Object obj : rep){
            retJson.put(getChildName(count), obj);
            count++;
        }
        JSONObjectElement retObject = new JSONObjectElement(name, retJson);
        return retObject.toJSONObjectElement();
    }


}
