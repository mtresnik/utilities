package com.resnik.util.text.similarity;

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

    public static String stripPunctuation(String input){
        String retString = "";
        String puncSet = ".,/\\[]{}!?@#$%^&*()-_=+\"';`~<>";
        for(char c : input.toCharArray()){
            if(!puncSet.contains(c+"")){
                retString += c;
            }
        }
        return retString;
    }

    public static List<String> addIgnoreCase(String[] vals, List<String> ret){
        for(String aTemp : vals){
            boolean contains = false;
            for(String uTemp : ret){
                if(aTemp.equalsIgnoreCase(uTemp)){
                    contains = true;
                    break;
                }
            }
            if(!contains){
                ret.add(aTemp);
            }
        }
        return ret;
    }

    public static Map<String, Integer> countIgnoreCase(String[] vals, Map<String, Integer> ret){
        for(String temp : vals){
            String match = null;
            for(String subTemp : ret.keySet()){
                if(temp.equalsIgnoreCase(subTemp)){
                    match = subTemp;
                    break;
                }
            }
            int count = ret.get(match);
            ret.put(match, count+1);
        }
        return ret;
    }

    public static double cosine(String a, String b){
        String[] aSplit = stripPunctuation(a).split(" ");
        String[] bSplit = stripPunctuation(b).split(" ");
        return cosine(aSplit, bSplit);
    }

    public static double cosine(String[] aSplit, String[] bSplit){
        System.out.println(Arrays.toString(aSplit) + "\t" + Arrays.toString(bSplit));
        Map<String, Integer> aMap = new LinkedHashMap<>();
        Map<String, Integer> bMap = new LinkedHashMap<>();
        List<String> uniqueYoke = addIgnoreCase(aSplit, new ArrayList<>());
        addIgnoreCase(bSplit, uniqueYoke);
        for(String temp : uniqueYoke){
            aMap.put(temp, 0);
            bMap.put(temp, 0);
        }
        countIgnoreCase(aSplit, aMap);
        countIgnoreCase(bSplit, bMap);
        Collection<Integer> aVec = aMap.values();
        Collection<Integer> bVec = bMap.values();
        // a . b = |a|*|b|*cos(a^b)
        assert (aVec.size() == bVec.size());
        Integer[] aArr = aVec.toArray(new Integer[aVec.size()]);
        Integer[] bArr = bVec.toArray(new Integer[bVec.size()]);
        System.out.println(Arrays.toString(aArr) + "\t" + Arrays.toString(bArr));
        double dot = 0.0;
        double aSquareSum = 0.0;
        double bSquareSum = 0.0;
        for(int i = 0; i < aArr.length; i++){
            dot += aArr[i]*bArr[i];
            aSquareSum += Math.pow(aArr[i], 2);
            bSquareSum += Math.pow(bArr[i], 2);
        }
        double aMag = Math.sqrt(aSquareSum);
        double bMag = Math.sqrt(bSquareSum);
        return dot / (aMag * bMag);
    }




}
