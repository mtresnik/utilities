package com.resnik.util.rest;

import com.resnik.util.serial.json.JSONTree;
import com.resnik.util.serial.xml.XMLTree;
import com.resnik.util.serial.xml.XMLTreeParser;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HTTPResponse {

    List<String> lines = new ArrayList<>();
    int responseCode;

    public HTTPResponse(List<String> lines, int responseCode) {
        this.lines = lines;
        this.responseCode = responseCode;
    }

    public JSONTree toJSON() throws ParseException {
        String combinedString = "";
        for(String line : lines){
            combinedString += line;
        }
        JSONParser jsonParser = new JSONParser();
        Object ret = jsonParser.parse(combinedString);
        JSONTree tree = JSONTree.generateFromObject(ret);
        return tree;
    }

    public XMLTree toXML() throws IOException, SAXException, ParserConfigurationException {
        String combinedString = "";
        for(String line : lines){
            combinedString += line;
        }
        return XMLTreeParser.fromString(combinedString);
    }

    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public String toString(){
        String ret = "";
        for(String line : lines){
            ret += line + "\n";
        }
        return ret;
    }
}
