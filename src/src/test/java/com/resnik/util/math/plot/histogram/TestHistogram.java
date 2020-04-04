package com.resnik.util.math.plot.histogram;

import com.resnik.util.images.GifDecoder;
import com.resnik.util.images.ImageUtils;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.resnik.util.math.plot.histogram.Histogram.*;
import static com.resnik.util.math.plot.histogram.HistogramData.fromArray;

public class TestHistogram {

    @Test
    public void testHistogram() throws IOException {
        Histogram<Integer> integerHistogram = fromRandomInts();
        ImageUtils.saveImageBuffered(integerHistogram.getBarsImageBuffered(), "src/res/histogram/randints.png");
    }

    @Test
    public void testHistogramData(){
        fromArray(new double[]{0,0.5,1.0,1.5,2.0}, 0, 5, 0.5);
    }

    @Test
    public void fromGaussianGif() throws IOException {
        double mean = 0;
        double stdDev = 0.25;
        int min_n = 200;
        int max_n=400;
        int stride = 1;
        List<BufferedImage> bufferedImages = new ArrayList<>();
        Histogram<Double>[] histograms = fromGaussians(mean, stdDev, min_n, max_n, stride);
        for(int i = 0; i < histograms.length; i ++){
            Histogram<Double> histogram = histograms[i];
            BufferedImage bufferedFrame = histogram.getBarsImageBuffered();
            bufferedImages.add(bufferedFrame);
        }
        BufferedImage[] bufferedArray = bufferedImages.toArray(new BufferedImage[bufferedImages.size()]);
        GifDecoder gd = new GifDecoder();
        ImageUtils.saveGifBuffered(bufferedArray, gd, "src/res/histogram/gaussian.gif");
    }

    @Test
    public void testBinaryGif() throws IOException{
        // Any more than 20 can throw a integer overflow
        BinaryDistribution.saveGif(20,"src/res/histogram/binary.gif");
    }

}
