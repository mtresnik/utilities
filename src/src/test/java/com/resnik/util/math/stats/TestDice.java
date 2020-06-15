package com.resnik.util.math.stats;

import com.resnik.util.images.ImageUtils;
import com.resnik.util.logger.Log;
import com.resnik.util.math.plot.histogram.Histogram;
import com.resnik.util.math.plot.histogram.HistogramData;
import com.resnik.util.objects.structures.CountObject;
import org.junit.Test;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class TestDice {


    @Test
    public void testDice6Perm(){
        int min = 1;
        int max = 10;
        int numDice = 2;
        int maxRoll = max * numDice;
        Map<Integer, List<int[]>> permMap = new LinkedHashMap<>();
        Map<Integer, List<int[]>> combMap = new LinkedHashMap<>();
        // iterate through all possible combinations
        for(int die1 = min; die1<=max; die1++){
            for(int die2 = min; die2<=max; die2++){
                int sum = die1 + die2;
                int[] test = new int[]{die1, die2};
                permMap.putIfAbsent(sum, new ArrayList<>());
                permMap.get(sum).add(test);
                combMap.putIfAbsent(sum, new ArrayList<>());
                boolean contains = false;
                for(int[] present : combMap.get(sum)){
                    if(Arrays.equals(present, test) || (test[1] == present[0] && test[0] == present[1])){
                        contains = true;
                        break;
                    }
                }
                if(contains){
                    continue;
                }
                combMap.get(sum).add(test);

            }
        }
        HistogramData<Integer> permData = new HistogramData<>();
        for(Map.Entry<Integer, List<int[]>> entry : permMap.entrySet()){
            CountObject<Integer> countObject = new CountObject<Integer>(entry.getKey(), entry.getValue().size());
            permData.add(countObject);
        }

        HistogramData<Integer> combData = new HistogramData<>();
        for(Map.Entry<Integer, List<int[]>> entry : combMap.entrySet()){
            CountObject<Integer> countObject = new CountObject<Integer>(entry.getKey(), entry.getValue().size());
            combData.add(countObject);
        }

        Histogram<Integer> histogram = new Histogram<>();
        histogram.add("perm", permData);
        histogram.add("comb", combData);
        byte[][][] image = histogram.getBarsImage();
        try {
            ImageUtils.saveImageBytes(image, "src/res/testdice.bmp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int[] getLikelyMap(int numRolls, int numBins){
        int[] likelyMap = new int[numBins*2 + 1];
        for(int i = 0; i < numRolls; i++){
            double d1 = Math.random();
            int b1 = (int)(d1*numBins) + 1;
            for(int j = 0; j < numRolls; j++){
                double d2 = Math.random();
                int b2 = (int)(d2*numBins) + 1;
                int sum = b1 + b2;
                likelyMap[sum]++;
            }
        }
        return likelyMap;
    }

    private int[] getLikelyMap3(int numRolls, int numBins){
        int[] likelyMap = new int[numBins*3 + 1];
        for(int i = 0; i < numRolls; i++){
            double d1 = Math.random();
            int b1 = (int)(d1*numBins) + 1;
            for(int j = 0; j < numRolls; j++){
                double d2 = Math.random();
                int b2 = (int)(d2*numBins) + 1;
                for(int k = 0; k < numRolls; k++){
                    double d3 = Math.random();
                    int b3 = (int)(d3*numBins) + 1;
                    int sum = b1 + b2 + b3;
                    likelyMap[sum]++;
                }
            }
        }
        return likelyMap;
    }

    private int[] getLikelyMap4(int numRolls, int numBins){
        int[] likelyMap = new int[numBins*4 + 1];
        for(int i = 0; i < numRolls; i++){
            double d1 = Math.random();
            int b1 = (int)(d1*numBins) + 1;
            for(int j = 0; j < numRolls; j++){
                double d2 = Math.random();
                int b2 = (int)(d2*numBins) + 1;
                for(int k = 0; k < numRolls; k++){
                    double d3 = Math.random();
                    int b3 = (int)(d3*numBins) + 1;
                    for(int l = 0; l < numRolls; l++){
                        double d4 = Math.random();
                        int b4 = (int)(d4*numBins) + 1;
                        int sum = b1 + b2 + b3 + b4;
                        likelyMap[sum]++;
                    }
                }
            }
        }
        return likelyMap;
    }

    private int[] superEpicLiklihoodMap(int numRolls, int numBins, int dice){
        int[] likelyMap = new int[numBins*dice + 1];
        for (int i = 0; i < numRolls; i++) {
            int sum = 0;
            for (int j = 0; j < dice; j++) {
                double d1 = Math.random();
                int b1 = (int)(d1*numBins) + 1;
                sum+= b1;
            }
            likelyMap[sum]++;
            Log.e("TestDice", "NumRolls:" + i);
        }
        return likelyMap;
    }

    @Test
    public void testRandDist(){
        int numRolls = 200000;
        int numBins = 6;
        int numDice = 4;

        int[] likelyMap = superEpicLiklihoodMap(numRolls, numBins, numDice);

        HistogramData<Integer> permData = new HistogramData<>();
        for(int index = numDice; index < likelyMap.length; index++){
            CountObject<Integer> countObject = new CountObject<Integer>(index, likelyMap[index]);
            permData.add(countObject);
        }

        Log.e("TestDice", "Made permData");

        Histogram<Integer> histogram = new Histogram<>(1280, 720);
        histogram.add("perm", permData);
        byte[][][] image = histogram.getBarsImage();
        try {
            ImageUtils.saveImageBytes(image, "src/res/randdice.bmp");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
