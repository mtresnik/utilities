package com.resnik.util.math.numbers;


import com.resnik.util.logger.Log;
import com.resnik.util.math.MathUtils;
import com.resnik.util.math.plot.histogram.Histogram;
import com.resnik.util.objects.structures.CountList;
import com.resnik.util.objects.structures.CountObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Prime {

    public static final String TAG = Prime.class.getSimpleName();

    private static final int[] FIRST_10 = new int[]{2,3,5,7,11,13,17,19,23,29};
    private static final Map<Integer, Integer> EVALUATED = new LinkedHashMap<>();

    private static final BigInteger[] FIRST_10_BIG = new BigInteger[FIRST_10.length];
    private static final Map<Integer, BigInteger> EVALUATED_BIG = new LinkedHashMap<>();

    static {
        for(int i = 0; i < FIRST_10.length; i++){
            EVALUATED.put(i, FIRST_10[i]);
            FIRST_10_BIG[i] = new BigInteger(Integer.toString(FIRST_10[i]));
            EVALUATED_BIG.put(i, FIRST_10_BIG[i]);
        }
    }
    private Prime(){
    }

    public static int lowestPrimeDivisor(int num){
        for(Map.Entry<Integer, Integer> entry : EVALUATED.entrySet()){
            if(num % entry.getValue() == 0){
                return entry.getValue();
            }
        }
        for(int i = 2; i < Math.sqrt(num) + 1; i++){
            if(num % i == 0){
                return i;
            }
        }
        return num;
    }

    public static boolean isPrime(int num){
        if(num <= 1){
            return false;
        }
        for(Map.Entry<Integer, Integer> entry : EVALUATED.entrySet()){
            if(num % entry.getValue() == 0){
                return false;
            }
        }
        for(int i = 2; i < Math.sqrt(num) + 1; i++){
            if(num % i == 0){
                return false;
            }
        }
        return true;
    }

    public static boolean isPrime(BigInteger num){
        String rep = num.toString();
        String lastChar = Character.toString(rep.charAt(rep.length() - 1));
        if(lastChar.equals("0")){
            return false;
        }
        if(num.compareTo(BigInteger.ONE) <= 0){
            return false;
        }
        for(Map.Entry<Integer, BigInteger> entry : EVALUATED_BIG.entrySet()){
            if(num.mod(entry.getValue()).compareTo(BigInteger.ZERO) == 0){
                return false;
            }
        }
        for(BigInteger i = new BigInteger("2"); i.multiply(i).compareTo(num) < 1; i = i.add(BigInteger.ONE)){
            if(num.mod(i).compareTo(BigInteger.ZERO) == 0){
                return false;
            }
        }
        return true;
    }

    public static int[] listPrimes(int n){
        int[] retArray = new int[n];
        int first = 2;
        int prev = first;
        if(n == 1){
            return new int[]{first};
        }
        if(n > 1){
            retArray[0] = first;
        }
        for(int index = 1; index < retArray.length; index++){
            int curr = EVALUATED.getOrDefault(index, nextPrime(prev));
            EVALUATED.putIfAbsent(index, curr);
            retArray[index] = curr;
            prev = curr;
        }
        return retArray;
    }

    public static CountList<Integer> getEndings(int n){
        int[] primes = listPrimes(n);
        CountList<Integer> tempList = new CountList<>();
        for(int prime : primes){
            int[] digits = MathUtils.digits(prime);
            tempList.addElement(digits[digits.length - 1]);
        }
        int[] digits = new int[]{0,1,2,3,4,5,6,7,8,9};
        List<CountObject<Integer>> endingsList = new ArrayList<>();
        for(int curr : digits){
            Log.v(TAG,curr);
            int count = tempList.getCountOr(curr, 0);
            endingsList.add(new CountObject<>(curr, count));
        }
        return new CountList<>(endingsList, false);
    }

    public static BigInteger[] listBigPrimes(int n){
        BigInteger[] retArray = new BigInteger[n];
        BigInteger first = new BigInteger("2");
        BigInteger prev = first;
        if(n > 1){
            retArray[0] = first;
        }
        for(int index = 1; index < retArray.length; index++){
            BigInteger curr = EVALUATED_BIG.getOrDefault(index, nextPrime(prev));
            EVALUATED_BIG.putIfAbsent(index, curr);
            retArray[index] = curr;
            prev = curr;
        }
        return retArray;
    }

    public static int nextPrime(int start){
        int curr = start;
        while(!isPrime(curr)){
            curr++;
        }
        return curr;
    }

    public static BigInteger nextPrime(BigInteger start){
        BigInteger curr = start;
        while(!isPrime(curr)){
            curr = curr.add(BigInteger.ONE);
        }
        return curr;
    }

    public static int[] listPrimeFactors(int num){
        if(num <= 1){
            return new int[0];
        }
        if(isPrime(num)){
            return new int[]{num};
        }
        List<Integer> retList = new ArrayList<>();
        int lowestPrimeDivisor = lowestPrimeDivisor(num);
        int remainder = num / lowestPrimeDivisor;
        retList.add(lowestPrimeDivisor);
        int[] remainderFactors = listPrimeFactors(remainder);
        for(int fact : remainderFactors){
            retList.add(fact);
        }
        int[] retArray = new int[retList.size()];
        for(int i = 0; i < retArray.length; i++){
            retArray[i] = retList.get(i);
        }
        return retArray;
    }


    public static BufferedImage[] getEndingsGif(int n){
        return getEndingsGif(10, n, 5);
    }

    public static BufferedImage[] getEndingsGif(int start, int n, int stride){
        List<BufferedImage> bufferedImages = new ArrayList<>();
        Color temp = new Color(127,0,127,255);
        for(int i = start; i <= n; i += stride){
            CountList<Integer> currList = getEndings(i);
            Histogram<Integer> histogram = new Histogram<>(500, 300, currList, temp);
            BufferedImage bufferedFrame = histogram.getBarsImageBuffered();
            bufferedImages.add(bufferedFrame);
        }
        BufferedImage[] bufferedArray = bufferedImages.toArray(new BufferedImage[bufferedImages.size()]);
        return bufferedArray;
    }

}
