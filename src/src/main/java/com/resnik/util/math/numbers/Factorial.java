package com.resnik.util.math.numbers;

import com.resnik.util.logger.Log;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.resnik.util.math.MathUtils.mc;
import static com.resnik.util.math.numbers.BigNumberUtils.pow;

public class Factorial {

    public static final int SMALL_FACT_LIMIT = 17;
    private static final Map<Integer, Integer> EVALUATED = new LinkedHashMap<>();
    private static final Map<BigInteger, BigInteger> EVALUATED_BIG = new LinkedHashMap<>();

    public static int fact(int n){
        if(n > SMALL_FACT_LIMIT){
            throw new IllegalArgumentException("Factorial for small integers limited by:" + SMALL_FACT_LIMIT);
        }
        if(EVALUATED.containsKey(n)){
            return EVALUATED.get(n);
        }
        int ret = 1;
        for(int i = 1; i < n; i++){
            if(EVALUATED.containsKey(i)){
                ret = EVALUATED.get(i);
                continue;
            }
            ret *= i;
            EVALUATED.putIfAbsent(i, ret);
        }
        return ret;
    }

    public static BigInteger fact(BigInteger n){
        if(EVALUATED_BIG.containsKey(n)){
            return EVALUATED_BIG.get(n);
        }
        if(n.equals(BigInteger.ZERO) || n.equals(BigInteger.ONE)){
            return BigInteger.ONE;
        }
        BigInteger ret = BigInteger.ONE;
        for(BigInteger i = new BigInteger("2"); i.compareTo(n) <= 0; i = i.add(BigInteger.ONE)){
            ret = ret.multiply(i);
        }
        return ret;
    }

    public static BigDecimal eApprox(BigInteger n){
        BigDecimal _n = new BigDecimal(n);
        BigDecimal one_over_n = BigDecimal.ONE.divide(_n, mc);
        return pow(BigDecimal.ONE.add(one_over_n, mc), n);
    }

    public static BigDecimal eX(BigDecimal x, BigInteger n){
        BigDecimal ret = new BigDecimal("0");
        for(BigInteger k = BigInteger.ZERO; k.compareTo(n) <= 0; k = k.add(BigInteger.ONE)){
            BigDecimal term = pow(x, k);
            term = term.divide(new BigDecimal(fact(k)), mc);
            ret = ret.add(term, mc);
        }
        return ret;
    }

    public static BigDecimal e(BigInteger n){
        return eX(BigDecimal.ONE, n);
    }


}
