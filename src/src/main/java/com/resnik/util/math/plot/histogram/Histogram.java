package com.resnik.util.math.plot.histogram;

import com.resnik.util.images.ImageUtils;
import com.resnik.util.objects.structures.CountList;
import com.resnik.util.objects.structures.CountObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class Histogram<T> {

    final int WIDTH, HEIGHT;
    Color backgroundColor = Color.WHITE;
    static final double AXIS_RATIO_WIDTH = 0.95, AXIS_RATIO_HEIGHT = 0.9;
    static final double BAR_HEIGHT = 0.9;
    final int AXIS_WIDTH_OFFSET, AXIS_HEIGHT_OFFSET;
    Color axisColor = Color.GRAY;
    HistogramDataset<T> dataset;
    Map<String, Color> colorMap;

    public static final int DEFAULT_WIDTH = 200, DEFAULT_HEIGHT= 100;

    public Histogram(){
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public Histogram(int WIDTH, int HEIGHT) {
        this.WIDTH = Math.max(WIDTH, HEIGHT);
        this.HEIGHT = HEIGHT;
        this.AXIS_WIDTH_OFFSET = WIDTH - (int)(AXIS_RATIO_WIDTH*WIDTH);
        this.AXIS_HEIGHT_OFFSET = HEIGHT - (int)(AXIS_RATIO_HEIGHT*HEIGHT);
        this.dataset = new HistogramDataset();
        this.colorMap = new LinkedHashMap<>();
    }

    public Histogram(CountList<T> countList, Color repColor){
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT, countList, repColor);
    }

    public Histogram(CountList<T> countList){
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT, countList);
    }

    public Histogram(int WIDTH, int HEIGHT, CountList<T> countList){
        this(WIDTH, HEIGHT);
        HistogramData<T> data = HistogramData.toHistogramData(countList);
        this.add("dataset1", data);
    }

    public Histogram(int WIDTH, int HEIGHT, CountList<T> countList, Color repColor){
        this(WIDTH, HEIGHT, countList);
        this.colorMap.replace("dataset1", repColor);
    }

    public Histogram<T> setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public Histogram<T> setAxisColor(Color axisColor) {
        this.axisColor = axisColor;
        return this;
    }

    public Histogram<T> add(String name, HistogramData<T> data){
        HistogramData<T> rem = this.dataset.getOrDefault(name, new HistogramData<T>()).yoke(data);
        this.dataset.put(name, rem);
        this.colorMap.put(name, HistogramDataset.RAND_COLOR());
        return this;
    }

    private byte[][][] getBackgroundImage(){
        byte[][][] retImage = new byte[HEIGHT][WIDTH][];
        retImage = ImageUtils.fillImage(retImage, backgroundColor);
        return retImage;
    }

    private BufferedImage getBackgroundImageBuffered(){
        BufferedImage retImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        retImage = ImageUtils.fillImage(retImage, backgroundColor);
        return retImage;
    }

    private byte[][][] getAxisImage(){
        byte[][][] retImage = this.getBackgroundImage();
        retImage = ImageUtils.drawBox(retImage, axisColor, false, this.axis_y(), this.axis_x());
        return retImage;
    }

    private BufferedImage getAxisImageBuffered(){
        BufferedImage retImage = this.getBackgroundImageBuffered();
        retImage = ImageUtils.drawBox(retImage, axisColor, false, this.axis_y(), this.axis_x());
        return retImage;
    }

    public byte[][][] getBarsImage(){
        byte[][][] retImage = this.getAxisImage();
        // Get the max count of all bars and divide each bar by the max count
        double maxCount = this.dataset.maxCount();
        // This is the scalar used when finding out all bars heights
        // Draw each bar based on how much space the bargroup takes up
        BarCollection<T>[] collections = this.getBarCollections();

        double axisWidth = this.axisWidth();
        int offset = 1;
        double collectionWidth = axisWidth / collections.length - offset;
        double barWidth = collectionWidth / this.dataset.numDatasets();
        double currX = this.AXIS_WIDTH_OFFSET;
        for(BarCollection<T> collection : collections){
            for(Map.Entry<String, Integer> entry : collection.keyMap.entrySet()){
                double height = Math.max((entry.getValue() / maxCount)*axisHeight() - 2, 0);
                int[] xBounds = new int[]{(int)currX + offset, (int)(currX + barWidth)};
                int[] yBounds = new int[]{(int)(Math.max(this.axis_y()[0], this.axis_y()[1]) - height), (int)(Math.max(this.axis_y()[0], this.axis_y()[1])) - 1};
                if(height == 0){
                    yBounds[0] -= 1;
                }
                retImage = ImageUtils.drawBox(retImage, this.colorMap.get(entry.getKey()), true, yBounds, xBounds);
                currX += barWidth;
            }
            currX += 1;
        }

        return retImage;
    }

    public BufferedImage getBarsImageBuffered(){
        BufferedImage retImage = this.getAxisImageBuffered();
        // Get the max count of all bars and divide each bar by the max count
        double maxCount = this.dataset.maxCount();
        // This is the scalar used when finding out all bars heights
        // Draw each bar based on how much space the bargroup takes up
        BarCollection<T>[] collections = this.getBarCollections();

        double axisWidth = this.axisWidth();
        int offset = 1;
        double collectionWidth = axisWidth / collections.length - offset;
        double barWidth = collectionWidth / this.dataset.numDatasets();
        double currX = this.AXIS_WIDTH_OFFSET;
        for(BarCollection<T> collection : collections){
            for(Map.Entry<String, Integer> entry : collection.keyMap.entrySet()){
                double height = Math.max((entry.getValue() / maxCount)*axisHeight() - 2, 0);
                int[] xBounds = new int[]{(int)currX + offset, (int)(currX + barWidth)};
                int[] yBounds = new int[]{(int)(Math.max(this.axis_y()[0], this.axis_y()[1]) - height), (int)(Math.max(this.axis_y()[0], this.axis_y()[1])) - 1};
                if(height == 0){
                    yBounds[0] -= 1;
                }
                retImage = ImageUtils.drawBox(retImage, this.colorMap.get(entry.getKey()), true, yBounds, xBounds);
                currX += barWidth;
            }
            currX += 1;
        }

        return retImage;
    }

    private int getBarWidth(){
        throw new UnsupportedOperationException();
    }

    private int tickWidth() {
        int numBars = this.dataset.size();
        int numDatasets = this.dataset.numDatasets();
        int numBarsDatasets = numBars * numDatasets;
        throw new UnsupportedOperationException();
    }

    private int[] getTicks(){
        int numBars = this.dataset.size();
        throw new UnsupportedOperationException();
    }

    private int[] axis_x(){
        return new int[]{AXIS_WIDTH_OFFSET, (int)(AXIS_RATIO_WIDTH*WIDTH)};
    }


    private int[] axis_y(){
        return new int[]{AXIS_HEIGHT_OFFSET, (int)(AXIS_RATIO_HEIGHT*HEIGHT)};
    }

    private int axisWidth(){
        return (int)(AXIS_RATIO_WIDTH*WIDTH) - AXIS_WIDTH_OFFSET;
    }

    private int axisHeight(){
        return (int)(AXIS_RATIO_HEIGHT*HEIGHT) - AXIS_HEIGHT_OFFSET;
    }

    private static class BarCollection<T> {
        final T key;
        final Map<String, Integer> keyMap = new LinkedHashMap<>();

        public BarCollection(T key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return "BarCollection{" +
                    "key=" + key +
                    ", keyMap=" + keyMap +
                    '}';
        }
    }

    private java.util.List<T> uniqueKeys(){
        java.util.List<T> allKeys = new ArrayList();
        for(Map.Entry<String, HistogramData<T>> entry : this.dataset){
            String dataName = entry.getKey();
            HistogramData<T> currData = entry.getValue();
            for(int i = 0; i < currData.size(); i++){
                CountObject<T> currCount = currData.get(i);
                if(!allKeys.contains(currCount.getElement())){
                    allKeys.add(currCount.getElement());
                }
            }
        }
        return allKeys;
    }

    private BarCollection<T>[] getBarCollections(){
        java.util.List<T> allKeys = uniqueKeys();
        BarCollection<T>[] retArray = new BarCollection[allKeys.size()];
        for(int i = 0; i < allKeys.size(); i++){
            T currKey = allKeys.get(i);
            BarCollection<T> currBar = new BarCollection<T>(currKey);
            for(Map.Entry<String, HistogramData<T>> entry : this.dataset){
                String dataName = entry.getKey();
                HistogramData<T> currData = entry.getValue();
                currBar.keyMap.put(dataName, currData.getCountOr(currKey, 0));
            }
            retArray[i] = currBar;
        }
        return retArray;
    }

    public static Histogram<Double> fromGaussian(double mean, double stdDev, int n){
        Random random = new Random();
        double min = mean - 3*stdDev;
        double max = mean + 3*stdDev;
        double[] data = new double[n];
        for(int i = 0; i < n; i++){
            data[i] = random.nextGaussian()*stdDev + mean;
        }
        HistogramData<Double> histData = HistogramData.fromArray(data, min, max, 0.05);
        Histogram<Double> retHist = new Histogram<>(1280,720);
        retHist.add("gaussian", histData);
        retHist.colorMap.put("gaussian", Color.BLUE);
        return retHist;
    }

    public static Histogram<Double>[] fromGaussians(double mean, double stdDev, int min_n, int max_n, int stride){
        Histogram<Double>[] retArray = new Histogram[(max_n - min_n) / stride];
        int index = 0;
        for(int n = min_n; n < max_n; n+=stride){
            Histogram<Double> curr = fromGaussian(mean, stdDev, n);
            retArray[index] = curr;
            index++;
        }
        return retArray;
    }

    public static Histogram<Integer> fromRandomInts(){
        int length = 100;
        Histogram<Integer> histogram = new Histogram<>(600,300);
        HistogramData<Integer> histogramData = new HistogramData<>();
        for(int i = 0; i < length; i++){
            histogramData.addElement(i, (int)(Math.random()*length));
        }
        histogram.dataset.add("index", histogramData);
        histogram.colorMap.put("index", Color.BLUE);
        return histogram;
    }


    public void putColor(String name, Color color){
        this.colorMap.put(name, color);
    }

}
