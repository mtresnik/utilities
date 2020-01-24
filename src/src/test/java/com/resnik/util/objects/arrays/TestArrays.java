package com.resnik.util.objects.arrays;

import com.resnik.util.images.GifDecoder;
import com.resnik.util.images.ImageUtils;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

public class TestArrays {

    @Test
    public void testArrays() throws IOException {
        int width = 1000, height = 1000;
        byte[] red = ImageUtils.awtToByte(Color.red);
        byte[] yellow = ImageUtils.awtToByte(Color.yellow);
        byte[] orange = new byte[]{(byte)180,(byte) 100, 0};
        byte[] blue = new byte[]{0,0,(byte) 100};
        byte[] green = new byte[]{0,(byte)100, 0};

        byte[][] pallete = new byte[][]{blue, green};

        BufferedImage[] frames = new BufferedImage[50];
        byte[][][] inputImage = new byte[height][width][];
        for (int ROW = 0; ROW < height; ROW++) {
            for (int COL = 0; COL < width; COL++) {
                inputImage[ROW][COL] = pallete[(int)(Math.random()*pallete.length)];
            }
        }
        int count = 0;
        int max = (width*height)/2;
        System.out.println(Arrays.deepToString(pallete));
        for (int i = 0; i < frames.length ; i++) {
            for (int ROW = 0; ROW < height ; ROW++) {
                int NEW_ROW = (int)(Math.random()*height);
                for (int COL = 0; COL < width; COL++) {
                    int NEW_COL = (int)(Math.random()*width);
                    inputImage[NEW_ROW][NEW_COL] = pallete[(int)(Math.random()*pallete.length)];
                }
                count++;
            }
            frames[i] = ImageUtils.bytesToBufferedImage(inputImage);
        }
        GifDecoder gd = new GifDecoder();
        ImageUtils.saveGifBuffered(frames, gd, "res/static.gif");
    }

}
