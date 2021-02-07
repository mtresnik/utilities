package com.resnik.util.objects.arrays;

import com.resnik.util.images.GifDecoder;
import com.resnik.util.images.ImageUtils;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestSort {

    @Test
    public void testSortImage(){
        // Greyscale
        final int size = 64;
        List<BufferedImage> images = new ArrayList<>();
        double[] toSort = new double[size*size];
        for(int i = 0; i < toSort.length; i++){
            toSort[i] = Math.random();
        }
        QuickSort quickSort = new QuickSort(toSort);
        quickSort.sort();
    }

    @Test
    public void testSortRowCol(){
        final int size = 100;
        List<BufferedImage> images = new ArrayList<>();
        double[] toSort = new double[size*size];
        for(int i = 0; i < toSort.length; i++){
            toSort[i] = Math.random();
        }
        images.add(ImageUtils.bytesToBufferedImage(fromGreyScale(toSort)));
        for(int COL = 0; COL< size; COL++){
            sortCol(toSort, COL);
            images.add(ImageUtils.bytesToBufferedImage(fromGreyScale(toSort)));
        }
        for(int ROW = 0; ROW< size; ROW++){
            sortRow(toSort, ROW);
            images.add(ImageUtils.bytesToBufferedImage(fromGreyScale(toSort)));
        }
        GifDecoder gd = new GifDecoder();
        gd.loopCount = 0;
        try {
            ImageUtils.saveGifBuffered(images.toArray(new BufferedImage[0]), gd, "src/res/rowcoltest.gif");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static byte[][][] fromGreyScale(double[] arr){
        int width = (int) Math.sqrt(arr.length);
        int height = width;
        byte[][][] retImage = new byte[height][width][];
        for(int ROW = 0; ROW < height; ROW++){
            for(int COL = 0; COL < width; COL++){
                int index = ROW*width + COL;
                retImage[ROW][COL] = ImageUtils.grey(arr[index]);
            }
        }
        return retImage;
    }

    private static void sortRow(double[] input, int ROW){
        int width = (int) Math.sqrt(input.length);
        double[] rowClone = Arrays.copyOfRange(input, ROW*width, ROW*width + width);
        Arrays.sort(rowClone);
        for(int COL = 0; COL < width; COL++){
            int index = ROW*width + COL;
            input[index] = rowClone[COL];
        }
    }

    private static void sortCol(double[] input, int COL){
        int width = (int) Math.sqrt(input.length);
        double[] colClone = new double[width];
        for(int ROW = 0; ROW < width; ROW++){
            colClone[ROW] = input[ROW*width + COL];
        }
        Arrays.sort(colClone);
        for(int ROW = 0; ROW < width; ROW++){
            input[ROW*width + COL] = colClone[ROW];
        }
    }

    private static void swap(double[] input, int index1, int index2){
        double temp = input[index1];
        input[index1] = input[index2];
        input[index2] = temp;
    }

    public static class QuickSort{

        int sortCall = 0;
        int swapCall = 0;
        int pps = 8;

        int partition(int low, int high)
        {
            double pivot = toSort[high];
            int i = (low-1); // index of smaller element
            for (int j=low; j<high; j++)
            {
                // If current element is smaller than the pivot
                if (toSort[j] < pivot)
                {
                    i++;
                    swap(i, j);
                }
            }
            // swap arr[i+1] and arr[high] (or pivot)
            swap(i + 1, high);
            return i+1;
        }

        void swap(int i, int j){
            // swap arr[i] and arr[j]
            double temp = toSort[i];
            toSort[i] = toSort[j];
            toSort[j] = temp;
            int size = (int) Math.sqrt(this.toSort.length);
            if(toSort[i] != toSort[j] && swapCall % size == 0){
                int p = size * pps;
                images.add(ImageUtils.bytesToBufferedImage(ImageUtils.resizeNearest(fromGreyScale(this.toSort), new int[]{p,p})));
            }
            swapCall++;
        }


        /* The main function that implements QuickSort()
          arr[] --> Array to be sorted,
          low  --> Starting index,
          high  --> Ending index */
        void sort(int low, int high)
        {
            if (low < high)
            {
                sortCall++;
                System.out.println("sortCall:" + sortCall);
            /* pi is partitioning index, arr[pi] is
              now at right place */
                int pi = partition(low, high);

                // Recursively sort elements before
                // partition and after partition
                sort(low, pi-1);
                sort(pi+1, high);
            }
        }

        private double[] toSort;
        private List<BufferedImage> images;

        public QuickSort(double[] input){
            toSort = input;
        }

        public void sort(){
            images = new ArrayList<>();
            this.sort( 0, toSort.length-1);
            GifDecoder gd = new GifDecoder();
            try {
                ImageUtils.saveGifBuffered(images.toArray(new BufferedImage[images.size()]), gd, "src/res/sortRandom.gif", 1, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
