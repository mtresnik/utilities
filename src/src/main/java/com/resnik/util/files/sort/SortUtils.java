package com.resnik.util.files.sort;

import com.resnik.util.files.FileUtils;
import com.resnik.util.logger.Log;

import java.io.File;
import java.util.*;

public class SortUtils {

    public static final String TAG = SortUtils.class.getSimpleName();

    public static final String outputName = "SORTED";

    public static File getSortedDir(File inputDirectory){
        File sortedDir = new File(inputDirectory, outputName);
        if(!sortedDir.exists()){
            sortedDir.mkdir();
        }
        return sortedDir;
    }

    // Put all files into sub directories based on file extensions
    public static void sortExtensions(File inputDirectory){
        if(inputDirectory == null || !inputDirectory.isDirectory()){
            throw new IllegalArgumentException("Must be non null directory.");
        }
        if(!inputDirectory.exists()){
            throw new IllegalArgumentException("Directory doesn't exist.");
        }
        File[] subFiles = inputDirectory.listFiles();
        Map<String, List<File>> extensionMap = new LinkedHashMap<>();
        List<File> directories = new ArrayList<>();
        for(File file : subFiles){
            if(file.isDirectory() && !file.getName().equals(outputName)){
                directories.add(file);
                continue;
            }
            String extension = FileUtils.getFileExtension(file).toLowerCase();
            if(!extensionMap.containsKey(extension)){
                extensionMap.put(extension, new ArrayList<File>(){{add(file);}});
            }else{
                extensionMap.get(extension).add(file);
            }
        }
        Log.v(TAG,"Extension Map:" + extensionMap);
        Log.v(TAG,"Directories:" + directories);
        File sortedDir = getSortedDir(inputDirectory);

    }

    public static void main(String[] args) {
        sortExtensions(new File("D:\\School"));
    }

}
