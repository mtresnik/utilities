package com.resnik.util.images.features;

import java.util.Arrays;

public class Block {

    final Cell[][] cells;

    public Block(Cell[][] cells) {
        this.cells = cells;
    }

    public int[] histogramVector() {
        int[] retArray = new int[cells.length * cells[0].length * cells[0][0].histogram.length];
        int index = 0;
        for (int ROW = 0; ROW < cells.length; ROW++) {
            for (int COL = 0; COL < cells[0].length; COL++) {
                for (int HIST = 0; HIST < cells[0][0].histogram.length; HIST++) {
                    retArray[index] = cells[ROW][COL].histogram[HIST];
                    index++;
                }
            }
        }
        return retArray;
    }

    @Override
    public String toString() {
        return "Block{" + "histVector=" + Arrays.toString(this.histogramVector()) + '}';
    }

}