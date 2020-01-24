package com.resnik.util.images.features;

import java.util.Arrays;

public class Cell {

    public final int[] histogram;

    public Cell(int[] histogram) {
        this.histogram = histogram;
    }

    @Override
    public String toString() {
        return "Cell{" + "histogram=" + Arrays.toString(histogram) + '}';
    }

}