package com.resnik.util.geo;

import com.resnik.util.images.ImageUtils;
import com.resnik.util.logger.Log;
import com.resnik.util.objects.structures.tree.kd.KDTree;
import com.resnik.util.objects.structures.tree.kd.KDTreeValue;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class TestFlat {


    public void testFlat() throws IOException {
        byte[][][] world = ImageUtils.loadImageBytes("src/res/sunday.jpg");
        int radius = world.length;
        byte[][][] retImage = new byte[radius*2][radius*2][];

        for(int ROW = 0; ROW < retImage.length; ROW++){
            for(int COL = 0; COL < retImage.length; COL++){
                retImage[ROW][COL] = ImageUtils.BLACK_B;
            }
        }
        int centerX = radius;
        int centerY = radius;

        double dTheta = 5e-4;

        for(int ROW = 0; ROW < world.length; ROW++){
            Log.d("FLAT", "ROW:" + ROW + "/" + world.length);
            double local_radius = ROW;
            Map<Double, byte[]> thetaMap = new LinkedHashMap<>();
            for(int COL = 0; COL < world[0].length; COL++){
                double t = ((double) COL) / (world[0].length - 1.0);
                double theta = t * Math.PI * 2;
                double x = Math.cos(theta)*local_radius + centerX;
                double y = Math.sin(theta)*local_radius + centerY;
                int xCoord = world[0].length - 1 - (int) x;
                xCoord = Math.max(xCoord, 0);
                int yCoord = (int) y;
                retImage[yCoord][xCoord] = world[ROW][COL];
                thetaMap.put(theta, world[ROW][COL]);
            }
            for(double theta = 0; theta < Math.PI * 2; theta+= dTheta){
                if(thetaMap.containsKey(theta)){
                    continue;
                }
                double x = Math.cos(theta)*local_radius + centerX;
                double y = Math.sin(theta)*local_radius + centerY;
                int xCoord = world[0].length - 1 - (int) x;
                int yCoord = (int) y;
                byte[] pixel = ImageUtils.BLACK_B;
                for(Map.Entry<Double, byte[]> entry : thetaMap.entrySet()){
                    if(entry.getKey() >= theta){
                        pixel = entry.getValue();
                        break;
                    }
                }
                retImage[yCoord][xCoord] = pixel;
            }
        }


        ImageUtils.saveImageBytes(retImage, "src/res/flat_sunday.bmp");

    }

}
