package com.resnik.util.text;

import java.util.*;

public final class Similarity {

    private Similarity(){}

    public static int[][] lcsNumArray(String s1, String s2){
        int[][] numArray = new int[s1.length() + 1][s2.length() + 1];
        for(int i = 1; i < numArray[0].length; i++){
            char c2 = s2.charAt(i - 1);
            for(int j = 1; j < numArray.length; j++){
                char c1 = s1.charAt(j - 1);
                if(c1 == c2){
                    numArray[j][i] = numArray[j - 1][i - 1] + 1;
                }else {
                    numArray[j][i] = Math.max(numArray[j-1][i], numArray[j][i-1]);
                }
            }
        }
        return numArray;
    }

    public static String lcs(String s1, String s2){
        int[][] numArray = lcsNumArray(s1, s2);
        Stack<Character> retStack = new Stack<>();
        int i = numArray.length - 1;
        int j = numArray[0].length - 1;
        int currNum = numArray[i][j];
        while(currNum != 0){
            if(numArray[i - 1][j] == currNum){
                i--;
            }else if(numArray[i][j - 1] == currNum){
                j--;
            }else if(numArray[i - 1][j - 1] != currNum){
                i--;
                j--;
                retStack.push(s1.charAt(i));
            }
            currNum = numArray[i][j];
        }
        String retString = "";
        while(!retStack.isEmpty()){
            retString += retStack.pop();
        }
        return retString;
    }

    public static double lcsSimilarity(String s1, String s2){
        return lcs(s1,s2).length() / ((double) Math.max(s1.length(), s2.length()));
    }

    public static Comparator<String> similarityComparator(final String compareTo){
        return new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return Double.compare(1 - lcsSimilarity(s, compareTo), 1 - lcsSimilarity(t1, compareTo));
            }
        };
    }

    public static Map<String, List<String>> generateCompareMap(String... values){
        Map<String, List<String>> retMap = new LinkedHashMap<>();
        for(String str : values){
            List<String> rep = new ArrayList<>();
            for(String s1 : values){
                rep.add(s1);
            }
            rep.sort(similarityComparator(str));
            retMap.put(str, rep);
        }
        return retMap;
    }


}
