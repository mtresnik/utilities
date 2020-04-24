package com.resnik.util.math.fractals;

import com.resnik.util.images.GifDecoder;
import com.resnik.util.images.ImageUtils;
import com.resnik.util.logger.Log;
import com.resnik.util.math.symbo.algebra.ComplexNumber;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Mandelbrot {

    public static final String TAG = Mandelbrot.class.getSimpleName();

    public static int MAX_CONVERGENCE = 100;

    public static int convergenceLinear(ComplexNumber c){
        ComplexNumber z = ComplexNumber.ZERO;
        int n = 0;
        while (z.r() <= 2 && n < MAX_CONVERGENCE){
            z = z.multiply(z).add(c);
            n++;
        }
        return n;
    }

    public static Map<Map.Entry<Integer, Integer>, Double> rawMap = new LinkedHashMap<>();

    public static byte[][][] mandelbrot(int n){
        double iMin = -1.5;
        double iMax = 1.5;
        double xMin = -2;
        double xMax = 1;
        int height = 2000;
        int width = height;
        double d_i = (iMax - iMin)/height;
        double d_x = (xMax - xMin)/width;
        byte[][][] retImage = new byte[height][width][];
        for(int ROW = 0; ROW < height; ROW++){
            double curr_i = d_i*ROW + iMin;
            for(int COL = 0; COL < width; COL++){
                double curr_x = d_x*COL + xMin;
                byte[] pixel = ImageUtils.WHITE_B;
                double convergenceNumber = convergenceLinear(new ComplexNumber(curr_x, curr_i));
                if(convergenceNumber >= n || convergenceNumber == MAX_CONVERGENCE){
                    pixel = ImageUtils.BLACK_ARGB;
                }else{
                    double gradient = Math.min(convergenceNumber / n, 1.0);
                    pixel = CircleApproximation.gradientB(gradient);
                }
                retImage[ROW][COL] = pixel;
            }
            Log.v(TAG,"Completed Row:" + ROW);
        }
        return retImage;
    }

    public static void writeGif() throws IOException{
        int max = 65;
        int min = 10;
        int stride = 5;
        BufferedImage[] ret = new BufferedImage[(max - min)/stride];
        int count = 0;
        for(int i = min; i < max; i+= stride){
            byte[][][] retImage = mandelbrot(i);
            ret[count] = ImageUtils.bytesToBufferedImage(retImage);
            count++;
        }
        GifDecoder gd = new GifDecoder();
        ImageUtils.saveGifBuffered(ret, gd, "res/test.gif", 300, true);
    }

    public static byte[][][] convergencePlot(){
        double iMin = -1.5;
        double iMax = 1.5;
        double xMin = -2;
        double xMax = 1;
        int height = 2000;
        int width = height;
        double d_i = (iMax - iMin)/height;
        double d_x = (xMax - xMin)/width;
        byte[][][] retImage = new byte[height][width][];
        for(int ROW = 0; ROW < height; ROW++){
            double curr_i = d_i*(height - ROW - 1) + iMin;
            for(int COL = 0; COL < width; COL++){
                double curr_x = d_x*COL + xMin;
                ComplexNumber curr = new ComplexNumber(curr_x, curr_i);
                double convergenceNumber = convergenceLinear(curr);
                double gradient = Math.min(convergenceNumber / MAX_CONVERGENCE, 1.0);
                byte[] pixel = CircleApproximation.gradientB(gradient);
                if(convergenceNumber == MAX_CONVERGENCE){
                    pixel = ImageUtils.BLACK_B;
                }
                retImage[ROW][COL] = pixel;
            }
            Log.v(TAG,"Completed Row:" + ROW);
        }
        return retImage;
    }

    public static void main(String[] args) throws IOException {
        writeGif();
//        ImageUtils.saveImageBytes(convergencePlot(), "res/convergence.bmp");
    }

}
