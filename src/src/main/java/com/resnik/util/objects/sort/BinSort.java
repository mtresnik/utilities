package com.resnik.util.objects.sort;

import com.resnik.util.logger.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BinSort extends SortingAlgoritihm<Double>{

    private int binSize;

    public BinSort(Double[] elements, int binSize) {
        super(elements);
        this.binSize = binSize;
    }

    public BinSort(Double[] elements) {
        this(elements, 2000);
    }

    public static class SortList extends ArrayList<Double>{

        public boolean add(Double d){
            super.add(d);
            Collections.sort(this);
            return true;
        }

    }

    public static boolean isSorted(List<Double> doubles){
        if(doubles.size() == 1){
            return true;
        }
        for(int i = 0; i < doubles.size() - 1; i++){
            if(doubles.get(i + 1) < doubles.get(i)){
                return false;
            }
        }
        return true;
    }

    public static boolean isSorted(Double[] doubles){
        if(doubles.length == 1){
            return true;
        }
        for(int i = 0; i < doubles.length - 1; i++){
            if(doubles[i + 1] < doubles[i]){
                return false;
            }
        }
        return true;
    }


    @Override
    public Double[] sort() {
        if(this.elements.length <= 1){
            return this.elements;
        }
        if(isSorted(this.elements)){
            return this.elements;
        }
        if(this.elements.length == 2){
            if(this.elements[0] > this.elements[1]){
                return new Double[]{this.elements[1], this.elements[0]};
            }
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
        // Separate based on unique values
        int size = Math.min(binSize, this.elements.length);
        List<Double>[] similarLists = new List[size];
        // O(n)
        int maxIndex = similarLists.length - 1;
        int currIndex;
        for(i = 0; i < this.elements.length; i++){
            currIndex = (int)(Math.round(((this.elements[i] - min) / spread) * maxIndex));
            if(similarLists[currIndex] == null) {
                similarLists[currIndex] = new ArrayList<>();
            }
            similarLists[currIndex].add(this.elements[i]);
        }
        // O(n)
        // Get next non-null reference and length
        int index = 0;
        List<Double> similarList;
        for(i = 0; i < size; i++){
            similarList = similarLists[i];
            if(similarList != null){
                if(!isSorted(similarList)){
                    similarList.sort(Double::compareTo);
                }
                for (Double aDouble : similarList) {
                    this.elements[index] = aDouble;
                    index++;
                }
            }
        }
        return this.elements;
    }

    public static void main(String[] args) {
        int size = 500_000;
        Double[] toSort = new Double[size];
        for(int i = 0; i < toSort.length; i++){
            toSort[i] = size *Math.random();
        }
        Double[] internal = Arrays.copyOf(toSort, toSort.length);
        long start = System.currentTimeMillis();
        BinSort minSort = new BinSort(internal);
        Double[] sorted = minSort.sort();
        Log.d("BinSort", "Time:" + (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        Arrays.sort(toSort);
        Log.d("BinSort", "Time:" + (System.currentTimeMillis() - start));
        Log.d("BinSort", "Equals:" + Arrays.equals(toSort, sorted));
        // Log.d("BinSort", "Sorted:" + Arrays.toString(sorted));
        // Log.d("BinSort", "Java:" + Arrays.toString(toSort));
        for(int i = 0; i < size; i++){
            if(!sorted[i].equals(toSort[i])){
                // System.out.printf("Error %s!=%s\n", sorted[i], toSort[i]);
            }
        }
    }
}
