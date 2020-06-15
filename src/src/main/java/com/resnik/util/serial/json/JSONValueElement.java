package com.resnik.util.serial.json;

import org.json.simple.JSONObject;

public class JSONValueElement extends JSONElement<Object> {

    public JSONValueElement(String name, Object rep) {
        super(name, rep);
    }

    @Override
    public JSONObjectElement toJSONObjectElement() {
        JSONObject retJson = new JSONObject();
        retJson.put(this.name, this.rep);
        return new JSONObjectElement(this.name, retJson);
    }

    public String toString(){
        return name + ":" + rep;
    }



}
