package com.resnik.util.objects.sort;

import java.util.*;

public class MeanSort extends SortingAlgoritihm<Double>{

    public MeanSort(Double[] elements) {
        super(elements);
    }

    private static Double getMean(Double[] elements){
        double sum = 0.0;
        for(Double d : elements){
            sum += d;
        }
        return sum / elements.length;
    }

    private static Double getRoughMean(Double[] elements){
        return getRoughMean(elements, 10);
    }

    private static Double getRoughMean(Double[] elements, int num){
        if(num >= elements.length || num <= 1){
            return getMean(elements);
        }
        Set<Integer> indices = new LinkedHashSet<>();
        double sum = 0.0;
        while (indices.size() < num){
            int index = (int)(Math.random()*(elements.length - 1));
            if(!indices.contains(index)){
                sum += elements[index];
                indices.add(index);
            }
        }
        return sum / num;
    }

    @Override
    public Double[] sort() {
        // For all less than the mean add to left
        // For all greater than mean add to right
        if(this.elements.length <= 1){
            return this.elements;
        }
        List<Double> less = new ArrayList<>();
        List<Double> greater = new ArrayList<>();
        Double mean = getRoughMean(this.elements);
        for(Double d : this.elements){
            if(d < mean){
                less.add(d);
            }else {
                greater.add(d);
            }
        }
        Double[] lessArray = less.toArray(new Double[less.size()]);
        Double[] greaterArray = greater.toArray(new Double[greater.size()]);
        MeanSort left = new MeanSort(lessArray);
        MeanSort right = new MeanSort(greaterArray);
        // Combine less and greater
        Double[] leftRes = left.sort();
        Double[] rightRes = right.sort();
        List<Double> ret = new ArrayList<>();
        ret.addAll(Arrays.asList(leftRes));
        ret.addAll(Arrays.asList(rightRes));
        return ret.toArray(new Double[ret.size()]);
    }

    public static void main(String[] args) {
        int size = 100;
        Double[] toSort = new Double[size];
        for(int i = 0; i < toSort.length; i++){
            toSort[i] = Math.floor(100 *Math.random());
        }
        MeanSort meanSort = new MeanSort(toSort);
        meanSort.sort();
    }
}
