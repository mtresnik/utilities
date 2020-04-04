package com.resnik.util.math.numbers;

import com.resnik.util.images.GifDecoder;
import com.resnik.util.images.ImageUtils;
import com.resnik.util.logger.Log;
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

    public static final String TAG = TestNumbers.class.getSimpleName();

    @Test
    public void testPrimes() throws IOException {
        Log.v(TAG,Arrays.toString(listPrimeFactors(200)));
        assert (MathUtils.mult(listPrimeFactors(200)) == 200);
        GifDecoder gd = new GifDecoder();
        BufferedImage[] bufferedArray = getEndingsGif(300);
        ImageUtils.saveGifBuffered(bufferedArray, gd, "src/res/histogram/primes.gif");
    }

    @Test
    public void testFib(){
        Log.v(TAG,Arrays.toString(fibBigArrLinear(100)));
        Log.v(TAG,goldenRatioApproxBig(200000));
        Log.v(TAG,GOLDEN_RATIO_PLUS);
    }

    @Test
    public void testContinuedFraction(){
        Log.v(TAG,Arrays.toString(generateContinuedFraction(new BigDecimal(Double.toString(3.1415926)))));
    }

}
