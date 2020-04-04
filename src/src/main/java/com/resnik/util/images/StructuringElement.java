package com.resnik.util.images;

import com.resnik.util.logger.Log;

import java.util.Arrays;

public final class StructuringElement {

    public static final String TAG = StructuringElement.class.getSimpleName();

    private final int[][] int_representation;
    private final int[][][] vec_representation;
    private final int center_x, center_y;

    private StructuringElement(int[][] int_representation, int center_x, int center_y) {
        this.int_representation = int_representation;
        this.center_x = center_x;
        this.center_y = center_y;
        // System.out.printf("x:%s\ty:%s\n", center_x, center_y);
        this.vec_representation = new int[this.int_representation.length][this.int_representation[0].length][2];
        for (int ROW = 0; ROW < this.int_representation.length; ROW++) {
            for (int COL = 0; COL < this.int_representation[0].length; COL++) {
                this.vec_representation[ROW][COL] = (this.int_representation[ROW][COL] == 1 ? new int[]{COL - center_x, ROW - center_y} : null);
            }
        }
    }

    public void print() {
        for (int i = 0; i < this.int_representation.length; i++) {
            Log.v(TAG,Arrays.toString(this.int_representation[i]));
        }
    }

    public void printVec() {
        for (int ROW = 0; ROW < this.vec_representation.length; ROW++) {
            Log.v(TAG,Arrays.deepToString(this.vec_representation[ROW]));
        }

    }

    public int[][][] applyVectorRepresentation(int[] vector) {
        int[][][] retArray = new int[this.vec_representation.length][this.vec_representation[0].length][];
        for (int ROW = 0; ROW < this.vec_representation.length; ROW++) {
            for (int COL = 0; COL < this.vec_representation[0].length; COL++) {
                int[] currVec = this.vec_representation[ROW][COL];
                retArray[ROW][COL] = (currVec != null ? new int[]{vector[0] + currVec[0], vector[1] + currVec[1]} : null);
            }
        }
        return retArray;
    }

    public static StructuringElement square(int size) {
        size = (size % 2 == 0 ? size + 1 : size);
        int[][] temp_rep = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                temp_rep[i][j] = 1;
            }
        }
        return new StructuringElement(temp_rep, size / 2, size / 2);
    }

    public static StructuringElement diamond(int size) {
        size = (size % 2 == 0 ? size + 1 : size);
        double[] center = new double[]{size / 2, size / 2};
        int[][] temp_rep = new int[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                double dist = Math.abs(row - center[1]) + Math.abs(col - center[0]);
                temp_rep[row][col] = (dist > size / 2 ? 0 : 1);
            }
        }
        return new StructuringElement(temp_rep, size / 2, size / 2);
    }

    public static StructuringElement circle(int radius) {
        int[][] temp_rep = new int[2 * radius + 1][2 * radius + 1];
        double[] center = new double[]{(2 * radius + 1) / 2, (2 * radius + 1) / 2};
        for (int row = 0; row < temp_rep.length; row++) {
            for (int col = 0; col < temp_rep[0].length; col++) {
                double dist = Math.sqrt(Math.pow(center[1] - row, 2) + Math.pow(center[0] - col, 2));
                temp_rep[row][col] = (dist > radius ? 0 : 1);
            }
        }
        return new StructuringElement(temp_rep, (2 * radius + 1) / 2, (2 * radius + 1) / 2);
    }

    public static StructuringElement rect(int height, int width, int c_x, int c_y) {
        int[][] temp_rep = new int[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                temp_rep[row][col] = 1;
            }
        }
        return new StructuringElement(temp_rep, c_x, c_y);
    }

}