package com.resnik.util.serial.json;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class JSONObjectElement extends JSONElement<JSONObject> {

    public JSONObjectElement(String name, JSONObject rep) {
        super(name, rep);
    }

    @Override
    public JSONObjectElement toJSONObjectElement() {
        JSONObject clone = recurJSON(this.rep);
        JSONObjectElement retElement = new JSONObjectElement(this.name, clone);
        return retElement;
    }

    public static JSONObject recurJSON(JSONObject input){
        Map map = (Map) new LinkedHashMap<>(input);
        for(Object objEntry : map.entrySet()){
            Map.Entry entry = (Map.Entry) objEntry;
            if(entry.getValue() instanceof JSONArray){
                JSONArray subChild = (JSONArray) entry.getValue();
                JSONArrayElement jsonArrayElement = new JSONArrayElement(entry.getKey().toString(), subChild);
                JSONObjectElement subObj = jsonArrayElement.toJSONObjectElement();
                JSONObject newRep = recurJSON(subObj.rep);
                map.replace(entry.getKey(), newRep);
            }else if (entry.getValue() instanceof JSONObject){
                map.replace(entry.getKey(), recurJSON((JSONObject) entry.getValue()));
            }
        }
        JSONObject clone = new JSONObject(map);
        return clone;
    }

}
