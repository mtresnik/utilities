package com.resnik.util.math.numbers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.*;

public class Fibonacci {

    public static final int SMALL_FIB_LIMIT = 47;

    public static final double GOLDEN_RATIO_PLUS = (1 + Math.sqrt(5))/2;
    public static final double GOLDEN_RATIO_MINUS = (1 - Math.sqrt(5))/2;

    private static final int[] FIRST_5 = new int[]{0,1,1,2,3};
    private static final Map<Integer, Integer> EVALUATED = new LinkedHashMap<>();

    private static final BigInteger[] FIRST_5_BIG = new BigInteger[FIRST_5.length];
    private static final Map<Integer, BigInteger> EVALUATED_BIG = new LinkedHashMap<>();

    static {
        for(int index = 0; index<FIRST_5.length; index++){
            EVALUATED.put(index, FIRST_5[index]);
            FIRST_5_BIG[index] = new BigInteger(Integer.toString(FIRST_5[index]));
            EVALUATED_BIG.put(index, FIRST_5_BIG[index]);
        }
    }

    @Deprecated
    public static int fibRecursive(int n){
        smallFibCheck(n);
        if(EVALUATED.containsKey(n)){
            return EVALUATED.get(n);
        }
        EVALUATED.put(n, fibRecursive(n-2) + fibRecursive(n-1));
        return EVALUATED.get(n);
    }

    public static int fibLinear(int n){
        smallFibCheck(n);
        if(EVALUATED.containsKey(n)){
            return EVALUATED.get(n);
        }
        if(EVALUATED.containsKey(n - 2) && EVALUATED.containsKey(n - 1)){
            return EVALUATED.get(n - 2) + (EVALUATED.get(n - 1));
        }
        int lastIndex = 2;
        for(Map.Entry<Integer, Integer> entry : EVALUATED.entrySet()){
            lastIndex = Math.max(entry.getKey(), lastIndex);
        }
        for(int index = lastIndex + 1; index <= n; index++){
            Integer currFib = EVALUATED.get(index - 2) + (EVALUATED.get(index - 1));
            EVALUATED.putIfAbsent(index, currFib);
        }
        return EVALUATED.get(n);
    }

    public static int[] fibArrLinear(int n){
        smallFibCheck(n);
        int[] retArr = new int[n];
        for(int index = 0; index < n; index++){
            retArr[index] = fibLinear(index);
        }
        return retArr;
    }

    @Deprecated
    public static int[] fibArrRecursive(int n){
        smallFibCheck(n);
        int[] retArr = new int[n];
        for(int index = 0; index < n; index++){
            retArr[index] = fibRecursive(index);
        }
        return retArr;
    }

    private static void smallFibCheck(int n){
        if(n >= SMALL_FIB_LIMIT){
            throw new IllegalArgumentException("Small fibonacci numbers must be below the threshold:" + SMALL_FIB_LIMIT + "\t Please use the BigInteger equivalent.");
        }
    }

    public static double goldenRatioApproxSmall(int n){
        smallFibCheck(n);
        double prev = fibLinear(n - 1);
        double next = fibLinear(n);
        return next / prev;
    }

    public static BigDecimal goldenRatioApproxBig(int n){
        BigInteger next = fibBigLinear(n);
        BigInteger prev = fibBigLinear(n - 1);
        return new BigDecimal(next).divide(new BigDecimal(prev), MathContext.DECIMAL128);
    }

    @Deprecated
    public static BigInteger fibBigRecursive(int n){
        if(EVALUATED_BIG.containsKey(n)){
            return EVALUATED_BIG.get(n);
        }
        EVALUATED_BIG.put(n, fibBigRecursive(n-2).add(fibBigRecursive(n-1)));
        return EVALUATED_BIG.get(n);
    }

    public static BigInteger fibBigLinear(int n){
        if(EVALUATED_BIG.containsKey(n)){
            return EVALUATED_BIG.get(n);
        }
        if(EVALUATED_BIG.containsKey(n - 2) && EVALUATED_BIG.containsKey(n - 1)){
            return EVALUATED_BIG.get(n - 2).add(EVALUATED_BIG.get(n - 1));
        }
        int lastIndex = 2;
        for(Map.Entry<Integer, BigInteger> entry : EVALUATED_BIG.entrySet()){
            lastIndex = Math.max(entry.getKey(), lastIndex);
        }
        for(int index = lastIndex + 1; index <= n; index++){
            BigInteger currFib = EVALUATED_BIG.get(index - 2).add(EVALUATED_BIG.get(index - 1));
            EVALUATED_BIG.putIfAbsent(index, currFib);
        }
        return EVALUATED_BIG.get(n);
    }

    @Deprecated
    public static BigInteger[] fibBigArrRecursive(int n){
        BigInteger[] retArray = new BigInteger[n];
        for(int index = 0; index < n; index++){
            retArray[index] = fibBigRecursive(index);
        }
        return retArray;
    }

    public static BigInteger[] fibBigArrLinear(int n){
        BigInteger[] retArray = new BigInteger[n];
        for(int index = 0; index < n; index++){
            retArray[index] = fibBigLinear(index);
        }
        return retArray;
    }

    public static BigInteger[] primes(int n){
        if(n > 11){
            System.err.println("Prime fibonacci numbers over index 11 will take a while...");
        }
        BigInteger[] retArray = new BigInteger[n];
        List<BigInteger> allPrime = new ArrayList<>();
        int bigIndex = 0;
        for(int index = 0; index < n; index++){
            BigInteger currBig = fibBigLinear(bigIndex);
            while(!Prime.isPrime(currBig)){
                currBig = fibBigLinear(bigIndex);
                bigIndex++;
            }
            retArray[index] = currBig;
            bigIndex++;
        }

        return retArray;
    }

}
