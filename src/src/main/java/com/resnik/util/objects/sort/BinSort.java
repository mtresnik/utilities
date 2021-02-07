package com.resnik.util.objects.sort;

import com.resnik.util.logger.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BinSort extends SortingAlgoritihm<Double>{

    public BinSort(Double[] elements) {
        super(elements);
    }

    @Override
    public Double[] sort() {
        if(this.elements.length <= 1){
            return this.elements;
        }
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        int i;
        // O(n)
        for(i = 0; i < this.elements.length; i++){
            min = Math.min(min, this.elements[i]);
            max = Math.max(max, this.elements[i]);
        }
        double spread = max - min;
        List<Double>[] bins = new List[this.elements.length];
        // O(n)
        for(i = 0; i < this.elements.length; i++){
            bins[i] = new ArrayList<>();
        }
        // O(n)
        int maxIndex = this.elements.length - 1;
        for(i = 0; i < this.elements.length; i++){
            bins[(int)(((this.elements[i] - min) / spread) * maxIndex)].add(this.elements[i]);
        }
        Double[] ret = new Double[this.elements.length];
        i = 0;
        // O(n)
        for(List<Double> bin : bins){
            for(Double x : bin){
                ret[i] = x;
                i++;
            }
        }
        return ret;
    }

    public static void main(String[] args) {
        int size = 1_500_000;
        Double[] toSort = new Double[size];
        for(int i = 0; i < toSort.length; i++){
            toSort[i] = Math.floor(size *Math.random());
        }
        long start = System.currentTimeMillis();
        BinSort minSort = new BinSort(toSort);
        Double[] sorted = minSort.sort();
        Log.d("BinSort", "Time:" + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        Arrays.sort(toSort);
        Log.d("BinSort", "Time:" + (System.currentTimeMillis() - start));
        Log.d("BinSort", "Equals:" + Arrays.equals(toSort, sorted));
    }
}
