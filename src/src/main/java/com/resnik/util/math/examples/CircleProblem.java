package com.resnik.util.math.examples;

import com.resnik.util.logger.Log;
import com.resnik.util.math.numbers.BigNumberUtils;
import com.resnik.util.math.numbers.ContinuedFraction;
import com.resnik.util.math.numbers.Factorial;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public final class CircleProblem {

    public static final String TAG = CircleProblem.class.getSimpleName();

    public static final MathContext mc = new MathContext(32, RoundingMode.HALF_EVEN);
    public static final BigInteger DEFAULT_CAP = new BigInteger("1000");
    public static final BigDecimal A = A(DEFAULT_CAP);

    public static final Map<Integer, BigDecimal> W_MAP = new LinkedHashMap<>();
    public static final Map<Integer, BigDecimal> W_test_MAP = new LinkedHashMap<>();
    public static Map<BigInteger, BigDecimal> a_MAP = new LinkedHashMap<>();
    public static Map<BigInteger, BigDecimal> r_MAP = new LinkedHashMap<>();
    public static Map<BigInteger, BigDecimal> R_product_MAP = new LinkedHashMap<>();
    public static Map<BigInteger, BigDecimal> R_sum_MAP = new LinkedHashMap<>();

    private CircleProblem(){}

    public static BigDecimal a(BigInteger n){
        if(a_MAP == null){
            a_MAP = new LinkedHashMap<>();
        }
        if(a_MAP.containsKey(n)){
            return a_MAP.get(n);
        }
        BigDecimal numerator = new BigDecimal(Factorial.fact(n.multiply(new BigInteger("2"))));
        BigDecimal denominator = BigNumberUtils.pow(new BigDecimal("2"), n.multiply(new BigInteger("2")).add(BigInteger.ONE));
        denominator = denominator.multiply(BigNumberUtils.pow(new BigDecimal(Factorial.fact(n)), new BigInteger("2")), mc);
        denominator = denominator.multiply(new BigDecimal("-2").multiply(new BigDecimal(n), mc).add(BigDecimal.ONE, mc),mc);
        denominator = denominator.multiply(new BigDecimal("2").multiply(new BigDecimal(n), mc).add(BigDecimal.ONE, mc),mc);
        BigDecimal ret = numerator.divide(denominator, mc);
        a_MAP.put(n, ret);
        return ret;
    }

    public static BigDecimal A(BigInteger cap){
        BigDecimal ret = BigDecimal.ZERO;
        for(BigInteger index = BigInteger.ZERO; index.compareTo(cap) < 0; index = index.add(BigInteger.ONE)){
            ret = ret.add(a(index), mc);
        }
        return ret;
    }

    public static BigDecimal w(BigInteger cap, int recurr){
        if(recurr == 0){
            return A;
        }
        if(W_MAP.containsKey(recurr)){
            return W_MAP.get(recurr);
        }
        BigDecimal sum = BigDecimal.ZERO;
        for(BigInteger index = BigInteger.ZERO; index.compareTo(cap) < 0; index = index.add(BigInteger.ONE)){
            BigDecimal curr_a = a(index);
            BigDecimal w_2n = BigNumberUtils.pow(w(cap, recurr - 1), new BigInteger("2").multiply(index));
            sum = sum.add(curr_a.multiply(w_2n, mc), mc);
        }
        W_MAP.put(recurr, sum);
        return sum;
    }

    public static BigDecimal r(BigInteger n){
        if(r_MAP.containsKey(n)){
            return r_MAP.get(n);
        }
        BigDecimal N = new BigDecimal(n);
        BigDecimal TWO = new BigDecimal("2");
        BigDecimal numerator = TWO.multiply(N).add(BigDecimal.ONE, mc);
        numerator = numerator.multiply(BigDecimal.ONE.subtract(TWO.multiply(N, mc)), mc);
        BigDecimal denominator = TWO.multiply(N.add(BigDecimal.ONE, mc), mc);
        denominator = denominator.multiply(TWO.multiply(N, mc).add(new BigDecimal("3")), mc);
        denominator = denominator.multiply(new BigDecimal("-1"), mc);
        BigDecimal ret = numerator.divide(denominator, mc);
        r_MAP.put(n, ret);
        return ret;
    }

    public static BigDecimal R(BigInteger cap){
        if(R_product_MAP.containsKey(cap)){
            return R_product_MAP.get(cap);
        }
        if(R_product_MAP.containsKey(cap.subtract(BigInteger.ONE)) && R_product_MAP.containsKey(cap.subtract(new BigInteger("2")))){
            BigDecimal ret = R_product_MAP.get(cap.subtract(BigInteger.ONE)).multiply(R_product_MAP.get(cap.subtract(new BigInteger("2"))), mc);
            R_product_MAP.put(cap, ret);
        }
        BigDecimal product = BigDecimal.ONE;
        for(BigInteger index = BigInteger.ZERO; index.compareTo(cap) < 0; index = index.add(BigInteger.ONE)){
            BigDecimal curr = r(index);
            product = product.multiply(curr, mc);
            R_product_MAP.put(index, product);
        }
        R_product_MAP.put(cap, product);
        return product;
    }

    public static BigDecimal RSum(BigInteger cap){
        if(R_sum_MAP.containsKey(cap)){
            return R_sum_MAP.get(cap);
        }
        BigDecimal sum = BigDecimal.ZERO;
        for(BigInteger index = BigInteger.ZERO; index.compareTo(cap) < 0; index = index.add(BigInteger.ONE)){
            BigDecimal curr = R(index);
            sum = sum.add(curr, mc);
            R_sum_MAP.put(index, sum);
        }
        R_sum_MAP.put(cap, sum);
        return sum;
    }

    public static BigDecimal generateAFromR(BigInteger cap){
        BigDecimal a0 = a(BigInteger.ZERO);
        BigDecimal ret = a0.multiply(BigDecimal.ONE.add(RSum(cap),mc),mc);
        return ret;
    }

    public static BigDecimal wFromR(BigInteger cap, int recurr){
        if(recurr == 0){
            return generateAFromR(cap);
        }
        BigDecimal sum = BigDecimal.ZERO;
        for(BigInteger index = BigInteger.ZERO; index.compareTo(cap) < 0; index = index.add(BigInteger.ONE)){
            BigDecimal curr_a = a(index);
            BigDecimal w_2n = BigNumberUtils.pow(wFromR(cap, recurr - 1), new BigInteger("2").multiply(index));
            sum = sum.add(curr_a.multiply(w_2n, mc), mc);
        }
        return sum;
    }

    public static BigDecimal wFromTest(BigInteger cap, BigDecimal inner, int recurr){
        if(recurr == 0){
            return inner;
        }
        if(W_test_MAP.containsKey(recurr)){
            return W_test_MAP.get(recurr);
        }
        BigDecimal sum = BigDecimal.ZERO;
        for(BigInteger index = BigInteger.ZERO; index.compareTo(cap) < 0; index = index.add(BigInteger.ONE)){
            BigDecimal curr_a = a(index);
            BigDecimal w_2n = BigNumberUtils.pow(wFromTest(cap, inner,recurr - 1), new BigInteger("2").multiply(index));
            sum = sum.add(curr_a.multiply(w_2n, mc), mc);
        }
        W_test_MAP.put(recurr, sum);
        return sum;
    }

    public static void main(String[] args) {
        Log.v(TAG,r(new BigInteger("0")));
        Log.v(TAG,R(new BigInteger("12")));
        Log.v(TAG,generateAFromR(new BigInteger("3000")));
        Log.v(TAG,A);
        BigDecimal w = wFromTest(DEFAULT_CAP, new BigDecimal("0"), 20);
        BigInteger[] frac = ContinuedFraction.generateContinuedFraction(w);
        Log.v(TAG,W_test_MAP);
        Log.v(TAG, "Frac:" + Arrays.asList(frac));
    }

}
