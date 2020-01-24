package com.resnik.util.math.numbers;

import com.resnik.util.images.GifDecoder;
import com.resnik.util.images.ImageUtils;
import com.resnik.util.math.MathUtils;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

import static com.resnik.util.math.numbers.ContinuedFraction.*;
import static com.resnik.util.math.numbers.Fibonacci.*;
import static com.resnik.util.math.numbers.Prime.*;

public class TestNumbers {

    @Test
    public void testPrimes() throws IOException {
        System.out.println(Arrays.toString(listPrimeFactors(200)));
        assert (MathUtils.mult(listPrimeFactors(200)) == 200);
        GifDecoder gd = new GifDecoder();
        BufferedImage[] bufferedArray = getEndingsGif(300);
        ImageUtils.saveGifBuffered(bufferedArray, gd, "res/histogram/primes.gif");
    }

    @Test
    public void testFib(){
        System.out.println(Arrays.toString(fibBigArrLinear(100)));
        System.out.println(goldenRatioApproxBig(200000));
        System.out.println(GOLDEN_RATIO_PLUS);
    }

    @Test
    public void testContinuedFraction(){
        System.out.println(Arrays.toString(generateContinuedFraction(new BigDecimal(Double.toString(3.1415926)))));
    }

}
