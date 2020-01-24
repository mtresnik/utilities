package com.resnik.util.text;

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
    
    
    

}
