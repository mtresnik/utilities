package com.resnik.util.math.numbers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.resnik.util.math.MathUtils.mc;
import static com.resnik.util.math.numbers.BigNumberUtils.copyOnRange;
import static com.resnik.util.math.numbers.BigNumberUtils.fromInts;

public final class ContinuedFraction {

    private static final int[] PI_CONTINUED_FRACTION = new int[]{
            3,7,15,1,292,1,1,1,2,1,3,1,14,2,1,1,2,2,2,2,1,84,
            2,1,1,15,3,13,1,4,2,6,6,99,1,2,2,6,3,5,1,1,6,8,1,
            7,1,2,3,7,1,2,1,1,12,1,1,1,3,1,1,8,1,1,2,1,6,1,1,
            5,2,2,3,1,2,4,4,16,1,161,45,1,22,1,2,2,1,4,1,2,24,
            1,2,1,3,1,2,1};

    private ContinuedFraction(){}

    public static BigDecimal simpleContinuedFraction(BigInteger[] a){
        if(a.length == 0){
            return BigDecimal.ZERO;
        }
        if(a.length == 1){
            return new BigDecimal(a[0]);
        }
        return simpleContinuedFraction(a[0], Arrays.copyOfRange(a, 1, a.length));
    }

    public static BigDecimal simpleContinuedFraction(BigInteger a_0, BigInteger ... a){
        if(a == null || a.length == 0){
            return new BigDecimal(a_0);
        }
        if(a_0.equals(BigInteger.ZERO)){
            return BigDecimal.ONE.divide(simpleContinuedFraction(a));
        }
        return new BigDecimal(a_0).add(BigDecimal.ONE.divide(simpleContinuedFraction(a), mc), mc);
    }

    public static BigDecimal e(int n){
        return simpleContinuedFraction(eContinuedFraction(n));
    }

    public static BigInteger[] eContinuedFraction(int precision){
        if(precision <= 0){
            return new BigInteger[0];
        }
        BigInteger first = new BigInteger("2");
        BigInteger[] ret = new BigInteger[precision];
        ret[0] = first;
        int twoIndex = 1;
        for(int i = 1; i < precision; i++){
            if((i + 1) % 3 == 0){
                ret[i] = first.multiply(new BigInteger(Integer.toString(twoIndex)));
                twoIndex++;
            }else {
                ret[i] = BigInteger.ONE;
            }
        }
        return ret;
    }

    public static BigDecimal goldenRatio(int precision){
        return simpleContinuedFraction(goldenRatioContinuedFraction(precision));
    }

    public static BigInteger[] goldenRatioContinuedFraction(int precision){
        return copyOnRange(BigInteger.ONE, precision);
    }

    public static BigInteger[] sqrt2ContinuedFracttion(int precision){
        if(precision <= 0){
            return new BigInteger[0];
        }
        BigInteger first = BigInteger.ONE;
        BigInteger[] ret = new BigInteger[precision];
        ret[0] = first;
        for(int i = 1; i < precision; i++){
            ret[i] = new BigInteger("2");
        }
        return ret;
    }

    public static BigInteger[] generateContinuedFraction(BigDecimal input){
        return generateContinuedFraction(input, new BigInteger[0]);
    }

    private static BigInteger[] generateContinuedFraction(BigDecimal input, BigInteger[] currArr){
        if(BigNumberUtils.abs(simpleContinuedFraction(currArr).subtract(input)).compareTo(new BigDecimal(Double.toString(1e-3))) < 0){
            return currArr;
        }
        BigInteger a_0 = new BigInteger(input.toString().substring(0, input.toString().indexOf(".")));
        BigDecimal denom = input.subtract(new BigDecimal(a_0));
        BigDecimal rem = BigDecimal.ONE.divide(denom, MathContext.DECIMAL128);
        List<BigInteger> retList = new ArrayList<>();
        retList.addAll(Arrays.asList(currArr));
        retList.add(a_0);
        retList = (Arrays.asList(generateContinuedFraction(rem, retList.toArray(new BigInteger[retList.size()]))));
        BigInteger[] retArr = retList.toArray(new BigInteger[retList.size()]);;
        return retArr;
    }

    public static BigDecimal piContinued(){
        return simpleContinuedFraction(fromInts(PI_CONTINUED_FRACTION));
    }

    public static BigDecimal sqrt2Continued(int precision){
        return simpleContinuedFraction(sqrt2ContinuedFracttion(precision));
    }


}
