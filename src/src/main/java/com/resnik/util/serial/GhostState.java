package com.resnik.util.serial;

import com.resnik.util.logger.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GhostState {

    public static final String TAG = GhostState.class.getSimpleName();

    public static void makeFileXML(String path, String outPath) {
        File root = new File(path);
        if(new File(outPath).isDirectory()){
            outPath += (outPath.charAt(outPath.length() - 1) == '/' ? "" : "/") +  "out.xml";
        }
        FileElement root_element = new FileElement(root, true);
        root_element.writeXML(outPath);
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Please input a source path (arg0) and a destination path (arg1).");
            System.exit(1);
        }
        if(!new File(args[0]).exists() || !new File(args[0]).isDirectory()){
            System.err.println("Please input a valid source path (arg0).");
            System.exit(1);
        }
        makeFileXML(args[0], args[1]);
    }

    public static void listRoots(){
        File[] roots = File.listRoots();
        for(File root : roots){
            Log.v(TAG,"root:" + root.getAbsolutePath());
            if(root.listFiles() == null){
                continue;
            }
            Log.v(TAG,root.listFiles().length);
        }
    }

    private static class FileElement {

        final File file;
        final String name;
        public static final String FILE_TAG = "file";
        public static final String FOLDER_TAG = "folder";
        public static final String ROOT_TAG = "root";

        public FileElement(File file) {
            this(file, false);
        }

        public FileElement(File file, boolean isRoot){
            if(!file.exists()){
                throw new IllegalArgumentException("File does not exist at location:" + file.getAbsolutePath());
            }
            this.file = file;
            if(isRoot){
                name = file.getAbsolutePath();
            }else{
                name = null;
            }
        }

        public boolean isDirectory(){
            return this.file.isDirectory();
        }

        public List<FileElement> subDirectories(){
            if(!this.isDirectory()){
                return Collections.EMPTY_LIST;
            }
            List<FileElement> retList = new ArrayList();
            for(File child : this.file.listFiles()){
                if(child.isDirectory()){
                    retList.add(new FileElement(child));
                }
            }
            return retList;
        }

        public List<FileElement> subFiles(){
            if(!this.isDirectory()){
                return Collections.EMPTY_LIST;
            }
            List<FileElement> retList = new ArrayList();
            for(File child : this.file.listFiles()){
                if(!child.isDirectory()){
                    retList.add(new FileElement(child));
                }
            }
            return retList;
        }

        public List<FileElement> allChildren(){
            if(!this.isDirectory() || this.file==null || this.file.listFiles() == null){
                return Collections.EMPTY_LIST;
            }
            List<FileElement> retList = new ArrayList();
            for(File child : this.file.listFiles()){
                retList.add(new FileElement(child));
            }
            return retList;
        }

        public StorageInformation size(){
            if(!this.isDirectory()){
                return StorageInformation.generateFromBytes(file.length());
            }
            StorageInformation retAmount = StorageInformation.NULL;
            for(FileElement child : this.allChildren()){
                retAmount = retAmount.add(child.size());
            }
            return retAmount;
        }

        public String toString(){
            return toParameters();
        }

        public String toParameters(){
            String retString = "";
            retString += "name=\"" + (name == null ? this.file.getName() : name) + "\" ";
            retString += "size=\"" + this.size() + "\" ";
            return retString;
        }

        public static String[] encapsulate(final String TAG, String parameterString){
            String[] retArr = new String[2];
            retArr[0] = "<" + TAG + " " + parameterString + ">";
            retArr[1] = "</" + TAG + ">";
            return retArr;
        }

        public static String encapsulateSingle(final String TAG, String parameterString){
            return "<" + TAG + " " + parameterString + "/>";
        }

        public List<String> toXMLLines(){
            List<String> retList = new ArrayList();
            if(!this.isDirectory()){
                retList.add(encapsulateSingle(FILE_TAG, this.toParameters()));
                return retList;
            }
            String[] outer = encapsulate((this.name == null ? FOLDER_TAG : ROOT_TAG), this.toParameters());
            retList.add(outer[0]);
            for(FileElement child : this.allChildren()){
                List<String> rep = child.toXMLLines();
                for(String childLine : rep){
                    retList.add("\t" + childLine);
                }
            }
            retList.add(outer[1]);
            return retList;
        }

        public boolean writeXML(String outPath){
            try {
                PrintWriter pw = new PrintWriter(new File(outPath));
                List<String> lines = this.toXMLLines();
                lines.forEach((line) -> {
                    pw.println(line);
                });
                pw.close();
                return true;
            } catch (FileNotFoundException ex) {
                throw new IllegalArgumentException(ex.getMessage());
            }
        }

        public static class StorageInformation {
            final double amount;
            final String units;
            final long bytes;
            private static final double BYTE_TO_KB = 1024;
            private static final double BYTE_TO_MB = BYTE_TO_KB*1024;
            private static final double BYTE_TO_GB = BYTE_TO_MB*1024;
            private static final double BYTE_TO_TB = BYTE_TO_GB*1024;

            public static final StorageInformation NULL = new StorageInformation(0.0, 0, "B");

            public StorageInformation(double amount, long bytes, String TAG) {
                this.amount = amount;
                this.bytes = bytes;
                this.units = TAG;
            }

            public static StorageInformation generateFromBytes(long storageBytes){
                if(storageBytes/BYTE_TO_TB >= 1){
                    return new StorageInformation(storageBytes/BYTE_TO_TB, storageBytes, "TB");
                }else if(storageBytes/BYTE_TO_GB >= 1){
                    return new StorageInformation(storageBytes/BYTE_TO_GB, storageBytes, "GB");
                }else if(storageBytes/BYTE_TO_MB >= 1){
                    return new StorageInformation(storageBytes/BYTE_TO_MB, storageBytes, "MB");
                }else if(storageBytes/BYTE_TO_KB >= 1){
                    return new StorageInformation(storageBytes/BYTE_TO_KB, storageBytes, "KB");
                }else{
                    return new StorageInformation(storageBytes, storageBytes, "B");
                }
            }

            public String toString(){
                return this.amount + " " + this.units;
            }

            public String toXMLString(){
                return "size=" + this.toString();
            }

            public StorageInformation add(StorageInformation other){
                return generateFromBytes(this.bytes + other.bytes);
            }
        }
    }

}
