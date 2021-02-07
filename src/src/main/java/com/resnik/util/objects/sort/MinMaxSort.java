package com.resnik.util.objects.sort;

import com.resnik.util.logger.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


// ~ 0.5*x^2 complexity
public class MinMaxSort extends SortingAlgoritihm<Double>{

    public MinMaxSort(Double[] elements) {
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
            double max = Double.MIN_VALUE;
            int minIndex = -1;
            int maxIndex = -1;
            for(int j = 0; j < toSort.size(); j++){
                double curr = toSort.get(j);
                if(curr < min){
                    min = curr;
                    minIndex = j;
                }
                num++;
            }
            ret[i] = toSort.remove(minIndex);
            for(int j = 0; j < toSort.size(); j++){
                double curr = toSort.get(j);
                if(curr > max){
                    max = curr;
                    maxIndex = j;
                }
                num++;
            }
            ret[this.elements.length - i - 1] = toSort.remove(maxIndex);
            i++;
        }
        Log.d("MinMaxSort", num);
        return ret;
    }

    public static void main(String[] args) {
        int size = 40;
        Double[] toSort = new Double[size];
        for(int i = 0; i < toSort.length; i++){
            toSort[i] = Math.floor(100 *Math.random());
        }
        MinMaxSort minSort = new MinMaxSort(toSort);
        Double[] sorted = minSort.sort();
        Log.d("MinMaxSort", sorted);
        Arrays.sort(toSort);
        Log.d("MinMaxSort", "Equals:" + Arrays.equals(toSort, sorted));
    }

}
