package com.resnik.util.math.plot.histogram;

import com.resnik.util.images.GifDecoder;
import com.resnik.util.images.ImageUtils;
import com.resnik.util.math.shapes.fractals.PascalGenerator;
import com.resnik.util.objects.structures.CountList;
import com.resnik.util.objects.structures.CountObject;
import javafx.util.Pair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BinaryDistribution {

    // Generate pascal's triangle

    public static Histogram toHistogram(int row){
        Histogram<Double> ret = new Histogram<Double>(640, 360);
        HistogramData<Double> data = new HistogramData<>();
        CountList<Double> countList = new CountList<>();
        int[] pascalRow = PascalGenerator.generateRow(row);
        for(double i = 0; i < pascalRow.length; i++){
            double x = i;
            countList.add(new CountObject<Double>(x, pascalRow[(int)i]));
        }
        countList.setMaintainOrderPair(new Pair(true, new Comparator<CountObject<Double>>() {
            @Override
            public int compare(CountObject<Double> countObject, CountObject<Double> t1) {
                return Double.compare(countObject.getElement(), t1.getElement());
            }
        }));
        countList.sort();
        data = HistogramData.toHistogramData(countList);
        ret.dataset.add("distribution", data);
        ret.colorMap.put("distribution", Color.BLUE);
        return ret;
    }

    public static void saveGif(int NUM_ROWS, String pathName) throws IOException {
        List<BufferedImage> bufferedImages = new ArrayList<>();
        for(int i = 1; i < NUM_ROWS; i ++){
            Histogram<Double> histogram = toHistogram(i);
            BufferedImage bufferedFrame = histogram.getBarsImageBuffered();
            bufferedImages.add(bufferedFrame);
        }
        BufferedImage[] bufferedArray = bufferedImages.toArray(new BufferedImage[bufferedImages.size()]);
        GifDecoder gd = new GifDecoder();
        ImageUtils.saveGifBuffered(bufferedArray, gd, pathName);
    }

    public static void main(String[] args) throws IOException {

    }

}
