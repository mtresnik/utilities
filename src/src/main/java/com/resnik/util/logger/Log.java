package com.resnik.util.logger;

import com.resnik.util.files.FileUtils;
import com.resnik.util.text.StringUtils;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Log {

    private static PrintStream verbose = System.out;
    private static PrintStream err = System.err;
    private static PrintStream debug = System.out;
    private static PrintStream info = System.out;

    private static String VERBOSE_COLOR = ConsoleColors.WHITE;
    private static String ERROR_COLOR = ConsoleColors.RED;
    private static String INFO_COLOR = ConsoleColors.BLUE;
    private static String DEBUG_COLOR = ConsoleColors.GREEN;

    private static boolean ENABLED = true;
    private static File writeFile = null;

    public static void setFile(File file){
        if(file.isDirectory() || FileUtils.getFileExtension(file).isEmpty()){
            file.mkdirs();
            String pattern = "MM-dd-yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            writeFile = new File(file, "Log-" + simpleDateFormat.format(new Date()) + ".txt");
        }else{
            writeFile = file;
        }
    }

    public static void enable(){
        ENABLED = true;
    }

    public static void disable(){
        ENABLED = false;
    }

    public static void resetDefaults(){
        verbose = System.out;
        err = System.err;
        debug = System.out;
        info = System.out;
    }

    public static void setVerbose(PrintStream out) {
        Log.verbose = out;
    }

    public static void setErr(PrintStream err) {
        Log.err = err;
    }

    public static void setDebug(PrintStream debug) {
        Log.debug = debug;
    }

    public static void setInfo(PrintStream info) {
        Log.info = info;
    }

    private static void write(PrintStream printStream, String level, String TAG, String message, String color){
        if(!ENABLED){
            return;
        }
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
        String line = StringUtils.repeatString("-", "VERBOSE".length() - level.length());
        String toPrintStream = color + "Log." + level + line + "-"  + format.format(date) + "-" + TAG + ":" + message + ConsoleColors.RESET;
        printStream.println(toPrintStream);
        if(writeFile != null){
            String toPrint = "Log." + level + line + "-"  + format.format(date) + "-" + TAG + ":" + message;
            PrintWriter pw = null;
            try {
                pw = new PrintWriter(new FileOutputStream(writeFile, true));
                pw.println(toPrint);
                pw.close();
            } catch (FileNotFoundException e) {
            }
        }
    }


    public static void d(String TAG, String message){
        write(debug, "DEBUG", TAG, message, DEBUG_COLOR);
    }

    public static void d(String TAG, int message){
        d(TAG, Integer.toString(message));
    }

    public static void d(String TAG, double message){
        d(TAG, Double.toString(message));
    }

    public static void d(String TAG, Object message){
        d(TAG, Objects.toString(message));
    }

    public static void i(String TAG, String message){
        write(info, "INFO", TAG, message, INFO_COLOR);
    }

    public static void i(String TAG, int message){
        i(TAG, Integer.toString(message));
    }

    public static void i(String TAG, double message){
        i(TAG, Double.toString(message));
    }

    public static void i(String TAG, Object message){
        i(TAG, Objects.toString(message));
    }

    public static void e(String TAG, String message) {
        write(err, "ERROR", TAG, message, ERROR_COLOR);
    }

    public static void e(String TAG, int message){
        e(TAG, Integer.toString(message));
    }

    public static void e(String TAG, double message){
        e(TAG, Double.toString(message));
    }

    public static void e(String TAG, Object message){
        e(TAG, Objects.toString(message));
    }

    public static void v(String TAG, String message){
        write(verbose, "VERBOSE", TAG, message, VERBOSE_COLOR);
    }

    public static void v(String TAG, int message){
        i(TAG, Integer.toString(message));
    }

    public static void v(String TAG, double message){
        i(TAG, Double.toString(message));
    }

    public static void v(String TAG, Object message){
        v(TAG, Objects.toString(message));
    }



}
