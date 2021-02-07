package com.resnik.util.objects.sort;

import com.resnik.util.logger.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// ~ 0.5*x^2 complexity
public class MinSort extends SortingAlgoritihm<Double>{

    public MinSort(Double[] elements) {
        super(elements);
    }

    @Override
    public Double[] sort() {
        if(this.elements.length <= 1){
            return this.elements;
        }
        Double[] ret = new Double[this.elements.length];
        List<Double> toSort = new ArrayList<>(Arrays.asList(this.elements));
        int i = 0;
        double num = 0;
        while (!toSort.isEmpty()){
            double min = Double.MAX_VALUE;
            int minIndex = -1;
            for(int j = 0; j < toSort.size(); j++){
                double curr = toSort.get(j);
                if(curr < min){
                    min = curr;
                    minIndex = j;
                }
                num++;
            }
            ret[i] = toSort.remove(minIndex);
            i++;
        }
        Log.d("MinSort", num);
        return ret;
    }

    public static void main(String[] args) {
        int size = 100;
        Double[] toSort = new Double[size];
        for(int i = 0; i < toSort.length; i++){
            toSort[i] = Math.floor(100 *Math.random());
        }
        MinSort minSort = new MinSort(toSort);
        Double[] sorted = minSort.sort();
        Log.d("MinSort", sorted);
    }
}
