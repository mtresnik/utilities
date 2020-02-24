package com.resnik.util.text;

import java.util.Arrays;

import static com.resnik.util.objects.reflection.ReflectionUtils.stringOf;

public final class StringUtils {

    private StringUtils() {
    }

    public static String concat(Object... a) {
        return concatDelim("", a);
    }

    public static String concatNL(Object... a) {
        return concatDelim("\n", a) + "\n";
    }

    public static String concatDelim(String delim, Object... a) {
        String retString = "";
        for (int i = 0; i < a.length; i++) {
            String append = "";
            append = stringOf(a[i]);

            retString += ((i < a.length - 1) ? append + delim : append);
        }
        return retString;
    }
    
    public static int stringSimilar(String s1, String s2){
        if(s1.length() != s2.length()){
            throw new IllegalArgumentException("Strings must be of the same length.");
        }
        int retInt = 0;
        for (int i = 0; i < s1.length(); i++) {
            if(s1.charAt(i) == s2.charAt(i)){
                retInt++;
            }
        }
        return retInt;
    }

    public static String solidLine(int n){
        return repeatChar('-', n);
    }

    public static String repeatChar(char c, int n){
        String retString = "";
        for(int i = 0; i < n; i++){
            retString += c;
        }
        return retString;
    }

    public static String[] findRem(String test, String key){
        if(key == null || !test.contains(key)){
            return new String[0];
        }
        if(test.length() == key.length()){
            return new String[0];
        }
        int index = test.indexOf(key);
        if(index == 0){
           return new String[]{test.substring(key.length())};
        }
        if(test.endsWith(key)){
            return new String[]{test.substring(0,test.length() - key.length())};
        }
        return new String[]{
                test.substring(0, index),
                test.substring(index + key.length())
        };
    }

    public static String generalToString(Object o){
        if(o == null){
            return "null";
        }
        if(o instanceof int[]){
            return Arrays.toString((int[]) o);
        }
        if(o instanceof byte[]){
            return Arrays.toString((byte[]) o);
        }
        if(o instanceof double[]){
            return Arrays.toString((double[]) o);
        }
        if(o instanceof float[]){
            return Arrays.toString((float[]) o);
        }
        if(o instanceof char[]){
            return Arrays.toString((char[]) o);
        }
        if(o instanceof short[]){
            return Arrays.toString((short[]) o);
        }
        if(o instanceof long[]){
            return Arrays.toString((long[]) o);
        }
        if(o instanceof Object[]){
            return Arrays.toString((Object[]) o);
        }
        return o.toString();
    }
}
