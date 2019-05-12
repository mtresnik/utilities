package util.objects;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class files {

    
    private files() {

    }
    
    
    public static String getFileExtension(File file) {
        String fileExtension = "";
        String fileName = file.getName();
        if (fileName.contains(".") && fileName.lastIndexOf(".") != 0) {
            fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return fileExtension;
    }
    
    public static Map<String, List<String>> getFileExtensionNameMap(String directory){
        File dir = new File(directory);
        File[] subFiles = dir.listFiles();
        System.out.println("subFiles:" + subFiles);
        Map<String, List<String>> retMap = new HashMap();
        for(File file : subFiles){
            String key = getFileExtension(file);
            String value = file.getName();
            if(retMap.containsKey(key)){
                retMap.get(key).add(value);
            }else{
                retMap.put(key, new ArrayList(){{this.add(value);}});
            }
        }
        return retMap;
    }
    
    public static String[] getNames(String directory, String extension){
        Map<String, List<String>> extensionMap = getFileExtensionNameMap(directory);
        List<String> matchingList = extensionMap.get(extension);
        String[] retArray = new String[matchingList.size()];
        retArray = matchingList.toArray(retArray);
        return retArray;
    }
    
    public static Map<String, List<String>> getFileExtensionLocationMap(String directory){
        File dir = new File(directory);
        File[] subFiles = dir.listFiles();
        Map<String, List<String>> retMap = new HashMap();
        for(File file : subFiles){
            String key = getFileExtension(file);
            String value = file.getAbsolutePath();
            if(retMap.containsKey(key)){
                retMap.get(key).add(value);
            }else{
                retMap.put(key, new ArrayList(){{this.add(value);}});
            }
        }
        return retMap;
    }
    
    public static String[] getLocations(String directory, String extension){
        Map<String, List<String>> extensionMap = getFileExtensionLocationMap(directory);
        List<String> matchingList = extensionMap.get(extension);
        String[] retArray = new String[matchingList.size()];
        retArray = matchingList.toArray(retArray);
        return retArray;
    }
    
    public static void saveFile(String[] input, String fileLocation) throws FileNotFoundException{
        char[][] tempArray = new char[input.length][];
        for (int i = 0; i < input.length; i++) {
            tempArray[i] = input[i].toCharArray();
        }
        saveFile(tempArray, fileLocation);
    }

    

    public static void saveFile(char[][] input, String fileLocation) throws FileNotFoundException {
        File file = new File(fileLocation);
        PrintWriter pw = new PrintWriter(file);
        for (char[] row : input) {
            for (char elem : row) {
                pw.print(elem);
            }
            pw.println();
        }
        pw.close();
    }
}
