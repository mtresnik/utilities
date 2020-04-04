package com.resnik.util.math.statistics.noise;

import com.resnik.util.images.GifDecoder;
import com.resnik.util.images.ImageUtils;
import com.resnik.util.logger.Log;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;

public class OpenSimplexNoiseTest
{
    private static final int WIDTH = 256;
    private static final int HEIGHT = 256;
    private static final double FEATURE_SIZE = 100;

    public static void main(String[] args)
            throws IOException {

        OpenSimplexNoise noise = new OpenSimplexNoise();
        int size = 50;
        BufferedImage[] images = new BufferedImage[size];
        byte[][] colorArray = new byte[][]{
                {(byte) 255, (byte) 0, (byte) 0},
                {(byte) 255, (byte) 64, (byte) 0},
                {(byte) 255, (byte) 128, (byte) 0},
                {(byte) 255, (byte) 192, (byte) 0},
                {(byte) 255, (byte) 255, (byte) 0},
                {(byte) 192, (byte) 255, (byte) 0},
                {(byte) 128, (byte) 255, (byte) 0},
                {(byte) 64, (byte) 255, (byte) 0},
                {(byte) 0, (byte) 255, (byte) 0},
        };
        for(int iteration = 0; iteration < size; iteration ++){
            Log.e("Simplex", iteration);
            double[][] values = new double[WIDTH][HEIGHT];
            double maxValue = -Double.MAX_VALUE;
            double minValue = Double.MAX_VALUE;
            for(int ROW = 0; ROW < HEIGHT; ROW++){
                for(int COL = 0; COL < WIDTH; COL++){
                    values[ROW][COL] = noise.eval(COL / FEATURE_SIZE, ROW / FEATURE_SIZE, iteration/20.0);
                    maxValue = Math.max(maxValue, values[ROW][COL]);
                    minValue = Math.min(minValue, values[ROW][COL]);
                }
            }
            Log.e("Simplex", minValue + " -> " + maxValue);
            byte[][][] image = new byte[HEIGHT][WIDTH][];
            for(int ROW = 0; ROW < HEIGHT; ROW++){
                for(int COL = 0; COL < WIDTH; COL++){
                    double normalizedValue = (values[ROW][COL] + 1 ) / 2;
                    if(normalizedValue == 1.0){
                        normalizedValue = 0.99;
                    }
                    image[ROW][COL] = colorArray[(int)(normalizedValue*colorArray.length)];
                }
            }
            images[iteration] = ImageUtils.bytesToBufferedImage(image);
        }
        GifDecoder gd = new GifDecoder();
        gd.delay = 100;

        ImageUtils.saveGifBuffered(images, gd, "src/res/perlin.gif");


    }
}