package com.resnik.util.serial.csv;

import com.resnik.util.objects.reflection.ReflectionUtils;
import com.resnik.util.text.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class CSV {

    private List<String> header;
    private List<Class> classRep;
    private List<List<String>> lines;
    private List<List<Object>> objectLines;

    private static final String DEFAULT_DELIMITER = ",";

    public CSV(String[] header, List<List<String>> lines){
        if(header == null || header.length == 0){
            throw new IllegalArgumentException("Header must be non-null and non-empty.");
        }
        this.header = Arrays.asList(header);
        this.lines = new ArrayList<>(lines);
        this.inferDataTypes();
    }

    public CSV(String[] header, List<String> ... lines){
        this(header, Arrays.asList(lines));
    }

    public void inferDataTypes(){
        this.objectLines = new ArrayList<>();
        this.classRep = new ArrayList<>();
        for(int i = 0; i < header.size(); i++){
            Class expectedClass = null;
            List<Class> possibleTypes = new ArrayList<>();
            for(List<String> rowString : lines){
                possibleTypes.add(ReflectionUtils.possibleType(rowString.get(i)));
            }
            for(int p = 0; p < possibleTypes.size() - 1; p++){
                if(!possibleTypes.get(p).equals(possibleTypes.get(p + 1))){
                    expectedClass = String.class;
                    break;
                }
            }
            if(expectedClass == null) {
                expectedClass = possibleTypes.isEmpty() ? String.class : possibleTypes.get(0);
            }
            classRep.add(expectedClass);
        }
        for(List<String> rowString : lines){
            List<Object> rowObj = new ArrayList<>();
            for(int i = 0; i < rowString.size(); i++){
                rowObj.add(ReflectionUtils.cast(rowString.get(i), classRep.get(i)));
            }
            this.objectLines.add(rowObj);
        }
    }

    public List<Map.Entry<String, Object>> getRow(int ROW){
        List<Map.Entry<String, Object>> ret = new ArrayList<>();
        List<Object> objs = objectLines.get(ROW);
        for(int i = 0; i < header.size(); i++){
            ret.add(new AbstractMap.SimpleEntry<>(header.get(i), objs.get(i)));
        }
        return ret;
    }

    public List<Object> getColumn(int COL){
        List<Object> ret = new ArrayList<>();
        for(int i = 0; i < lines.size(); i++){
            ret.add(this.objectLines.get(i).get(COL));
        }
        return ret;
    }

    public int keyToIndex(String name){
        for(int i = 0; i < header.size(); i++){
            if(header.get(i).equals(name)){
                return i;
            }
        }
        return -1;
    }

    public List<Object> getColumn(String name){
        int index = keyToIndex(name);
        if(index == -1){
            return new ArrayList<>();
        }
        return getColumn(index);
    }

    public Class getType(int COL){
        return classRep.get(COL);
    }

    public Map.Entry<Class, List<Object>> getTypeAndColumn(int COL){
        return new AbstractMap.SimpleEntry<>(getType(COL), getColumn(COL));
    }

    public List<Map.Entry<String, Class>> getColTypes(){
        List<Map.Entry<String, Class>> retMap = new ArrayList<>();
        for(int i = 0; i < header.size(); i++){
            retMap.add(new AbstractMap.SimpleEntry<>(header.get(i), classRep.get(i)));
        }
        return retMap;
    }

    public static CSV load(String fileLocation) throws IOException{
        File file = new File(fileLocation);
        Scanner sc = new Scanner(file);
        String headerLine = sc.nextLine();
        String[] header = headerLine.split(DEFAULT_DELIMITER);
        List<List<String>> lines = new ArrayList<>();
        while(sc.hasNext()){
            String line = sc.nextLine();
            lines.add(Arrays.asList(line.split(DEFAULT_DELIMITER)));
        }
        sc.close();
        return new CSV(header, lines);
    }

    public void save(String fileLocation) throws IOException{
        File out = new File(fileLocation);
        PrintWriter pw = new PrintWriter(out);
        String headerString = "";
        for(int i = 0; i < header.size(); i++){
            headerString += header.get(i);
            if(i < header.size() - 1){
                headerString += DEFAULT_DELIMITER;
            }
        }
        pw.println(headerString);
        for(List<String> row : this.lines){
            String rowString = "";
            for(int i = 0; i < header.size(); i++){
                rowString += row.get(i);
                if(i < header.size() - 1){
                    rowString += DEFAULT_DELIMITER;
                }
            }
            pw.println(rowString);
        }
        pw.close();
    }

    public void addLine(String string){
        this.addLine(string.split(DEFAULT_DELIMITER));
    }

    public void addLine(String[] row){
        this.addLine(Arrays.asList(row));
    }

    public void addLine(List<String> row){
        this.lines.add(row);
        this.inferDataTypes();
    }

    public int size(){
        return this.lines.size();
    }

    public String toString(){
        String ret = "\n";
        String headerString = "";
        String sep = "\t\t";
        for(int i = 0; i < header.size(); i++){
            headerString += header.get(i);
            if(i < header.size() - 1){
                headerString += sep;
            }
        }
        ret += headerString + "\n";
        ret += StringUtils.repeatChar('-', 2*headerString.length()) + "\n";

        for(List<String> row : this.lines){
            String rowString = "";
            for(int i = 0; i < row.size(); i++){
                rowString += row.get(i);
                if(i < header.size() - 1){
                    rowString += sep;
                }
            }
            ret += rowString + "\n";
        }

        return ret;
    }

}
