package com.resnik.util.images;

import com.resnik.util.logger.Log;
import com.resnik.util.objects.structures.tree.kd.KDTree;
import com.resnik.util.objects.structures.tree.kd.KDTreeNode;
import com.resnik.util.objects.structures.tree.kd.KDTreeValue;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestImages {

    public static final String TAG = TestImages.class.getSimpleName();

    @Test
    public void testAscii(){
        try {
            byte[][][] giraffe_picture = ImageUtils.loadImageToByteArray("src/res/giraffe.jpg", BufferedImage.TYPE_INT_BGR);

            byte[][][] giraffe_ascii = ImageUtils.coloredASCII(giraffe_picture, BufferedImage.TYPE_INT_BGR);
            ImageUtils.saveImageFromByteArray(giraffe_ascii, "src/res/giraffe_ascii.jpg", BufferedImage.TYPE_INT_BGR);

        } catch (IOException ex) {
            Logger.getLogger(TestImages.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    @Test
    public void testSunglasses(){
        // Note: This takes a while
        try {
            GifDecoder gd = new GifDecoder();
            gd.read("src/res/sunglasses.gif");
            BufferedImage[] buff = ImageUtils.gifDecoderToBufferedImages(gd);
            BufferedImage[] buff_colored = ImageUtils.coloredASCII(buff);
            ImageUtils.saveGifBuffered(buff_colored, gd, "src/res/sunglasses_ascii.gif");
        } catch (IOException ex) {
            Logger.getLogger(TestImages.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void testPolygons(){
        try {
            String name = "mona";
            byte[][][] giraffe_picture = ImageUtils.loadImageToByteArray("src/in/" + name + ".jpg", BufferedImage.TYPE_INT_BGR);
            int height = giraffe_picture.length;
            int width = giraffe_picture[0].length;

            List<double[]> points = new ArrayList<>();
            List<byte[]> matchingColors = new ArrayList<>();
            int randCount = (int) Math.sqrt(width*height)*10;
            for(int i = 0; i < randCount; i++){
                int COL = (int)(Math.random()*width);
                int ROW = (int)(Math.random()*height);
                points.add(new double[]{COL, ROW});
                matchingColors.add(giraffe_picture[ROW][COL]);
            }
            Log.e(TAG, "Rand added:" + randCount);

            byte[][][] image = new byte[height][width][];
            for(int ROW = 0; ROW < height; ROW++){
                for(int COL = 0; COL < width; COL++){
                    image[ROW][COL] = ImageUtils.WHITE_B;
                }
            }
            int dCol = 100;
            int dRow = dCol;
            Integer[][] matching = new Integer[height][width];
            for(int index = 0; index < points.size(); index++){
                if(index % 100 == 0){
                    Log.e(TAG, "Point:" + index);
                }
                double[] point = points.get(index);
                int col = (int) point[0];
                int row = (int) point[1];
                for(int ROW = row - dRow; ROW < row + dRow; ROW++){
                    if(ROW < 0 || ROW >= height){
                        continue;
                    }
                    for(int COL = col - dCol; COL<col+dCol; COL++){
                        if(COL < 0 || COL >= width){
                            continue;
                        }
                        double dx = COL - point[0];
                        double dy = ROW - point[1];
                        double dist = Math.sqrt(dx*dx + dy*dy);
                        if(matching[ROW][COL] == null){
                            matching[ROW][COL] = index;
                        }else{
                            double dx1 = COL - points.get(matching[ROW][COL])[0];
                            double dy1 = ROW - points.get(matching[ROW][COL])[1];
                            double dist1 = Math.sqrt(dx1*dx1 + dy1*dy1);
                            if(dist < dist1){
                                matching[ROW][COL] = index;
                            }
                        }

                    }
                }
            }
            for(int ROW = 0; ROW < height; ROW++){
                for(int COL = 0; COL < width; COL++){
                    int minIndex = matching[ROW][COL];
                    image[ROW][COL] = matchingColors.get(minIndex);
                }
            }

            try {
                ImageUtils.saveImageBytes(image, "src/res/"+ name + ".bmp");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void testKDImage(){
        try {
            String name = "mike";
            byte[][][] giraffe_picture = ImageUtils.loadImageToByteArray("src/in/" + name + ".jpg", BufferedImage.TYPE_INT_BGR);
            int height = giraffe_picture.length;
            int width = giraffe_picture[0].length;

            KDTree<byte[]> kdtree = new KDTree<>(2);
            List<double[]> points = new ArrayList<>();
            List<byte[]> matchingColors = new ArrayList<>();
            int randCount = 600;
            for(int i = 0; i < randCount; i++){
                int COL = (int)(Math.random()*width);
                int ROW = (int)(Math.random()*height);
                double[] point = new double[]{COL, ROW};
                byte[] pixel = giraffe_picture[ROW][COL];
                points.add(point);
                matchingColors.add(pixel);
                kdtree.insert(new KDTreeValue<>(point, pixel));
            }
            Log.e(TAG, "Rand added:" + randCount);

            byte[][][] image = new byte[height][width][];
            for(int ROW = 0; ROW < height; ROW++){
                for(int COL = 0; COL < width; COL++){
                    image[ROW][COL] = ImageUtils.WHITE_B;
                }
            }
            for(int ROW = 0; ROW < height; ROW++){
                Log.e(TAG, "Row Perc:" + ROW / (height * 1.0));
                for(int COL = 0; COL < width; COL++){
                    KDTreeNode<byte[]> closest = kdtree.closest(new double[]{COL, ROW});
                    image[ROW][COL] = closest.value.data;
                }
            }

            try {
                ImageUtils.saveImageBytes(image, "src/res/"+ name + "_kd.bmp");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testRescale(){
        try {
            BufferedImage bufferedImage = ImageIO.read(new File("src/res/giraffe.png"));
            bufferedImage = ImageUtils.scaleBest(bufferedImage, 1280, 720);
            ImageIO.write(bufferedImage, "png", new File("src/res/giraffe_up.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
