package com.resnik.util.math.stats;

import com.resnik.util.images.ImageUtils;
import com.resnik.util.logger.Log;
import com.resnik.util.math.plot.histogram.Histogram;
import com.resnik.util.math.plot.histogram.HistogramData;
import com.resnik.util.serial.csv.CSV;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestStock {


    public static final String TAG = TestStock.class.getSimpleName();

    @Test
    public void testGoogle(){
        try {
            CSV file = CSV.load("src/res/AMZN.csv");
            Log.d(TAG, file.getColTypes());
            List<Object> dates = file.getColumn(0);
            List<Object> opens = file.getColumn(1);
            List<Object> closes = file.getColumn(4);

            double[] percentChanges = new double[Math.min(opens.size(), closes.size())];
            double minPercent = Double.MAX_VALUE;
            double maxPercent = -Double.MAX_VALUE;
            for(int index = 0; index < opens.size() && index < closes.size(); index++){
                Object open = opens.get(index);
                Object close = closes.get(index);
                if(close instanceof Number && open instanceof Number){
                    double openVal = ((Number) open).doubleValue();
                    double closeVal = ((Number) close).doubleValue();
                    double delta = closeVal - openVal;
                    double percent = 100*delta / openVal;
                    percentChanges[index] = percent;
                    minPercent = Math.min(minPercent, percent);
                    maxPercent = Math.max(maxPercent, percent);
                }
            }
            HistogramData data = HistogramData.fromArray(percentChanges, minPercent, maxPercent, 0.5);
            Histogram histogram = new Histogram(1280, 720);
            histogram.add("amazon", data);
            byte[][][] retImage = histogram.getBarsImage();
            ImageUtils.saveImageBytes(retImage, "src/res/amazon.bmp");
            Log.d(TAG, "min:" + minPercent);
            Log.d(TAG, "max:" + maxPercent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
