package com.resnik.util.math.numbers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;

import static com.resnik.util.math.MathUtils.mc;

public final class BigNumberUtils {

    private BigNumberUtils(){}

    public static BigDecimal pow(BigDecimal low, BigInteger pow){
        if(pow.equals(BigInteger.ZERO) || low.equals(BigDecimal.ONE)){
            return BigDecimal.ONE;
        }
        if(pow.equals(BigInteger.ONE)){
            return low;
        }
        BigInteger posPow = pow;
        boolean doFlip = false;
        if(pow.compareTo(BigInteger.ZERO) < 0){
            posPow = posPow.multiply(new BigInteger("-1"));
            doFlip = true;
        }
        BigDecimal ret = BigDecimal.ONE;
        for(BigInteger i = BigInteger.ZERO; i.compareTo(posPow) < 0; i = i.add(BigInteger.ONE)){
            ret = ret.multiply(low, mc);
        }
        if(doFlip){
            ret = BigDecimal.ONE.divide(ret, mc);
        }
        return ret;
    }

    public static BigInteger[] fromStrings(String ...  input){
        BigInteger[] ret = new BigInteger[input.length];
        for (int i = 0; i < input.length; i++) {
            ret[i] = new BigInteger(input[i]);
        }
        return ret;
    }

    public static BigInteger[] fromInts(int ... input){
        BigInteger[] ret = new BigInteger[input.length];
        for (int i = 0; i < input.length; i++) {
            ret[i] = new BigInteger(Integer.toString(input[i]));
        }
        return ret;
    }

    public static BigInteger[] copyOnRange(BigInteger a, int length){
        BigInteger[] ret = new BigInteger[length];
        for (int i = 0; i < length; i++) {
            ret[i] = a;
        }
        return ret;
    }

    public static BigDecimal[] copyOnRange(BigDecimal a, int length){
        BigDecimal[] ret = new BigDecimal[length];
        for (int i = 0; i < length; i++) {
            ret[i] = a;
        }
        return ret;
    }

    public static <T> void bigIntFor(BigInteger initial, BigInteger limit, BigInteger increment, Function<BigInteger, T> forFunc){
        for(BigInteger i = initial; i.compareTo(limit) < 0; i = i.add(increment)){
            forFunc.apply(i);
        }
    }

    public static BigInteger bigIntSum(BigInteger initial, BigInteger limit, BigInteger increment, Function<BigInteger, BigInteger> forFunc){
        BigInteger[] ret = new BigInteger[]{BigInteger.ZERO};
        bigIntFor(initial, limit, increment, (bi) -> ret[0] = ret[0].add(forFunc.apply(bi)));
        return ret[0];
    }

    public static BigInteger bigIntMult(BigInteger initial, BigInteger limit, BigInteger increment, Function<BigInteger, BigInteger> forFunc){
        BigInteger[] ret = new BigInteger[]{BigInteger.ZERO};
        bigIntFor(initial, limit, increment, (bi) -> ret[0] = ret[0].multiply(forFunc.apply(bi)));
        return ret[0];
    }

    public static BigInteger abs(BigInteger input){
        return new BigInteger(input.toString().replace("-", ""));
    }

    public static BigDecimal abs(BigDecimal input){
        return new BigDecimal(input.toString().replace("-", ""));
    }

}
