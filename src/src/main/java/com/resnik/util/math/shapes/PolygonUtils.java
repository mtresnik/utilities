package com.resnik.util.math.shapes;

import com.resnik.util.images.ImageUtils;
import com.resnik.util.logger.Log;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PolygonUtils {


    public static final String TAG = PolygonUtils.class.getSimpleName();

    public static Polygon randomPolygon(Shape inner, Shape outer, int NUM_POINTS){
        List<PolygonPoint> points = new ArrayList<>();
        BoundingBox outerBBOX = outer.toBoundingBox();
        while(points.size() < NUM_POINTS){
            double testX = (outerBBOX.maxX() - outerBBOX.minX()) * Math.random() + outerBBOX.minX();
            double testY = (outerBBOX.maxY() - outerBBOX.minY()) * Math.random() + outerBBOX.minY();
            PolygonPoint testPoint = new PolygonPoint(testX, testY);
            if(outer.contains(testPoint) && !inner.contains(testPoint)){
                points.add(testPoint);
            }
        }
        return new Polygon(points);
    }

    public static void writeRandomImage(int height, int width){
        byte[][][] image = new byte[height][width][];
        for(int ROW = 0; ROW < height; ROW++){
            for(int COL = 0; COL < width; COL++){
                image[ROW][COL] = ImageUtils.WHITE_ARGB;
            }
        }
        BoundingBox outer = new BoundingBox(0,0,width, height);
        int innerWidth = width/2;
        int innerHeight = height/2;
        BoundingBox inner = new BoundingBox(innerWidth/2, innerHeight/2, width - innerWidth/2, height - innerHeight/2);
        int size = 30;
        int recurr = 1;
        Polygon p = randomPolygon(inner, outer, size);
        for(int i = 0; i < recurr; i++){
            p = randomPolygon(inner, p, size);
        }
        Log.e(TAG, "init:" + p);
        List<Polygon> splitPloy = p.splitPointCloud(2);
        Log.e(TAG, "SplitPolySize=" + splitPloy.size());
        byte[][] colors = new byte[][]{
                new byte[]{(byte) 200, (byte) 0, (byte) 0},
                new byte[]{(byte) 0, (byte) 200, (byte) 0},
                ImageUtils.BLACK_ARGB,
        };
        for(int i = 0; i < splitPloy.size(); i++){
            Polygon subPoly = splitPloy.get(i);
            Log.e(TAG, "sub:" + subPoly);
            byte[] pixel = colors[i];
            for(int ROW = 0; ROW < height; ROW++){
                for(int COL = 0; COL < width; COL++){
                    PolygonPoint test = new PolygonPoint(COL, ROW);
                    if(subPoly.contains(test)){
                        image[ROW][COL] = pixel;
                    }
                }
            }
        }
        try {
            ImageUtils.saveImageBytes(image, "res/test.bmp");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        BoundingBox outer = new BoundingBox(-1, -1, 1, 1);
        BoundingBox inner = new BoundingBox(-0.5, -0.5, 0.5, 0.5);
        randomPolygon(inner, outer, 100);
        writeRandomImage(720,1280);
    }



}
